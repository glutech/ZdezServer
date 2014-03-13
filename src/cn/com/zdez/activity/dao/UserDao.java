package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.activity.po.User;

public class UserDao extends BaseDao<User> {
	@Override
	protected User parseRS(ResultSet rs) throws SQLException {
		User usr = new User();
		usr.setUsrId(rs.getInt("usr_id"));
		usr.setUsrNickname(rs.getString("usr_nickname"));
		usr.setUsrPhone(rs.getString("usr_phone"));
		usr.setUsrSignature(rs.getString("usr_signature"));
		usr.setUsrAvatar(rs.getString("usr_avatar"));
		usr.setStuId(rs.getInt("stu_id"));
		usr.setUsrAbleStateEnum(User.UsrAbleStateEnum.valueOf(rs
				.getString("usr_able_state_enum")));
		usr.setUsrTypeEnum(User.UsrTypeEnum.valueOf(rs
				.getString("usr_type_enum")));
		return usr;
	}

	/**
	 * 根据用户ID获取用户信息
	 */
	public User getUserInfoById(int usrId) {
		String sql = "select * from a_users where usr_id = ?";
		Object[] params = { usrId };
		ResultSet rSet = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rSet);
	}

	/**
	 * 根据用户昵称查看用户信息
	 */
	public User getUserInfoByNickname(String nickname) {
		String sql = "select * from a_users where usr_nickname=?";
		Object[] params = { nickname };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);

	}

	/**
	 * 根据学生ID获取用户信息
	 */
	public User getUserInfoByStuId(int stuId) {
		String sql = "select * from a_users where stu_id=?";
		Object[] params = { stuId };
		ResultSet rs = getSqlExecution().execSqlWithRS(sql, params);
		return parseRsToPo(rs);
	}

	/**
	 * 新增用户
	 */
	public boolean insertUser(User usr) {
		String sql = "insert a_user into (usr_nickname, usr_phone, usr_signature, usr_avatar, stu_id,"
				+ " usr_able_state_enum, usr_type_enum )";
		Object[] params = { usr.getUsrNickname(), usr.getUsrPhone(),
				usr.getUsrSignature(), usr.getUsrAvatar(), usr.getStuId(),
				usr.getUsrAbleStateEnum(), usr.getUsrTypeEnum() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 删除用户信息
	 */
	public boolean deleteUser(User usr) {
		String sql = "delete from a_users where usr_id = ?";
		Object[] params = { usr.getUsrId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新用户信息
	 */
	public boolean modifyUser(User usr) {
		String sql = "update a_users set usr_nickname=?, usr_phone?, usr_signature=?, usr_avatar=?, stu_id=?,"
				+ " usr_able_state_enum=?, usr_type_enum=?_";
		Object[] params = { usr.getUsrNickname(), usr.getUsrPhone(),
				usr.getUsrSignature(), usr.getUsrAvatar(), usr.getStuId(),
				usr.getUsrAbleStateEnum(), usr.getUsrTypeEnum() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新用户状态
	 */
	public boolean modifyUserAbleState(User usr) {
		String sql = "update a_users set usr_able_state_enum = ? where usr_id=?";
		Object[] params = { usr.getUsrId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}

	/**
	 * 更新用户类型
	 */
	public boolean modifyUserType(User usr) {
		String sql = "update a_users set usr_type_enum = ? where usr_id = ?";
		Object[] params = { usr.getUsrId() };
		return getSqlExecution().execSqlWithoutRS(sql, params);
	}
}
