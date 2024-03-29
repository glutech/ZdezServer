package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.Major;

public class MajorDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 获取某一学院包含的所有专业
	 * 
	 * @param departmentId
	 * @return list of majors
	 */
	public List<Major> getMajorList(int departmentId) {
		List<Major> list = new ArrayList<Major>();
		String sql = "select * from major where departmentId=? and isTeacherUsed=0";
		Object[] params = { departmentId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				Major major = new Major();
				major.setId(rs.getInt("id"));
				major.setName(rs.getString("name"));
				major.setDepartmentId(rs.getInt("departmentId"));
				major.setSchoolSysId(rs.getInt("schoolSysId"));
				list.add(major);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过专业的Id获取该专业的详细信息
	 * 
	 * @param majorId
	 * @return major entity
	 */
	public Major getMajorById(int majorId) {
		Major major = new Major();
		String sql = "select * from major where id=?";
		Object[] params = { majorId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				major.setId(rs.getInt("id"));
				major.setName(rs.getString("name"));
				major.setDepartmentId(rs.getInt("departmentId"));
				major.setSchoolSysId(rs.getInt("schoolSysId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return major;
	}

	/**
	 * 获取某一专业所属的学院id
	 * 
	 * @param majorId
	 * @return
	 */
	public int getDepartmentIdByMajorId(int majorId) {
		int departmentId = 0;
		String sql = "select departmentId from major where id=?";
		Object[] params = { majorId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				departmentId = rs.getInt("departmentId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return departmentId;
	}

	/**
	 * 获取所有专业
	 * 
	 * @return
	 */
	public List<Major> getAll() {
		List<Major> list = new ArrayList<Major>();
		String sql = "select * from major";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				Major m = new Major();
				m.setId(rs.getInt("id"));
				m.setName(rs.getString("name"));
				m.setSchoolSysId(rs.getInt("schoolSysId"));
				m.setDepartmentId(rs.getInt("departmentId"));
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<String> getNameById(String[] major) {
		List<String> list = new ArrayList<String>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select name from major where id = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i=0, count  = major.length; i <count; i++) {
				pstmt.setInt(1, Integer.parseInt(major[i]));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
