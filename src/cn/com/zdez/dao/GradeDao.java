package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.Grade;

public class GradeDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 通过年级id获取年级详细信息
	 * 
	 * @param gradeId
	 * @return
	 */
	public Grade getGradeById(int gradeId) {
		Grade grade = new Grade();
		String sql = "select * from grade where id=?";
		Object[] params = { gradeId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				grade.setId(rs.getInt("id"));
				grade.setDescription(rs.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return grade;
	}

	/**
	 * 获取所有年级
	 * 
	 * @return list of grade entity
	 */
	public List<Grade> getAll() {
		List<Grade> list = new ArrayList<Grade>();
		String sql = "select * from grade";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				Grade g = new Grade();
				g.setId(rs.getInt("id"));
				g.setDescription(rs.getString("description"));
				list.add(g);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<String> getDescriptionById(String[] grade) {
		List<String> list = new ArrayList<String>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select description from grade where id = ?";
		
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i=0, count = grade.length; i<count; i++) {
				pstmt.setInt(1, Integer.parseInt(grade[i]));
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
