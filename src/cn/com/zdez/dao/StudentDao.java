package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.UserCache;
import cn.com.zdez.po.Student;
import cn.com.zdez.util.MD5;
import cn.com.zdez.vo.StudentVo;

public class StudentDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 通过学生用户名获取学生详细信息
	 * 
	 * @param username
	 * @return student entity
	 */
	public Student getStudentById(int Id) {
		Student stu = new Student();
		String sql = "select * from student where id=?";
		Object[] params = { Id };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				stu.setId(rs.getInt("id"));
				stu.setUsername(rs.getString("username"));
				stu.setPassword(rs.getString("password"));
				stu.setName(rs.getString("name"));
				stu.setGender(rs.getString("gender"));
				stu.setAdmissionTime(rs.getTime("admissionTime"));
				stu.setStatus(rs.getInt("status"));
				stu.setStaus(rs.getString("staus"));
				stu.setGradeId(rs.getInt("gradeId"));
				stu.setMajorId(rs.getInt("majorId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stu;
	}

	/**
	 * 通过学生idList获得学生信息
	 * 
	 * @param stuIdList
	 * @return
	 */
	public List<Student> getStudentByIdList(List<Integer> stuIdList) {
		List<Student> list = new ArrayList<Student>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select * from student where id = ?";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0, count = stuIdList.size(); i < count; i++) {
				pstmt.setInt(1, stuIdList.get(i));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Student stu = new Student();
					stu.setId(rs.getInt("id"));
					stu.setUsername(rs.getString("username"));
					stu.setPassword(rs.getString("password"));
					stu.setName(rs.getString("name"));
					stu.setStatus(rs.getInt("status"));
					stu.setStaus(rs.getString("staus"));
					stu.setGradeId(rs.getInt("gradeId"));
					stu.setMajorId(rs.getInt("majorId"));
					list.add(stu);
				}
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 获取对应专业和年级之下的所有学生
	 * 
	 * @param gradeId
	 * @param majorId
	 * @return
	 */
	public List<Integer> getStudentByGradeMajor(String[] gradeId,
			String[] majorId) {
		List<Integer> list = new ArrayList<Integer>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select id from student where gradeId=? and majorId=? and isTeacher = 0";
			pstmt = conn.prepareStatement(sql);
			for (int m = 0, count1 = gradeId.length; m < count1; m++) {
				for (int n = 0, count2 = majorId.length; n < count2; n++) {
					int gradeIdInt = Integer.parseInt(gradeId[m]);
					int majorIdInt = Integer.parseInt(majorId[n]);
					pstmt.setInt(1, gradeIdInt);
					pstmt.setInt(2, majorIdInt);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						list.add(rs.getInt(1));
					}
				}
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
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
		List<Integer> list = new ArrayList<Integer>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select id from student where gradeId=? and majorId=? and gender=?";
			pstmt = conn.prepareStatement(sql);
			for (int m = 0, count1 = gradeId.length; m < count1; m++) {
				for (int n = 0, count2 = majorId.length; n < count2; n++) {
					for (int q = 0, count3 = gender.length; q < count3; q++) {
						int gradeIdInt = Integer.parseInt(gradeId[m]);
						int majorIdInt = Integer.parseInt(majorId[n]);
						String genderString = gender[q];
						pstmt.setInt(1, gradeIdInt);
						pstmt.setInt(2, majorIdInt);
						pstmt.setString(3, genderString);
						ResultSet rs = pstmt.executeQuery();
						while (rs.next()) {
							list.add(rs.getInt(1));
						}
					}
				}
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 获取学生数，用于分页
	 * 
	 * @return
	 */
	public int getStudentCount() {
		int i = -1;
		String sql = "select count(*) from student where isTeacher = 0";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 通过页码获取要显示的学生信息
	 * 
	 * @param start
	 * @param ent
	 * @return
	 */
	public List<StudentVo> getStudentByPage(int start, int end) {
		List<StudentVo> list = new ArrayList<StudentVo>();
		String sql = "select student.id as id, student.username as username, student.name as name, student.gender, "
				+ "grade.description as grade, major.name as major, department.name as department, school.name as school "
				+ "from ((((student join grade) join major) join school) join department) "
				+ "where ((student.gradeId = grade.id) and (student.majorId = major.id) "
				+ "and (department.schoolId = school.id) and (major.departmentId = department.id)) and isTeacher = 0 limit ?,?";
		Object[] params = { start, end };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				StudentVo stuVo = new StudentVo();
				stuVo.setId(rs.getInt("id"));
				stuVo.setUsername(rs.getString("username"));
				stuVo.setName(rs.getString("name"));
				if (rs.getString("gender").equals("M")) {
					stuVo.setGender("男");
				} else if (rs.getString("gender").equals("F")) {
					stuVo.setGender("女");
				}
				stuVo.setGrade(rs.getString("grade"));
				stuVo.setMajor(rs.getString("major"));
				stuVo.setDepartment(rs.getString("department"));
				stuVo.setSchool(rs.getString("school"));
				list.add(stuVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过学生用户名获取学生的详细信息
	 * 
	 * @param username
	 * @return
	 */
	public StudentVo getStudentVoByUsername(String username) {
		StudentVo stuVo = new StudentVo();
		String sql = "select student.id, student.username as username, student.password as password, student.name as name, student.staus, " +
				"student.gender, grade.description as grade, major.name as major, department.name as department, school.name as school "
				+ "from ((((student join grade) join major) join school) join department) "
				+ "where ((student.gradeId = grade.id) and (student.majorId = major.id) "
				+ "and (department.schoolId = school.id) and (major.departmentId = department.id)) "
				+ "and student.username = ?";
		Object[] params = { username };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				stuVo.setId(rs.getInt("id"));
				stuVo.setUsername(rs.getString("username"));
				stuVo.setPassword(rs.getString("password"));
				stuVo.setName(rs.getString("name"));
				if (rs.getString("gender").equals("M")) {
					stuVo.setGender("男");
				} else if (rs.getString("gender").equals("F")) {
					stuVo.setGender("女");
				} else {
					stuVo.setGender("未知");
				}
				stuVo.setGrade(rs.getString("grade"));
				stuVo.setMajor(rs.getString("major"));
				stuVo.setDepartment(rs.getString("department"));
				stuVo.setSchool(rs.getString("school"));
				stuVo.setStaus(rs.getString("staus"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stuVo;
	}

	/**
	 * 根据关键字，获取符合条件的学生记录的数量，学生管理中查询时使用 ---目前只能查询用户名，需要进一步加强---
	 * 
	 * @param keyword
	 * @return
	 */
	public int getStudentQueryCount(String keyword) {
		int i = -1;
		String sql = "select count(*) from student where name like '%"
				+ keyword + "%' and isTeacher = 0";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 通过页码和查询的关键字获取要显示的学生信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<StudentVo> getStudentByPage(int start, int end, String keyword) {
		List<StudentVo> list = new ArrayList<StudentVo>();
		String sql = "select student.id as id, student.username as username, student.name as name, student.gender, "
				+ "grade.description as grade, major.name as major, department.name as department, school.name as school "
				+ "from ((((student join grade) join major) join school) join department) "
				+ "where ((student.gradeId = grade.id) and (student.majorId = major.id) "
				+ "and (department.schoolId = school.id) and (major.departmentId = department.id)) "
				+ "and isTeacher = 0 and student.name like '%"
				+ keyword
				+ "%' limit ?,?";
		Object[] params = { start, end };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				StudentVo stuVo = new StudentVo();
				stuVo.setId(rs.getInt("id"));
				stuVo.setUsername(rs.getString("username"));
				stuVo.setName(rs.getString("name"));
				if (rs.getString("gender").equals("M")) {
					stuVo.setGender("男");
				} else if (rs.getString("gender").equals("F")) {
					stuVo.setGender("女");
				}
				stuVo.setGrade(rs.getString("grade"));
				stuVo.setMajor(rs.getString("major"));
				stuVo.setDepartment(rs.getString("department"));
				stuVo.setSchool(rs.getString("school"));
				list.add(stuVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据学生id删除学生
	 * 
	 * @param stuId
	 * @return
	 */
	public boolean deleteStudent(int stuId) {
		boolean flag = false;
		String sql = "delete from student where id = ?";
		Object[] params = { stuId };
		if (sqlE.execSqlWithoutRS(sql, params)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 根据用户id修改staus字段
	 * 
	 * @param username
	 * @param staus
	 * @return
	 */
	public boolean modifyStaus(String username, String staus) {
		boolean flag = false;
		String sql = "update student set staus = ? where username = ?";
		Object[] params = { staus, username};
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 用户从客户端修改密码
	 * 
	 * @param stuId
	 * @param newPassword
	 * @return
	 */
	public boolean modifyPassword(int stuId, String newPassword) {
		boolean flag = false;
		newPassword = new MD5().toMD5String(newPassword);
		String sql = "update student set password = ? where id = ?";
		Object[] params = { newPassword, stuId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	public String getPassword(int stuId) {
		String psw = "";
		String sql = "select password from student where id = ?";
		Object[] params = { stuId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				psw = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return psw;
	}

	/**
	 * 通过学生id获得用户名，用于修改缓存中的学生密码
	 * 
	 * @param stuId
	 * @return
	 */
	public String getUsernameById(int stuId) {
		String username = "";
		String sql = "select username from student where id = ?";
		Object[] params = { stuId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				username = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return username;
	}

	/**
	 * 修改学生信息
	 * 
	 * @param stu
	 * @return
	 */
	public boolean modifyStudentInfo(Student stu) {
		boolean flag = false;
		String sql = "update student set name = ?, gender = ?, gradeId = ?, majorId = ? where id = ?";
		Object[] params = { stu.getName(), stu.getGender(), stu.getGradeId(),
				stu.getMajorId(), stu.getId() };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 获取所有学生
	 * 
	 * @return
	 */
	public List<Student> getAll() {
		List<Student> list = new ArrayList<Student>();
		String sql = "select * from student";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				Student s = new Student();
				s.setId(rs.getInt("id"));
				s.setUsername(rs.getString("username"));
				s.setPassword(rs.getString("password"));
				s.setName(rs.getString("name"));
				s.setGender(rs.getString("gender"));
				s.setAdmissionTime(rs.getTime("admissionTime"));
				s.setStatus(rs.getInt("status"));
				s.setStaus(rs.getString("staus"));
				s.setGradeId(rs.getInt("gradeId"));
				s.setIsTeacher(rs.getInt("isTeacher"));
				s.setMajorId(rs.getInt("majorId"));
				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取某一学校下的所有学生，用于新闻资讯的发送
	 * 
	 * @param school
	 * @return
	 */
	public List<Student> getStuBySchool(String[] school) {
		List<Student> list = new ArrayList<Student>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select * from student where "
					+ "majorId = any (select id from major "
					+ "where departmentId = any (select id from department "
					+ "where schoolId = ?))";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0, count = school.length; i < count; i++) {
				pstmt.setInt(1, Integer.parseInt(school[i]));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Student stu = new Student();
					stu.setId(rs.getInt("id"));
					stu.setUsername(rs.getString("username"));
					stu.setName(rs.getString("name"));
					stu.setGender(rs.getString("gender"));
					list.add(stu);
				}
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	public boolean isExist(String username) {
		boolean flag = false;
		String sql = "select username from student where username = ?";
		Object[] params = { username };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean newStudent(Student stu) {
		boolean flag = false;
		String sql = "insert into student (username, password, name, gender, status, staus, gradeId, isTeacher, majorId) values (?,?,?,?,?,?,?,?,?)";
		Object[] params = { stu.getUsername(), stu.getPassword(),
				stu.getName(), stu.getGender(), stu.getStatus(),
				stu.getStaus(), stu.getGradeId(), stu.getIsTeacher(),
				stu.getMajorId() };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	public boolean changePswToMd5(int begin, int end) {
		boolean flag = false;
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String sql1 = "select id, password from student where id between ? and ?";
		String sql2 = "update student set password = ? where id = ?";
		try {
			conn = factory.getConnection();
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setInt(1, begin);
			pstmt1.setInt(2, end);
			ResultSet rs1 = pstmt1.executeQuery();
			MD5 md5 = new MD5();
			while (rs1.next()) {
				System.out.println(md5.toMD5String(rs1.getString(2)));
				System.out.println(rs1.getInt(1));
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, md5.toMD5String(rs1.getString(2)));
				pstmt2.setInt(2, rs1.getInt(1));
				pstmt2.executeUpdate();
			}
			flag = true;
			if (pstmt1 != null) {
				pstmt1.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return flag;
	}

	public List<Integer> getTopStudentIds(int num) {
		System.out.println("Getting stu Ids!!!!");
		List<Integer> idList = new ArrayList<Integer>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select id from student limit 0,?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getInt(1));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}

		return idList;
	}

	public void cacheStudentAll() {
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select count(id) from student";
		String sql1 = "select student.id as id, student.username as username, student.password as password, student.name as name, student.staus, " +
				"student.gender, grade.description as grade, major.name as major, department.name as department, school.name as school "
				+ "from ((((student join grade) join major) join school) join department) "
				+ "where ((student.gradeId = grade.id) and (student.majorId = major.id) "
				+ "and (department.schoolId = school.id) and (major.departmentId = department.id)) and isTeacher = 0 limit ?,?";

		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			int idCount = 0;
			while (rs.next()) {
				idCount = rs.getInt(1);
			}

			pstmt = conn.prepareStatement(sql1);

			UserCache cache = new UserCache();

			int mod = idCount % 200;
			int count = idCount / 200;
			for (int i = 1; i <= count; i++) {
				List<StudentVo> tempList = new ArrayList<StudentVo>();
				int start = (i - 1) * 200;
				int end = i * 200;
				pstmt.setInt(1, start);
				pstmt.setInt(2, end);

				ResultSet rs1 = pstmt.executeQuery();
				while (rs1.next()) {
					StudentVo stuVo = new StudentVo();
					stuVo.setId(rs1.getInt("id"));
					stuVo.setUsername(rs1.getString("username"));
					stuVo.setPassword(rs1.getString("password"));
					stuVo.setName(rs1.getString("name"));
					if (rs1.getString("gender").equals("M")) {
						stuVo.setGender("男");
					} else if (rs1.getString("gender").equals("F")) {
						stuVo.setGender("女");
					} else {
						stuVo.setGender("未知");
					}
					stuVo.setGrade(rs1.getString("grade"));
					stuVo.setMajor(rs1.getString("major"));
					stuVo.setDepartment(rs1.getString("department"));
					stuVo.setSchool(rs1.getString("school"));
					stuVo.setStaus(rs.getString("staus"));
					tempList.add(stuVo);
				}

				cache.cacheStudentInfoAll(tempList);
			}

			if (mod != 0) {

				List<StudentVo> tempList = new ArrayList<StudentVo>();

				int start = count * 200;
				int end = start + mod;

				pstmt.setInt(1, start);
				pstmt.setInt(2, end);
				ResultSet rs1 = pstmt.executeQuery();
				while (rs1.next()) {
					StudentVo stuVo = new StudentVo();
					stuVo.setId(rs1.getInt("id"));
					stuVo.setUsername(rs1.getString("username"));
					stuVo.setPassword(rs1.getString("password"));
					stuVo.setName(rs1.getString("name"));
					if (rs1.getString("gender").equals("M")) {
						stuVo.setGender("男");
					} else if (rs1.getString("gender").equals("F")) {
						stuVo.setGender("女");
					} else {
						stuVo.setGender("未知");
					}
					stuVo.setGrade(rs1.getString("grade"));
					stuVo.setMajor(rs1.getString("major"));
					stuVo.setDepartment(rs1.getString("department"));
					stuVo.setSchool(rs1.getString("school"));
					stuVo.setStaus(rs.getString("staus"));
					tempList.add(stuVo);
				}

				cache.cacheStudentInfoAll(tempList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 用于新建信息时抄送列表的对象选择。从数据库中获取学生处、就业处、团委与保卫处的老师列表
	 */
	public List<Student> getTeacherByMajor(int majorId) {
		List<Student> list = new ArrayList<Student>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select id, name from student where majorId=? and isTeacher=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, majorId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Student stu = new Student();
				stu.setId(rs.getInt("id"));
				stu.setName(rs.getString("name"));
				list.add(stu);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}
}
