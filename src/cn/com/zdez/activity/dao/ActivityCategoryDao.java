package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zdez.activity.bo.ActInteractionBo;
import cn.com.zdez.activity.po.ActivityCategory;

public class ActivityCategoryDao extends BaseDao<ActivityCategory> {

	@Override
	protected ActivityCategory parseRS(ResultSet rs) throws SQLException {
		ActivityCategory po = new ActivityCategory();
		po.setCatgId(rs.getInt("catg_id"));
		po.setCatgName(rs.getString("catg_name"));
		return po;
	}

	public ActivityCategory getActCatgById(int catgId) {
		String sql = "select * from a_cat_category_terms where catg_id = ?";
		Object[] params = { catgId };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);
	}

	public List<ActivityCategory> getAllActCatgs() {
		String sql = "select * from a_cat_category_terms";
		Object[] params = {};
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToList(rs);
	}

	public ActInteractionBo getActInteractionBoByCatgId(int catgId) {
		String sql = "select count(ta.map_id) as jon_num, count(tb.cmt_id) as cmt_num "
				+ "from a_act_usr_map as ta, a_comments as tb, ( select actg_id from a_activities where actg_id = ? ) as tmp "
				+ "where ta.act_id = tmp.actg_id and (tb.cmt_parent_cmt_id_nullabled is null and tb.act_id = tmp.actg_id)";
		Object[] params = { catgId };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToT(rs, new ActInteractionBoParser());
	}

	public Map<Integer, ActInteractionBo> getAllActInteractionBos(
			List<ActivityCategory> catgs) {
		String sql = "select tmp.actg_id as catg_id, count(ta.map_id) as jonNum, count(tb.cmt_id) as cmtNum "
				+ "from a_act_usr_map as ta, a_comments as tb, ( select actg_id from a_activities ) as tmp "
				+ "where ta.act_id = tmp.actg_id and (tb.cmt_parent_cmt_id_nullabled is null and tb.act_id = tmp.actg_id)";
		Object[] params = {};
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		ActInteractionBoParser boParser = new ActInteractionBoParser();
		Map<Integer, ActInteractionBo> r = new HashMap<Integer, ActInteractionBo>();
		try {
			while (rs.next()) {
				int id = rs.getInt("catg_id");
				if (id == 0) {
					// 查询时，当不存在category时会返回[null,0,0]，需过滤
					return r;
				}
				r.put(id, boParser.parseRS(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return r;
	}

	private static class ActInteractionBoParser implements
			BaseDao.TParser<ActInteractionBo> {
		@Override
		public ActInteractionBo parseRS(ResultSet rs) throws SQLException {
			ActInteractionBo bo = new ActInteractionBo();
			bo.setJonNum(rs.getInt("jon_num"));
			bo.setCmtNum(rs.getInt("cmt_num"));
			return bo;
		}
	}
}
