package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.activity.po.Activity;

public class ActivityDao extends BaseDao<Activity> {

	@Override
	protected Activity parseRS(ResultSet rs) throws SQLException {
		Activity act = new Activity();
		act.setActId(rs.getInt("act_id"));
		act.setCatgId(rs.getInt("catg_id"));
		act.setTmId(rs.getInt("tm_id"));
		act.setActAddr(rs.getString("act_add"));
		act.setActInfo(rs.getString("act_info"));
		act.setActName(rs.getString("act_name"));
		act.setActState(Activity.ActStateEnum.valueOf(rs
				.getString("act_state_enum")));
		act.setActType(Activity.ActTypeEnum.valueOf(rs
				.getString("act_type_enum")));
		return act;
	}

	/**
	 * 新增活动
	 */
	public boolean insertActivity(Activity act) {
		String sql = "insert into a_actvites(catg_id, tm_id, act_add, act_info, act_name, act_state_enum, act_type_enum) "
				+ "values (?,?,?,?,?,?,?)";
		Object[] params = { act.getCatgId(), act.getTmId(), act.getActAddr(),
				act.getActInfo(), act.getActState().toString(),
				act.getActType().toString() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 删除活动
	 */
	public boolean deleteActivity(Activity act) {
		String sql = "delete from a_activities where act_id = ?";
		Object[] params = { act.getActId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新活动
	 */
	public boolean updateActivity(Activity act) {
		String sql = "update a_activities set catg_id=?, tm_id=?, act_add=?, act_info=?, "
				+ "act_name=?, act_state_enum=?, act_type_enum=?";
		Object[] params = { act.getCatgId(), act.getTmId(), act.getActAddr(),
				act.getActInfo(), act.getActState().toString(),
				act.getActType().toString() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新活动状态
	 */
	public boolean modifyActivityState(Activity act) {
		String sql = "update a_activities set act_state_enum = ? where act_id = ?";
		Object[] params = { act.getActType().toString(), act.getActId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新活动类型
	 */
	public boolean modifyActivityType(Activity act) {
		String sql = "update a_activities set act_type_enum = ? where act_id=?";
		Object[] params = { act.getActType().toString(), act.getActId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 通过活动ID查询活动详细信息
	 */
	public Activity getActivity(int actId) {
		String sql = "select * from a_activities where act_id = ?";
		Object[] params = { actId };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);
	}

}