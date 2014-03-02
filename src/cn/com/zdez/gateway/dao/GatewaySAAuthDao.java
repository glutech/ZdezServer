package cn.com.zdez.gateway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.dao.SQLExecution;
import cn.com.zdez.gateway.po.GatewaySAAuth;

public class GatewaySAAuthDao {

	private SQLExecution sqlE = new SQLExecution();

	private GatewaySAAuth parseRs(ResultSet rs) throws SQLException {
		GatewaySAAuth model = new GatewaySAAuth();
		model.setAuthId(rs.getInt("auth_id"));
		model.setSchoolAdminUsername(rs.getString("school_admin_username"));
		model.setAuthToken(rs.getString("auth_token"));
		model.setCreateIp(rs.getString("create_ip"));
		model.setCreateTime(rs.getTimestamp("create_time"));
		return model;
	}

	private GatewaySAAuth parseRsToModel(ResultSet rs) {
		try {
			return rs.next() ? parseRs(rs) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GatewaySAAuth getAuthByAuthId(int authId) {
		String sql = "select * from gateway_sa_auth_map where auth_id = ?";
		Object[] params = { authId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		return parseRsToModel(rs);
	}

	public GatewaySAAuth getAuthByAdminUsername(String username) {
		String sql = "select * from gateway_sa_auth_map where school_admin_username = ?";
		Object[] params = { username };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		return parseRsToModel(rs);
	}

	public GatewaySAAuth getAuthByAuthToken(String authToken) {
		String sql = "select * from gateway_sa_auth_map where auth_token = ?";
		Object[] params = { authToken };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		return parseRsToModel(rs);
	}

	public boolean insertAuth(GatewaySAAuth auth) {
		String sql = "insert into gateway_sa_auth_map(school_admin_username, auth_token, create_ip, create_time) values(?, ?, ?, ?)";
		Object[] params = { auth.getSchoolAdminUsername(), auth.getAuthToken(),
				auth.getCreateIp(), auth.getCreateTime() };
		return sqlE.execSqlWithoutRS(sql, params);
	}

	public int getLastInsertAuthId() {
		String sql = "select LAST_INSERT_ID()";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean deleteAuth(GatewaySAAuth auth) {
		String sql = "delete from gateway_sa_auth_map where auth_id = ?";
		Object[] params = { auth.getAuthId() };
		return sqlE.execSqlWithoutRS(sql, params);
	}
}
