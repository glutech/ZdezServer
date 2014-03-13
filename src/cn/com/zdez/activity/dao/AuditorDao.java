package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.activity.po.Auditor;

public class AuditorDao extends BaseDao<Auditor> {

	@Override
	protected Auditor parseRS(ResultSet rs) throws SQLException {
		Auditor adt = new Auditor();
		adt.setAdtoId(rs.getInt("adto_id"));
		adt.setAdtoName(rs.getNString("adto_name"));
		return adt;
	}

	/**
	 * 通过用户ID获取审核人员信息
	 * 
	 */
	public Auditor getAuditorInfoById(int adtoId) {
		String sql = "select * from a_auditor where adto_id=?";
		Object[] params = { adtoId };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);
	}

	/**
	 * 获取审核人员数量
	 */
	public int getAuditorCount() {
		String sql = "SELECT COUNT(*) FROM a_auditor";
		Object[] params = {};
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseInt(rs);
	}

	/**
	 * 增加一条审核人员信息
	 */
	public boolean insertAuditor(Auditor adto) {
		String sql = "insert into a_auditor(adto_name) values (?)";
		Object[] params = { adto.getAdtoName() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 修改审核人员信息
	 */
	public boolean updateAuditor(Auditor adto) {
		String sql = "update a_auditor set adto_name =? where adto_id =?";
		Object[] params = { adto.getAdtoName() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 删除一条审核人员信息
	 */
	public boolean delAuditor(Auditor adto) {
		String sql = "delete from a_auditor where adto_id=?";
		Object[] params = { adto.getAdtoId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 通过外键关联查询
	 */
}
