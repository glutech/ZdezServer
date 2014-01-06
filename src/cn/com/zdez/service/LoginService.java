package cn.com.zdez.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.dao.LoginDao;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.po.Admin;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.Student;
import cn.com.zdez.vo.StudentVo;

public class LoginService {
	
	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();
	
	LoginDao dao = new LoginDao();

	/**
	 * 公司管理员账户登录验证
	 * 
	 * @param admin
	 * @return
	 */
	public boolean admin_loginCheck(Admin admin) {
		return dao.admin_loginCheck(admin);
	}

	/**
	 * 学校管理员账户登录验证
	 * 
	 * @param schoolAdmin
	 * @return
	 */
	public boolean schoolAdmin_loginCheck(SchoolAdmin schoolAdmin) {
		return dao.schoolAdmin_loginCheck(schoolAdmin);
	}

	/**
	 * 学生用户账户登录验证
	 * 
	 * @param student
	 * @return
	 */
	public boolean studentLoginCheck(Student student) {
		return dao.studentLoginCheck(student);
	}
	
	public void newIosUser(StudentVo stuVo) {
		try {
			jedis.hset("stu:id:username", Integer.toString(stuVo.getId()), stuVo.getUsername());
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}

	public void newWPUser(StudentVo stuVo) {
		try {
			jedis.hset("stu:id:username", Integer.toString(stuVo.getId()), stuVo.getUsername());
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}

}
