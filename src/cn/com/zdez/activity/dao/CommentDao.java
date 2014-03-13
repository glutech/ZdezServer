package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.activity.po.Comment;

public class CommentDao extends BaseDao<Comment> {

	@Override
	protected Comment parseRS(ResultSet rs) throws SQLException {
		Comment cmt = new Comment();
		cmt.setActId(rs.getInt("cmt_id"));
		cmt.setActId(rs.getInt("act_id"));
		cmt.setUsrId(rs.getInt("usr_id"));
		cmt.setCmtContent(rs.getString("cmt_content"));
		cmt.setCmtTime(rs.getTimestamp("cmt_time"));
		cmt.setCmtTypeEnum(Comment.CmtTypeEnum.valueOf(rs
				.getString("cmt_state_enum")));
		cmt.setCmtParentIdNullabled(getIntegerFromRS(rs,
				"cmt_parent_id_nullabled"));
		return cmt;
	}

	/**
	 * 新增评论
	 */
	public Comment insertComment(Comment cmt) {
		String sql = "insert into a_comments (act_id, usr_id, cmt_content, cmt_parent_id_nullabled, "
				+ "cmt_time, cmt_state_enum)";
		Object[] params = { cmt.getActId(), cmt.getUsrId(),
				cmt.getCmtContent(), cmt.getCmtParentIdNullabled(),
				cmt.getCmtTime(), cmt.getCmtTypeEnum().toString() };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);
	}

	/**
	 * 删除评论
	 */
	public boolean deleteComment(Comment cmt) {
		String sql = "delete from a_comments where cmt_id=?";
		Object[] params = { cmt.getCmtId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新评论
	 */
	public boolean modifyComment(Comment cmt) {
		String sql = "update a_comments set act_id=?, usr_id=?, cmt_content=?, cmt_parent_id_nullabled, "
				+ "cmt_time=?, cmt_state_enum=?";
		Object[] params = { cmt.getActId(), cmt.getUsrId(),
				cmt.getCmtContent(), cmt.getCmtParentIdNullabled(),
				cmt.getCmtTime(), cmt.getCmtTypeEnum() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新评论状态
	 */
	public boolean modifyCommentState(Comment cmt) {
		String sql = "update a_comment set cmt_state_enum=? where cmt_id=?";
		Object[] params = { cmt.getCmtTypeEnum().toString(),cmt.getCmtId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);

		/**
		 * 根据外键查询
		 */
	}
}
