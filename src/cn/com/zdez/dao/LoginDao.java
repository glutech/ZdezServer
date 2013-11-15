package cn.com.zdez.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import cn.com.zdez.cache.UserCache;
import cn.com.zdez.po.Admin;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.Student;

public class LoginDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 验证公司管理员用户
	 * 
	 * @param admin
	 * @return true or false
	 */
	public boolean admin_loginCheck(Admin admin) {
		boolean flag = false;
		String sql = "select username from admin where username=? and password=?";
		Object[] params = { admin.getUsername(), admin.getPassword() };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 验证学校管理员用户
	 * 
	 * @param schoolAdmin
	 * @return true or false
	 */
	// 到数据库中验证，加入缓存之后，此方法弃用
	// public boolean schoolAdmin_loginCheck(SchoolAdmin schoolAdmin) {
	// boolean flag = false;
	// String sql =
	// "select username from schoolAdmin where username=? and password=?";
	// Object[] params = { schoolAdmin.getUsername(),
	// schoolAdmin.getPassword() };
	// ResultSet rs = sqlE.execSqlWithRS(sql, params);
	// try {
	// while (rs.next()) {
	// flag = true;
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return flag;
	// }

	public boolean schoolAdmin_loginCheck(SchoolAdmin schoolAdmin) {
		boolean flag = false;

		if (this.checkPsw(schoolAdmin)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 从缓存中检查用户名密码是否正确
	 * 
	 * @param schoolAdmin
	 * @return
	 */
	public boolean checkPsw(SchoolAdmin schoolAdmin) {
		boolean flag = false;
		UserCache cache = new UserCache();
		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();

		try {

			if (jedis.sismember("schoolAdminList", schoolAdmin.getUsername())) {
				if (schoolAdmin.getPassword().equals(
						jedis.hget("schoolAdmin:" + schoolAdmin.getUsername(),
								"password"))) {
					flag = true;
				}
			} else {
				// 缓存中没有相关数据，则从数据库中取出数据并写入缓存
				if (cache.cacheSchoolAdminInfoByUname(schoolAdmin.getUsername())) {
					flag = this.checkPsw(schoolAdmin);
				}
			}

		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();

		return flag;

	}

	/**
	 * 验证客户端学生用户登陆
	 * 
	 * @param student
	 * @return true or false
	 */
	// 这是从数据库中取数据验证的方法，弃用
	// public boolean studentLoginCheck(Student student) {
	// boolean flag = false;
	// String sql = "select id from student where username=? and password=?";
	// Object[] params = { student.getUsername(), student.getPassword() };
	// ResultSet rs = sqlE.execSqlWithRS(sql, params);
	// try {
	// while (rs.next()) {
	// flag = true;
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// return flag;
	// }

	/**
	 * 客户端登录验证，验证缓存中的数据
	 * 
	 * @param student
	 * @return
	 */
	public boolean studentLoginCheck(Student student) {
		boolean flag = false;
		if (this.checkPsw(student)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 验证客户端传入的用户名、密码和设备号，缓存中有数据则验证缓存，没有则从数据库中取数据并写入缓存
	 * 
	 * @param student
	 * @return
	 */
	public boolean checkPsw(Student student) {
		boolean flag = false;

		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();
		UserCache cache = new UserCache();

		try {

			if (jedis.sismember("studentList", student.getUsername())) {
				if (jedis.hget("student:" + student.getUsername(), "password")
						.equals(student.getPassword())) {
					jedis.hset("student:" + student.getUsername(), "staus", student.getStaus());
					flag = true;
				}
			} else {
				// 缓存中没有数据时，从数据库中取出并放进缓存
				if (cache.cacheStudentInfoByUname(student)) {
					flag = this.checkPsw(student);
				}
			}

		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
		return flag;
	}
}
