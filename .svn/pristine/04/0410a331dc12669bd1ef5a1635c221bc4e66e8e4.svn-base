package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.School;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;

public class SchoolDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 通过学校id获取学校名
	 * 
	 * @param schoolId
	 * @return
	 */
	public String getSchoolNameById(int schoolId) {
		String schoolName = "";
		String sql = "select * from school where id=?";
		Object[] params = { schoolId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				schoolName = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return schoolName;
	}

	/**
	 * 通过学校id列表批量获取学校名 为了解决在需要批量获取学校名时的频繁的数据库连接
	 * 
	 * @param schoolIdList
	 * @return
	 */
	public List<String> getSchoolNameById(List<Integer> schoolIdList) {
		List<String> list = new ArrayList<String>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			String sql = "select * from school where id=?";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0, count = schoolIdList.size(); i < count; i++) {
				pstmt.setInt(1, schoolIdList.get(i));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString("name"));
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
	 * 获取所有学校实体
	 * 
	 * @return
	 */
	public List<School> getAll() {
		List<School> list = new ArrayList<School>();
		String sql = "select * from school";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				School s = new School();
				s.setId(rs.getInt("id"));
				s.setName(rs.getString("name"));
				s.setAddress(rs.getString("address"));
				list.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据学生实体获取该学生所属的学校id
	 * 
	 * @param stu
	 * @return
	 */
	public int getSchoolIdByStu(Student stu) {
		int i = 0;
		String sql = "select schoolId from department where id = any(select departmentId from major "
				+ "where id = any(select majorId from student where id = ?))";
		Object[] params = { stu.getId() };
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
	 * 通过学生实体获取该学生所属的学院id
	 * 
	 * @param stu
	 * @return
	 */
	public int getDepartmentIdByStu(Student stu) {
		int i = 0;
		String sql = "select departmentId from major where id = any(select majorId from student where id = ?)";
		Object[] params = { stu.getId() };
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
	 * 获取所有的学业层次
	 * 
	 * @return
	 */
	public List<SchoolSys> getSchoolSysAll() {
		List<SchoolSys> list = new ArrayList<SchoolSys>();
		String sql = "select * from schoolSys";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				SchoolSys ss = new SchoolSys();
				ss.setId(rs.getInt("id"));
				ss.setSysName(rs.getString("sysName"));
				list.add(ss);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过学校id的集合获取这些学校包含的学业层次，用于ZdezMsg的发送。 由于不同的学校可能包含同一个学业层次，所有要注意返回结果中的去重操作
	 * 
	 * @param schoolIds
	 * @return
	 */
	public List<SchoolSys> getSchoolSysListBySchoolIds(String[] schoolIds) {
		List<SchoolSys> list = new ArrayList<SchoolSys>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			String sql = "select * from schoolSys where id = any (select schoolSysId from major "
					+ "where departmentId = any (select id from department where schoolId = ?))";
			pstmt = conn.prepareStatement(sql);
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0, count = schoolIds.length; i < count; i++) {
				pstmt.setInt(1, Integer.parseInt(schoolIds[i]));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					SchoolSys ss = new SchoolSys();
					ss.setId(rs.getInt("id"));
					ss.setSysName(rs.getString("sysName"));

					// 进行结果中的去重操作
					if (list.size() > 0) {
						if (!idList.contains(ss.getId())) {
							list.add(ss);
							idList.add(ss.getId());
						}
					} else {
						list.add(ss);
						idList.add(ss.getId());
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

}
