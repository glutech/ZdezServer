package cn.com.zdez.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import cn.com.zdez.dao.ConnectionFactory;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.dao.SQLExecution;

/**
 * 将学生与学校的对应关系缓存在redis中，用于数据统计，避免在统计时频繁访问数据库
 * 
 * @author jokinryou
 * 
 */
public class SchoolStudentCache {

	public static JedisPool pool = new RedisConnection().getConnection();

	public void Cache() {
		Jedis jedis = pool.getResource();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select student.id as stuId, school.id as schoolId from student,school "
					+ "where school.id = any (select schoolId from department "
					+ "where id = any (select departmentId from major where id = student.majorId))";
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();
			while (rs.next()) {
				map.put(Integer.toString(rs.getInt("stuId")),
						Integer.toString(rs.getInt("schoolId")));
			}
			jedis.hmset("hashmap:stuId:schoolId", map);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 当学生拉取信息时，缓存学生与学校的对应关系，为了统计页面不出错。 调用此方法后，this.Cache()就可以不用了，加快了公司管理员登录速度。
	 * 
	 * @param stuId
	 */
	public void CacheStuSchool(int stuId) {

		Jedis jedis = pool.getResource();
		try {
			String key = "hashmap:stuId:schoolId";
			String schoolIdStr = jedis.hget(key, Integer.toString(stuId));
			if (schoolIdStr == null) {
				int schoolId = 0;
				String sql = "select schoolId from department, major, student where department.id = (select departmentId from major where major.id = (select majorId from student where id = ?)) limit 0,1";
				Object[] params = {stuId};
				ResultSet rs = new SQLExecution().execSqlWithRS(sql, params);
				while (rs.next()) {
					schoolId = rs.getInt(1);
				}
				jedis.hset(key, Integer.toString(stuId), Integer.toString(schoolId));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);

		}
		pool.destroy();

	}

}
