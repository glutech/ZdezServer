package cn.com.zdez.service;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import cn.com.zdez.cache.UserCache;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.dao.StudentDao;
import cn.com.zdez.po.Student;
import cn.com.zdez.util.MD5;
import cn.com.zdez.vo.StudentVo;

public class StudentService {

	StudentDao dao = new StudentDao();

	// /**
	// * 教师所属部门属于一个特殊专业，可以通过专业id获取该部门下的教师
	// * @param majorId
	// * @return list of teachers(student entity)
	// */
	// public List<Student> getTeacherList(int majorId) {
	// return dao.getTeacherList(majorId);
	// }

	/**
	 * 通过学生用户名获取学生详细信息
	 * 
	 * @param username
	 * @return student entity
	 */
	public Student getStudentById(int stuId) {
		return dao.getStudentById(stuId);
	}

	/**
	 * 通过学生idList获得学生信息
	 * 
	 * @param stuIdList
	 * @return
	 */
	public List<Student> getStudentByIdList(List<Integer> stuIdList) {
		return dao.getStudentByIdList(stuIdList);
	}

	/**
	 * 获取对应专业和年级之下的所有学生
	 * 
	 * @param gradeId
	 * @param majorId
	 * @return list of students' id
	 */
	public List<Integer> getStudentByGradeMajor(String[] gradeId,
			String[] majorId) {
		return dao.getStudentByGradeMajor(gradeId, majorId);
	}

	/**
	 * 获取对应专业年级和性别之下的所有学生
	 * 
	 * @param gradeId
	 * @param majorId
	 * @return
	 */
	public List<Integer> getStudentByGradeMajor(String[] gradeId,
			String[] majorId, String[] gender) {
		return dao.getStudentByGradeMajor(gradeId, majorId, gender);
	}

	/**
	 * 获取学生数，用于分页
	 * 
	 * @return
	 */
	public int getStudentCount() {
		return dao.getStudentCount();
	}

	/**
	 * 通过页码获取要显示的学生信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<StudentVo> getStudentByPage(int start, int end) {
		return dao.getStudentByPage(start, end);
	}

	/**
	 * 通过学生用户名获取学生的详细信息
	 * 
	 * @param username
	 * @return
	 */
	public StudentVo getStudentVoByUsername(String username) {
		// return dao.getStudentVoByUsername(username);
		return new UserCache().getStudentInfoFromCache(username);
	}

	public StudentVo getStudentVoByUsernameFromDB(String username) {
		return dao.getStudentVoByUsername(username);
	}

	/**
	 * 根据关键字，获取符合条件的学生记录的数量，学生管理中查询时使用 ---目前只能查询用户名，需要进一步加强---
	 * 
	 * @param keyword
	 * @return
	 */
	public int getStudentQueryCount(String keyword) {
		return dao.getStudentQueryCount(keyword);
	}

	/**
	 * 通过页码和查询的关键字获取要显示的学生信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<StudentVo> getStudentByPage(int start, int end, String keyword) {
		return dao.getStudentByPage(start, end, keyword);
	}

	/**
	 * 根据学生id删除学生
	 * 
	 * @param stuId
	 * @return
	 */
	public boolean deleteStudent(int stuId) {
		return dao.deleteStudent(stuId);
	}

	/**
	 * 根据用户id修改staus字段
	 * 
	 * @param stuId
	 * @param staus
	 * @return
	 */
	public boolean modifyStaus(String username, String staus) {
		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();
		try {
			jedis.hset("student:" + username, "staus", staus);
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();

		return dao.modifyStaus(username, staus);
	}

	/**
	 * 用户从客户端修改密码
	 * 
	 * @param stuId
	 * @param newPassword
	 * @return
	 */
	public boolean modifyPassword(int stuId, String oldPassword,
			String newPassword) {
		boolean flag = false;
		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();

		String psw = dao.getPassword(stuId);
		if (psw.equals(new MD5().toMD5String(oldPassword))) {
			flag = dao.modifyPassword(stuId, newPassword);
			String username = dao.getUsernameById(stuId);

			// 修改缓存中的密码
			try {
				jedis.hset("student:" + username, "password",
						new MD5().toMD5String(newPassword));
			} finally {
				pool.returnResource(jedis);
			}
		}

		pool.destroy();
		return flag;
	}

	/**
	 * 修改学生信息
	 * 
	 * @param stu
	 * @return
	 */
	public boolean modifyStudentInfo(Student stu) {
		return dao.modifyStudentInfo(stu);
	}

	/**
	 * 获取所有学生的id
	 * 
	 * @return
	 */
	public List<Integer> getAllStuId() {
		List<Integer> list = new ArrayList<Integer>();
		List<Student> stuList = dao.getAll();
		for (Student s : stuList) {
			list.add(s.getId());
		}
		return list;
	}

	/**
	 * 获取某一学校下的所有学生id
	 * 
	 * @param school
	 * @return
	 */
	public List<Integer> getStuIdListBySchool(String[] school) {
		List<Integer> list = new ArrayList<Integer>();
		List<Student> stuList = dao.getStuBySchool(school);
		for (int i = 0, count = stuList.size(); i < count; i++) {
			list.add(stuList.get(i).getId());
		}
		return list;
	}

	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 * @return
	 */
	public boolean isExist(String username) {
		return dao.isExist(username);
	}

	/**
	 * 增加学生时使用，由于增加了密码的md5加密，不能直接运行sql向数据库中插入数据
	 * 
	 * @param stu
	 * @return
	 */
	public boolean newStudent(Student stu) {
		return dao.newStudent(stu);
	}

	public boolean changePswToMd5(int begin, int end) {
		return dao.changePswToMd5(begin, end);
	}

	public List<Student> getAll() {
		return dao.getAll();
	}

	public List<Integer> getTopStudentIds(int num) {
		return dao.getTopStudentIds(num);
	}

	public void cacheStudentInfoAll() {
		dao.cacheStudentAll();
	}
	
	/*
	 * 用于新建信息时抄送列表的对象选择。从数据库中获取学生处、就业处、团委与保卫处的老师列表
	 */
	public List<Student> getTeacherByMajor(int majorId) {
		return dao.getTeacherByMajor(majorId);
	}
}
