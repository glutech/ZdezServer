package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.Department;

public class DepartmentDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 获取某一学校的所有学院
	 * 
	 * @param schoolId
	 * @return list of departments
	 */
	public List<Department> getDepartmentsList(int schoolId) {
		List<Department> list = new ArrayList<Department>();
		String sql = "select id, name from department where schoolId=? and isTeacherUsed=0";
		Object[] params = { schoolId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				Department departObject = new Department();
				departObject.setId(rs.getInt("id"));
				departObject.setName(rs.getString("name"));
				departObject.setSchoolId(schoolId);
				list.add(departObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据学院Id获取该学院的详细信息
	 * 
	 * @param departmentId
	 * @return department entity
	 */
	public Department getDepartmentById(int departmentId) {
		Department depart = new Department();
		String sql = "select name, schoolId from department where id=?";
		Object[] params = { departmentId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				depart.setId(departmentId);
				depart.setName(rs.getString("name"));
				depart.setSchoolId(rs.getInt("schoolId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return depart;
	}

	public int getSchoolIdByDepartmentId(int departmentId) {
		int schoolId = 0;
		String sql = "select schoolId from department where id=?";
		Object[] params = { departmentId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				schoolId = rs.getInt("schoolId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return schoolId;
	}

	public List<Department> getAll() {
		List<Department> list = new ArrayList<Department>();
		String sql = "select * from department";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				Department d = new Department();
				d.setId(rs.getInt("id"));
				d.setName(rs.getString("name"));
				d.setSchoolId(rs.getInt("schoolId"));
				list.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<String> getNameById(String[] department) {
		List<String> list = new ArrayList<String>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select name from department where id = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i=0, count = department.length; i<count; i++) {
				pstmt.setInt(1, Integer.parseInt(department[i]));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString(1));
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
