package cn.com.zdez.cache;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.spi.DirStateFactory.Result;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.dao.ConnectionFactory;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.dao.SQLExecution;
import cn.com.zdez.dao.SchoolAdminDao;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.Student;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.vo.StudentVo;

public class UserCache {

	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();

	/**
	 * 从数据库中取出所有schoolAdmin的信息并放入缓存
	 */
	public void cacheSchoolAdminInfoAll() {
		List<SchoolAdmin> sAdminList = new SchoolAdminDao().getAll();
		for (int i = 0, count = sAdminList.size(); i < count; i++) {
			ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();

			try {

				jedis.sadd("schoolAdminList", sAdminList.get(i).getUsername());

				map.put("username", sAdminList.get(i).getUsername());
				map.put("password", sAdminList.get(i).getPassword());
				map.put("telPhone",
						(sAdminList.get(i).getTelPhone() == null ? ""
								: sAdminList.get(i).getTelPhone()));
				map.put("name", (sAdminList.get(i).getName() == null ? ""
						: sAdminList.get(i).getName()));
				map.put("auth", Integer.toString(sAdminList.get(i).getAuth()));
				map.put("schoolId",
						(Integer.toString(sAdminList.get(i).getAuth()) == null ? ""
								: Integer.toString(sAdminList.get(i).getAuth())));
				map.put("departmentId",
						(Integer.toString(sAdminList.get(i).getDepartmentId()) == null ? ""
								: Integer.toString(sAdminList.get(i)
										.getDepartmentId())));
				map.put("majorId",
						(Integer.toString(sAdminList.get(i).getMajorId()) == null ? ""
								: Integer.toString(sAdminList.get(i)
										.getMajorId())));
				map.put("remarks", (sAdminList.get(i).getRemarks() == null ? ""
						: sAdminList.get(i).getRemarks()));

				jedis.hmset("schoolAdmin:" + sAdminList.get(i).getUsername(),
						map);

			} finally {
				pool.returnResource(jedis);
			}

		}

		pool.destroy();
	}

	/**
	 * 缓存单个SchoolAdmin的信息，用于缓存中没有schoolAdmin信息时
	 * 
	 * @param username
	 */
	public boolean cacheSchoolAdminInfoByUname(String username) {
		boolean flag = false;
		SchoolAdmin sAdmin = new SchoolAdminService()
				.getSchoolAdminInfoFromMySQL(username);
		if (sAdmin.getUsername() != null) {

			try {

				jedis.sadd("schoolAdminList", sAdmin.getUsername());

				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();

				map.put("username", sAdmin.getUsername());
				map.put("password", sAdmin.getPassword());
				map.put("telPhone",
						(sAdmin.getTelPhone() == null ? "" : sAdmin
								.getTelPhone()));
				map.put("name",
						(sAdmin.getName() == null ? "" : sAdmin.getName()));
				map.put("auth", Integer.toString(sAdmin.getAuth()));
				map.put("schoolId",
						(Integer.toString(sAdmin.getSchoolId()) == null ? ""
								: Integer.toString(sAdmin.getSchoolId())));
				map.put("departmentId",
						(Integer.toString(sAdmin.getDepartmentId()) == null ? ""
								: Integer.toString(sAdmin.getDepartmentId())));
				map.put("majorId",
						(Integer.toString(sAdmin.getMajorId()) == null ? ""
								: Integer.toString(sAdmin.getMajorId())));
				map.put("remarks",
						(sAdmin.getRemarks() == null ? "" : sAdmin.getRemarks()));

				jedis.hmset("schoolAdmin:" + sAdmin.getUsername(), map);

			} finally {
				pool.returnResource(jedis);
			}
			flag = true;
		}

		pool.destroy();
		return flag;
	}

	/**
	 * 根据schoolAdmin的用户名，从缓存中取信息
	 * 
	 * @param username
	 * @return
	 */
	public SchoolAdmin getInfoFromCache(String username) {
		SchoolAdmin sAdmin = new SchoolAdmin();
		String temp = "";
		String key = "schoolAdmin:" + username;

		try {

			temp = jedis.hget(key, "username");
			sAdmin.setUsername(temp);
			temp = jedis.hget(key, "password");
			sAdmin.setPassword(temp);
			temp = jedis.hget(key, "telPhone");
			sAdmin.setTelPhone(temp);
			temp = jedis.hget(key, "name");
			sAdmin.setName(temp);
			temp = jedis.hget(key, "auth");
			sAdmin.setAuth(Integer.parseInt(temp));
			temp = jedis.hget(key, "schoolId");
			sAdmin.setSchoolId(Integer.parseInt(temp));
			temp = jedis.hget(key, "departmentId");
			sAdmin.setDepartmentId(Integer.parseInt(temp));
			temp = jedis.hget(key, "majorId");
			sAdmin.setMajorId(Integer.parseInt(temp));
			temp = jedis.hget(key, "remarks");
			sAdmin.setRemarks(temp);

		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
		return sAdmin;
	}

	/**
	 * 缓存数据库中所有学生的信息。由于student表中记录数太多，不到万不得已不要调用。
	 * redis清空后可以调用，但是调用此方法时最好在凌晨，要不然影响系统正常使用
	 * 
	 * @param list
	 */
	public void cacheStudentInfoAll(List<StudentVo> list) {

		try {

			String keyAll = "studentList";
			for (int i = 0, count = list.size(); i < count; i++) {
				String key = "student:";
				StudentVo sVo = list.get(i);
				jedis.sadd(keyAll, sVo.getUsername());

				key = key + sVo.getUsername();

				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
				map.put("id", Integer.toString(sVo.getId()));
				map.put("username", sVo.getUsername());
				map.put("password", sVo.getPassword());
				map.put("name", sVo.getName() == null ? "" : sVo.getName());
				map.put("gender",
						sVo.getGender() == null ? "" : sVo.getGender());
				map.put("grade", sVo.getGrade() == null ? "" : sVo.getGrade());
				map.put("major", sVo.getMajor() == null ? "" : sVo.getMajor());
				map.put("department",
						sVo.getDepartment() == null ? "" : sVo.getDepartment());
				map.put("staus", sVo.getStaus());

				jedis.hmset(key, map);
			}

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 当缓存中没有学生信息时，从数据库中取出学生信息并写入缓存
	 * 
	 * @param username
	 */
	public boolean cacheStudentInfoByUname(Student stu) {
		boolean flag = false;
		StudentService sService = new StudentService();
		if (sService.isExist(stu.getUsername())) {

			if (sService.modifyStaus(stu.getUsername(), stu.getStaus())) {
				StudentVo sVo = sService.getStudentVoByUsernameFromDB(stu
						.getUsername());
				String keyAll = "studentList";
				String key = "student:" + sVo.getUsername();

				try {

					jedis.sadd(keyAll, sVo.getUsername());

					ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
					map.put("id", Integer.toString(sVo.getId()));
					map.put("username", sVo.getUsername());
					map.put("password", sVo.getPassword());
					map.put("name", sVo.getName() == null ? "" : sVo.getName());
					map.put("gender",
							sVo.getGender() == null ? "" : sVo.getGender());
					map.put("grade",
							sVo.getGrade() == null ? "" : sVo.getGrade());
					map.put("major",
							sVo.getMajor() == null ? "" : sVo.getMajor());
					map.put("department", sVo.getDepartment() == null ? ""
							: sVo.getDepartment());
					map.put("staus", sVo.getStaus());

					jedis.hmset(key, map);
				} finally {
					pool.returnResource(jedis);
				}
				flag = true;
			}

			pool.destroy();
		}
		return flag;
	}

	/**
	 * 根据用户名从缓存中获取学生信息
	 * 
	 * @param username
	 * @return
	 */
	public StudentVo getStudentInfoFromCache(String username) {
		StudentVo sVo = new StudentVo();

		try {

			String key = "student:" + username;
			String temp = "";

			temp = jedis.hget(key, "id");
			sVo.setId(Integer.parseInt(temp));
			temp = jedis.hget(key, "username");
			sVo.setUsername(username);
			temp = jedis.hget(key, "password");
			sVo.setPassword(temp);
			temp = jedis.hget(key, "name");
			sVo.setName(temp);
			temp = jedis.hget(key, "gender");
			sVo.setGender(temp);
			temp = jedis.hget(key, "grade");
			sVo.setGrade(temp);
			temp = jedis.hget(key, "major");
			sVo.setMajor(temp);
			temp = jedis.hget(key, "department");
			sVo.setDepartment(temp);
			temp = jedis.hget(key, "school");
			sVo.setSchool(temp);
			temp = jedis.hget(key, "staus");
			sVo.setStaus(temp);

		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
		return sVo;
	}

	public void cacheStuIdUnameAll() {
		String sql = "select id, username from student";
		Object[] params = {};
		ResultSet rs = new SQLExecution().execSqlWithRS(sql, params);
		String key = "stu:id:username";
		try {
			while (rs.next()) {
				jedis.hset(key, Integer.toString(rs.getInt("id")), rs.getString("username"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}

}
