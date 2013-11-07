package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.po.Grade;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;
import cn.com.zdez.util.AuthConvert;
import cn.com.zdez.vo.SchoolAdminVo;

public class SchoolAdminDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 通过用户名获取用户详细信息
	 * 
	 * @param username
	 * @return schoolAdmin entity
	 */
	public SchoolAdmin getSchoolAdminInfo(String username) {
		SchoolAdmin schoolAdmin = new SchoolAdmin();
		String sql = "select * from schoolAdmin where username=?";
		Object[] params = { username };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				schoolAdmin.setUsername(rs.getString("username"));
				schoolAdmin.setPassword(rs.getString("password"));
				schoolAdmin.setTelPhone(rs.getString("telPhone"));
				schoolAdmin.setName(rs.getString("name"));
				schoolAdmin.setAuth(rs.getInt("auth"));
				schoolAdmin.setSchoolId(rs.getInt("schoolId"));
				schoolAdmin.setDepartmentId(rs.getInt("departmentId"));
				schoolAdmin.setMajorId(rs.getInt("majorId"));
				schoolAdmin.setRemarks(rs.getString("remarks"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return schoolAdmin;
	}

	/**
	 * 学校管理员用户修改密码
	 * 
	 * @param sAdmin
	 * @return true or false
	 */
	public boolean modifyPassword(SchoolAdmin sAdmin) {

		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();

		boolean flag = false;
		String sql = "update schoolAdmin set password=? where username=?";
		Object[] params = { sAdmin.getPassword(), sAdmin.getUsername() };
		flag = sqlE.execSqlWithoutRS(sql, params);

		// 将修改后的密码写入redis
		try {

			jedis.hset("schoolAdmin:" + sAdmin.getUsername(), "password",
					sAdmin.getPassword());

		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
		return flag;
	}

	/**
	 * 根据学校管理员账户的权限等具体信息获得对应的教师列表 以便学校管理员在发送校园通知的时候可以选择是否发送给这些老师
	 * 
	 * @param sAdmin
	 * @return
	 */
	public List<Student> getTeacherList(SchoolAdmin sAdmin) {
		List<Student> list = new ArrayList<Student>();
		int auth = sAdmin.getAuth();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = factory.getConnection();
			if (auth == 1) {
				// 当学校管理员帐号拥有1级权限（学校级权限）时，可以选择发送校园通知给全校老师
				// 获取该学校的所有老师所在的学院
				String sqlDepartment = "select * from department where schoolId = ?";
				pstmt = conn.prepareStatement(sqlDepartment);
				pstmt.setInt(1, sAdmin.getSchoolId());
				ResultSet rsDepartment = pstmt.executeQuery();

				while (rsDepartment.next()) {
					// 获取该部门的所有老师所在的子部门（专业）
					String sqlMajor = "select * from major where departmentId = ?";
					pstmt = conn.prepareStatement(sqlMajor);
					pstmt.setInt(1, rsDepartment.getInt("id"));
					ResultSet rsMajor = pstmt.executeQuery();

					while (rsMajor.next()) {
						// 获取某一子部门（专业）下所有的老师信息
						String sqlTeacher = "select * from student where isTeacher = 1 and majorId = ?";
						pstmt = conn.prepareStatement(sqlTeacher);
						pstmt.setInt(1, rsMajor.getInt("id"));
						ResultSet rsTeacher = pstmt.executeQuery();
						while (rsTeacher.next()) {
							Student stu = new Student();
							stu.setId(rsTeacher.getInt("id"));
							stu.setUsername(rsTeacher.getString("username"));
							stu.setPassword(rsTeacher.getString("password"));
							stu.setName(rsTeacher.getString("name"));
							stu.setGender(rsTeacher.getString("gender"));
							stu.setAdmissionTime(rsTeacher
									.getTime("admissionTime"));
							stu.setStatus(rsTeacher.getInt("status"));
							stu.setStaus(rsTeacher.getString("staus"));
							stu.setGradeId(rsTeacher.getInt("gradeId"));
							stu.setMajorId(rsTeacher.getInt("majorId"));
							list.add(stu);
						}
					}
				}
				pstmt.close();
			} else if (auth == 2) {
				// 当学校管理员拥有2级权限（学院级权限）时，可以选择发送校园通知给本部门（学院）老师
				String sqlMajor = "select * from major where departmentId = ?";
				pstmt = conn.prepareStatement(sqlMajor);
				pstmt.setInt(1, sAdmin.getDepartmentId());
				ResultSet rsMajor = pstmt.executeQuery();
				while (rsMajor.next()) {
					// 获取某一子部门（专业）下所有的老师信息
					String sqlTeacher = "select * from student where isTeacher = 1 and majorId = ?";
					pstmt = conn.prepareStatement(sqlTeacher);
					pstmt.setInt(1, rsMajor.getInt("id"));
					ResultSet rsTeacher = pstmt.executeQuery();
					while (rsTeacher.next()) {
						Student stu = new Student();
						stu.setId(rsTeacher.getInt("id"));
						stu.setUsername(rsTeacher.getString("username"));
						stu.setPassword(rsTeacher.getString("password"));
						stu.setName(rsTeacher.getString("name"));
						stu.setGender(rsTeacher.getString("gender"));
						stu.setAdmissionTime(rsTeacher.getTime("admissionTime"));
						stu.setStatus(rsTeacher.getInt("status"));
						stu.setStaus(rsTeacher.getString("staus"));
						stu.setGradeId(rsTeacher.getInt("gradeId"));
						stu.setMajorId(rsTeacher.getInt("majorId"));
						list.add(stu);
					}
				}
				pstmt.close();
			} else if (auth == 3) {
				// 当学校管理员拥有3级权限（专业级权限）时，可以选择发送校园通知给本专业老师
				// 获取某一子部门（专业）下所有的老师信息
				String sqlTeacher = "select * from student where isTeacher = 1 and majorId = ?";
				pstmt = conn.prepareStatement(sqlTeacher);
				pstmt.setInt(1, sAdmin.getMajorId());
				ResultSet rsTeacher = pstmt.executeQuery();
				while (rsTeacher.next()) {
					Student stu = new Student();
					stu.setId(rsTeacher.getInt("id"));
					stu.setUsername(rsTeacher.getString("username"));
					stu.setPassword(rsTeacher.getString("password"));
					stu.setName(rsTeacher.getString("name"));
					stu.setGender(rsTeacher.getString("gender"));
					stu.setAdmissionTime(rsTeacher.getTime("admissionTime"));
					stu.setStatus(rsTeacher.getInt("status"));
					stu.setStaus(rsTeacher.getString("staus"));
					stu.setGradeId(rsTeacher.getInt("gradeId"));
					stu.setMajorId(rsTeacher.getInt("majorId"));
					list.add(stu);
				}
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 获得数据库中学校管理员的数量
	 * 
	 * @return
	 */
	public int getSchoolAdminCount() {
		int i = -1;
		String sql = "select count(username) from schoolAdmin";
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
	 * 查询时用，获取某一个关键字下对应的学校帐号的数量，用于分页
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSchoolAdminCount(String keyword) {
		int i = -1;
		String sql = "select count(id) from schoolAdmin where name like '%"
				+ keyword + "%' ";
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
	 * 根据页码获取对应的管理员信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SchoolAdminVo> getSchoolAdminByPage(int start, int end) {
		AuthConvert authConvert = new AuthConvert();
		List<SchoolAdminVo> list = new ArrayList<SchoolAdminVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select sa.username, sa.name, sa.auth, sa.departmentId, sa.majorId, s.name as school "
					+ "from schoolAdmin as sa, school as s where sa.schoolId = s.id limit ?,?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SchoolAdminVo sAdminVo = new SchoolAdminVo();
				sAdminVo.setUsername(rs.getString("username"));
				sAdminVo.setName(rs.getString("name"));
				sAdminVo.setSchool(rs.getString("school"));
				if (rs.getInt("auth") == 1) {
					sAdminVo.setAuth(authConvert.authConvert(1));
					sAdminVo.setDepartment("");
					sAdminVo.setMajor("");
				} else if (rs.getInt("auth") == 2) {
					sAdminVo.setAuth(authConvert.authConvert(2));
					String getDepartment = "select name from department where id = ?";
					pstmt = conn.prepareStatement(getDepartment);
					pstmt.setInt(1, rs.getInt("departmentId"));
					ResultSet rsDepartment = pstmt.executeQuery();
					while (rsDepartment.next()) {
						sAdminVo.setDepartment(rsDepartment.getString("name"));
					}
					sAdminVo.setMajor("");
				} else if (rs.getInt("auth") == 3) {
					sAdminVo.setAuth(authConvert.authConvert(3));
					String getDepartment = "select name from department where id = ?";
					pstmt = conn.prepareStatement(getDepartment);
					pstmt.setInt(1, rs.getInt("departmentId"));
					ResultSet rsDepartment = pstmt.executeQuery();
					while (rsDepartment.next()) {
						sAdminVo.setDepartment(rsDepartment.getString("name"));
					}
					String getMajor = "select name from major where id = ?";
					pstmt = conn.prepareStatement(getMajor);
					pstmt.setInt(1, rs.getInt("majorId"));
					ResultSet rsMajor = pstmt.executeQuery();
					while (rsMajor.next()) {
						sAdminVo.setMajor(rsMajor.getString("name"));
					}
				}
				list.add(sAdminVo);
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
	 * 按照分页和关键字查询要显示的学校管理员账户，用于搜索
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<SchoolAdminVo> getSchoolAdminByPage(int start, int end,
			String keyword) {
		AuthConvert authConvert = new AuthConvert();
		List<SchoolAdminVo> list = new ArrayList<SchoolAdminVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select sa.username, sa.name, sa.auth, sa.departmentId, sa.majorId, s.name as school "
					+ "from schoolAdmin as sa, school as s where sa.schoolId = s.id and sa.name like '%"
					+ keyword + "%' limit ?,?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SchoolAdminVo sAdminVo = new SchoolAdminVo();
				sAdminVo.setUsername(rs.getString("username"));
				sAdminVo.setName(rs.getString("name"));
				sAdminVo.setSchool(rs.getString("school"));
				if (rs.getInt("auth") == 1) {
					sAdminVo.setAuth(authConvert.authConvert(1));
					sAdminVo.setDepartment("");
					sAdminVo.setMajor("");
				} else if (rs.getInt("auth") == 2) {
					sAdminVo.setAuth(authConvert.authConvert(2));
					String getDepartment = "select name from department where id = ?";
					pstmt = conn.prepareStatement(getDepartment);
					pstmt.setInt(1, rs.getInt("departmentId"));
					ResultSet rsDepartment = pstmt.executeQuery();
					while (rsDepartment.next()) {
						sAdminVo.setDepartment(rsDepartment.getString("name"));
					}
					sAdminVo.setMajor("");
				} else if (rs.getInt("auth") == 3) {
					sAdminVo.setAuth(authConvert.authConvert(3));
					String getDepartment = "select name from department where id = ?";
					pstmt = conn.prepareStatement(getDepartment);
					pstmt.setInt(1, rs.getInt("departmentId"));
					ResultSet rsDepartment = pstmt.executeQuery();
					while (rsDepartment.next()) {
						sAdminVo.setDepartment(rsDepartment.getString("name"));
					}
					String getMajor = "select name from major where id = ?";
					pstmt = conn.prepareStatement(getMajor);
					pstmt.setInt(1, rs.getInt("majorId"));
					ResultSet rsMajor = pstmt.executeQuery();
					while (rsMajor.next()) {
						sAdminVo.setMajor(rsMajor.getString("name"));
					}
				}
				list.add(sAdminVo);
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
	 * 删除学校管理员帐号
	 * 
	 * @param username
	 * @return
	 */
	public boolean deleteSchoolAdmin(String username) {
		boolean flag = false;
		String sql = "delete from schoolAdmin where username = ?";
		Object[] params = { username };
		if (sqlE.execSqlWithoutRS(sql, params)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 根据学业层次id获取学业层次相关信息
	 * 
	 * @param schoolSysIdList
	 * @return
	 */
	public List<SchoolSys> getSchoolSysList(List<Integer> schoolSysIdList) {
		List<SchoolSys> list = new ArrayList<SchoolSys>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			for (int i = 0, count = schoolSysIdList.size(); i < count; i++) {
				int schoolSysId = schoolSysIdList.get(i);
				String sql = "select sysName from schoolSys where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, schoolSysId);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					SchoolSys sSys = new SchoolSys();
					sSys.setId(schoolSysId);
					sSys.setSysName(rs.getString("sysName"));
					list.add(sSys);
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
	 * 根据当前年份，获取毕业年度 返回当前年份再加之后4年
	 * 
	 * @return
	 */
	public List<Grade> getGradeList() {
		List<Grade> list = new ArrayList<Grade>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String year = df.format(new java.util.Date());
		SimpleDateFormat df1 = new SimpleDateFormat("MM");
		String month = df1.format(new java.util.Date());

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			int currentId = 0;

			// 获取当前年级对应的id
			String sql = "select id from grade where description = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, year);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				currentId = rs.getInt("id");
			}

			int i = 0;
			int count = 0;
			int tempMonth = Integer.parseInt(month);
			// 如果在9月份之前（包括9月份），则可向今年的毕业生发信息
			if (tempMonth <= 9) {
				i = currentId;
				count = currentId + 6;
			} else {
				// 如果在9月份之后，则不可再给今年的毕业生发信息
				i = currentId + 1;
				count = currentId + 7;
			}

			for (; i < count; i++) {
				sql = "select * from grade where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, i);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Grade g = new Grade();
					g.setId(rs.getInt("id"));
					g.setDescription(rs.getString("description"));
					list.add(g);
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
	 * 通过用户名获取所属学校的id
	 * 
	 * @param username
	 * @return
	 */
	public int getSchoolIdByUsername(String username) {
		int i = 0;
		String sql = "select schoolId from schoolAdmin where username = ?";
		Object[] params = { username };
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
	 * 通过用户名获取所属学院的id
	 * 
	 * @param username
	 * @return
	 */
	public int getDepartmentIdByUsername(String username) {
		int i = 0;
		String sql = "select departmentId from schoolAdmin where username = ?";
		Object[] params = { username };
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
	 * 通过用户名获取所属专业的id
	 * 
	 * @param username
	 * @return
	 */
	public int getMajorIdByUsername(String username) {
		int i = 0;
		String sql = "select majorId from schoolAdmin where username = ?";
		Object[] params = { username };
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

	public boolean newSchoolAdmin(SchoolAdmin sAdmin) {
		boolean flag = false;
		String sql = "";
		if (sAdmin.getAuth() == 3) {
			sql = "insert into schoolAdmin (username, password, telPhone, name, auth, schoolId, departmentId, majorId, remarks) values (?,?,?,?,?,?,?,?,?)";
			Object[] params = { sAdmin.getUsername(), sAdmin.getPassword(),
					sAdmin.getTelPhone(), sAdmin.getName(), sAdmin.getAuth(),
					sAdmin.getSchoolId(), sAdmin.getDepartmentId(),
					sAdmin.getMajorId(), sAdmin.getRemarks() };
			flag = sqlE.execSqlWithoutRS(sql, params);
		} else if (sAdmin.getAuth() == 2) {
			sql = "insert into schoolAdmin (username, password, telPhone, name, auth, schoolId, departmentId, remarks) values (?,?,?,?,?,?,?,?)";
			Object[] params = { sAdmin.getUsername(), sAdmin.getPassword(),
					sAdmin.getTelPhone(), sAdmin.getName(), sAdmin.getAuth(),
					sAdmin.getSchoolId(), sAdmin.getDepartmentId(),
					sAdmin.getRemarks() };
			flag = sqlE.execSqlWithoutRS(sql, params);
		} else {
			sql = "insert into schoolAdmin (username, password, telPhone, name, auth, schoolId, remarks) values (?,?,?,?,?,?,?)";
			Object[] params = { sAdmin.getUsername(), sAdmin.getPassword(),
					sAdmin.getTelPhone(), sAdmin.getName(), sAdmin.getAuth(),
					sAdmin.getSchoolId(), sAdmin.getRemarks() };
			flag = sqlE.execSqlWithoutRS(sql, params);
		}
		return flag;
	}

	/**
	 * 获取所有学校管理员信息，用于首次缓存
	 * 
	 * @return
	 */
	public List<SchoolAdmin> getAll() {
		List<SchoolAdmin> list = new ArrayList<SchoolAdmin>();

		String sql = "select * from schoolAdmin";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {

				SchoolAdmin s = new SchoolAdmin();
				s.setUsername(rs.getString("username"));
				s.setPassword(rs.getString("password"));
				s.setTelPhone(rs.getString("telPhone"));
				s.setName(rs.getString("name"));
				s.setAuth(rs.getInt("auth"));
				s.setSchoolId(rs.getInt("schoolId"));
				s.setDepartmentId(rs.getInt("departmentId"));
				s.setMajorId(rs.getInt("majorId"));
				s.setRemarks(rs.getString("remarks"));

				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public HashMap<Integer, List<Student>> getDepartmentStudent(SchoolAdmin sAdmin) {
		HashMap<Integer, List<Student>> department = new HashMap<Integer, List<Student>>();
		int auth = sAdmin.getAuth();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = factory.getConnection();

			if (auth == 1) {
				// 当学校管理员帐号拥有1级权限（学校级权限）时，可以选择发送校园通知给全校老师
				// 获取该学校的所有老师所在的学院
				String sqlDepartment = "select id from department where isTeacherUsed = 1 and schoolId = ?";
				pstmt = conn.prepareStatement(sqlDepartment);
				pstmt.setInt(1, sAdmin.getSchoolId());
				ResultSet rsDepartment = pstmt.executeQuery();

				while (rsDepartment.next()) {
					List<Student> list = new ArrayList<Student>();
					// 获取该部门的所有老师所在的子部门（专业）
					String sqlMajor = "select id from major where departmentId = ?";
					pstmt = conn.prepareStatement(sqlMajor);
					pstmt.setInt(1, rsDepartment.getInt("id"));
					ResultSet rsMajor = pstmt.executeQuery();

					while (rsMajor.next()) {
						// 获取某一子部门（专业）下所有的老师信息
						String sqlTeacher = "select * from student where isTeacher = 1 and majorId = ?";
						pstmt = conn.prepareStatement(sqlTeacher);
						pstmt.setInt(1, rsMajor.getInt("id"));
						ResultSet rsTeacher = pstmt.executeQuery();
						while (rsTeacher.next()) {
							Student stu = new Student();
							stu.setId(rsTeacher.getInt("id"));
							stu.setUsername(rsTeacher.getString("username"));
							stu.setName(rsTeacher.getString("name"));
							list.add(stu);
						}

					}
					department.put(rsDepartment.getInt("id"), list);
				}
				pstmt.close();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return department;
	}
}