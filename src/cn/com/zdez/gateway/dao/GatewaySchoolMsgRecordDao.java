package cn.com.zdez.gateway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.dao.SQLExecution;
import cn.com.zdez.gateway.po.GatewaySchoolMsgRecord;

public class GatewaySchoolMsgRecordDao {

	private SQLExecution sqlE = new SQLExecution();

	private GatewaySchoolMsgRecord parseRs(ResultSet rs) throws SQLException {
		GatewaySchoolMsgRecord model = new GatewaySchoolMsgRecord();
		model.setRecordId(rs.getInt("record_id"));
		model.setAuthIdNullabled(rs.getInt("auth_id_nullabled"));
		if (model.getAuthIdNullabled() == 0)
			model.setAuthIdNullabled(null);
		model.setSchoolMsgId(rs.getInt("school_msg_id"));
		model.setPostIp(rs.getString("post_ip"));
		model.setPostTime(rs.getTimestamp("post_time"));
		return model;
	}

	private GatewaySchoolMsgRecord parseRsToModel(ResultSet rs) {
		try {
			return rs.next() ? parseRs(rs) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GatewaySchoolMsgRecord getRecordByRecordId(int recordId) {
		String sql = "select * from gateway_school_msg_records where record_id = ?";
		Object[] params = { recordId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		return parseRsToModel(rs);
	}

	public boolean insertRecord(GatewaySchoolMsgRecord record) {
		String sql = "insert into gateway_school_msg_records(auth_id_nullabled, school_msg_id, post_ip	, post_time) values(?, ?, ?, ?)";
		Object[] params = { record.getAuthIdNullabled(),
				record.getSchoolMsgId(), record.getPostIp(),
				record.getPostTime() };
		return sqlE.execSqlWithoutRS(sql, params);
	}

	public boolean deleteRecord(GatewaySchoolMsgRecord record) {
		String sql = "delete from gateway_school_msg_records where record_id = ?";
		Object[] params = { record.getRecordId() };
		return sqlE.execSqlWithoutRS(sql, params);
	}
}
