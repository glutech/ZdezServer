package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.activity.po.Notice;

public class NoticeDao extends BaseDao<Notice> {

	@Override
	protected Notice parseRS(ResultSet rs) throws SQLException {
		Notice po = new Notice();
		po.setNoteId(rs.getInt("note_id"));
		po.setNoteTime(rs.getDate("note_time"));
		po.setNoteContent(rs.getString("note_content"));
		po.setNoteState(Notice.NoteStateEnum.valueOf(rs
				.getString("note_state_enum")));
		po.setActId(rs.getInt("act_id"));
		return po;
	}

	/**
	 * 通过ID号查找通知
	 */
	public Notice getNoticeById(int noticeId) {
		String sql = "select * from a_notices where note_id = ?";
		Object[] params = { noticeId };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);
	}

	/**
	 * 新增通知
	 */
	public boolean insertNotice(Notice note) {
		String sql = "insert into a_notices "
				+ "(note_time, note_content, note_state_enum, act_id) "
				+ "values(?, ?, ?, ?, ?)";
		Object[] params = { note.getNoteTime(), note.getNoteContent(),
				note.getNoteState(), note.getActId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 删除通知
	 */
	public boolean delNoticeByNoteId(Notice note) {
		String sql = "delete from notices where note_id=?";
		int noteId = note.getNoteId();
		Object[] params = { noteId };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新通知
	 */
	public boolean modifyNotice(Notice note) {
		String sql = "update a_notices set note_time=?, note_content=?, note_state_enum=?, act_id=? where note_id=?";
		Object[] params = { note.getNoteTime(), note.getNoteContent(),
				note.getNoteState(), note.getActId(), note.getNoteId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新通知状态
	 */
	public boolean modifyNoticeState(Notice note) {
		String sql = "update a_notice set note_state_enum = ? where note_id= ?";
		Object[] params = { note.getNoteState(), note.getNoteId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 通过外键查找
	 */

}
