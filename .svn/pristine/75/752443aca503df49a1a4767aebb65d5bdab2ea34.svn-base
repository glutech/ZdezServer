package cn.com.zdez.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import cn.com.zdez.dao.ConnectionFactory;

public class SQLExecution {
	
	ConnectionFactory factory = ConnectionFactory.getInstatnce();
	Connection conn = null;
	
	/**
	 * 传入sql语句和需要的参数，执行delete、insert、update等无结果集返回的语句
	 * @param sql
	 * @param params
	 * @return true or false
	 */
	public boolean execSqlWithoutRS(String sql, Object[] params) {
		boolean flag = true;
		try {
			if (conn == null) {
				conn = factory.getConnection();
			}
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}

			pstmt.execute();
			factory.freeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;

	}

	/**
	 * 传入sql语句和需要的参数，执行select有结果集返回的语句
	 * @param sql
	 * @param params
	 * @return resultset
	 */
	public ResultSet execSqlWithRS(String sql, Object[] params) {
		ResultSet rs = null;

		try {
			if (conn == null) {
				conn = factory.getConnection();
			}
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			rs = pstmt.executeQuery();
			factory.freeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;

	}

}
