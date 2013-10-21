package cn.com.zdez.dao;

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

}
