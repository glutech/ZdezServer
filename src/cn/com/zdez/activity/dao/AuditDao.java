package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import cn.com.zdez.activity.po.Audit;

public class AuditDao extends BaseDao<Audit> {
	@Override
	protected Audit parseRS(ResultSet rs) throws SQLException {
		Audit adt = new Audit();
		adt.setAdtId(rs.getInt("adt_id"));
		adt.setAdtoId(rs.getInt("adto_id"));
		adt.setAdtContent(rs.getString("adt_content"));
		adt.setAdtTime(rs.getDate("adt_time"));
		adt.setCmtId(rs.getInt("cmt_id"));
		adt.setNoteId(rs.getInt("note_id"));
		adt.setUsrId(rs.getInt("usr_id"));
		adt.setAdtState(Audit.AdtStateEnum.valueOf(rs
				.getString("adt_state_enum")));
		return adt;
	}

	/**
	 * 通过审核号获取审核信息
	 */
	public Audit getAuditInfo(int adtId) {
		String sql = "select * from a_audit where adt_id=?";
		Object[] params = { adtId };
		return parseRsToPo(getSqlExecution().execSqlWithRS(sql, params));
	}

	/**
	 * 增加审核信息
	 */
	public boolean insertAudit(Audit adt) {
		String sql = "insert into a_audit (adt_time, adt_state_enum, adt_content, adto_id, note_id, cmt_id, usr_id) values"
				+ " (?,?,?,?,?,?,?)";
		Object[] params = { adt.getAdtTime(), adt.getAdtState(),
				adt.getAdtContent(), adt.getAdtoId(), adt.getNoteId(),
				adt.getCmtId(), adt.getUsrId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 删除审核信息
	 */
	public boolean deleteAudit(Audit adt) {
		String sql = "delete from a_audit where adt_id=?";
		Object[] params = { adt.getAdtId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新审核信息
	 */
	public boolean modifyAudit(Audit adt) {
		String sql = "update a_audit set adt_time=?, adt_state_enum=?, adt_conten=?, adto_id=?, note_id=?, cmt_id=? , usr_id=? where adt_id =?";
		Object[] params = { adt.getAdtTime(), adt.getAdtState(),
				adt.getAdtContent(), adt.getAdtoId(), adt.getAdtoId(),
				adt.getCmtId(), adt.getUsrId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新审核状态
	 */
	public boolean modifyAuditState(Audit adt) {
		String sql = "update a_audit set adt_state_enum=? where adt_id = ?";
		Object[] params = { adt.getAdtState(), adt.getAdtId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}
}
