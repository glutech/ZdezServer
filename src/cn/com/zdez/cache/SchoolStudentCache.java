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

/**
 * 将学生与学校的对应关系缓存在redis中，用于数据统计，避免在统计时频繁访问数据库
 * 
 * @author jokinryou
 * 
 */
public class SchoolStudentCache {

	public void Cache() {
		JedisPool pool = new RedisConnection().getConnection();
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

}
