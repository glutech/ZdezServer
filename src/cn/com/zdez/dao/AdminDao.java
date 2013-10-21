package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.po.Admin;
import cn.com.zdez.util.MD5;

public class AdminDao {

	private SQLExecution sqlE = new SQLExecution();

	public boolean newAdmin(Admin admin) {
		boolean flag = false;
		String sql = "insert into admin (username, password) values (?,?)";
		Object[] params = { admin.getUsername(), admin.getPassword() };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 修改student表中密码为md5加密形式
	 * 
	 * @return
	 */
	public boolean setStuPwdtoMD5() {
		boolean flag = false;
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select username, password from student";
		String sql1 = "update student set password = ? where username = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String pswMD5 = new MD5().toMD5String(rs.getString("password"));
				pstmt1.setString(1, pswMD5);
				pstmt1.setString(2, rs.getString("username"));
				if (pstmt1.executeUpdate() > 0) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
			pstmt.close();
			pstmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return flag;
	}

	/**
	 * 修改admin表中密码为md5加密形式
	 * 
	 * @return
	 */
	public boolean setAdminPwdtoMD5() {
		boolean flag = false;
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select username, password from admin";
		String sql1 = "update admin set password = ? where username = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String pswMD5 = new MD5().toMD5String(rs.getString("password"));
				pstmt1.setString(1, pswMD5);
				pstmt1.setString(2, rs.getString("username"));
				if (pstmt1.executeUpdate() > 0) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
			pstmt.close();
			pstmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return flag;
	}

	/**
	 * 修改schoolAdmin表中密码为md5加密形式
	 * 
	 * @return
	 */
	public boolean setSchoolAdminPwdtoMD5() {
		boolean flag = false;
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select username, password from schoolAdmin";
		String sql1 = "update schoolAdmin set password = ? where username = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String pswMD5 = new MD5().toMD5String(rs.getString("password"));
				pstmt1.setString(1, pswMD5);
				pstmt1.setString(2, rs.getString("username"));
				if (pstmt1.executeUpdate() > 0) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
			pstmt.close();
			pstmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return flag;
	}

}
