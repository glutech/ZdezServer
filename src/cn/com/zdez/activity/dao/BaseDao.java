package cn.com.zdez.activity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import cn.com.zdez.dao.SQLExecution;

/**
 * dao包的统一基类 <br />
 * 提供了基础的读取逻辑封装
 * 
 * @param <T>
 *            具体的dao类所对应的po层实体
 * @author wu.kui2@gmail.com
 */
public abstract class BaseDao<T> {

	private SQLExecution sqlE = new SQLExecution();

	protected SQLExecution getSqlExecution() {
		return sqlE;
	}

	protected abstract T parseRS(ResultSet rs) throws SQLException;

	protected T parseRsToModel(ResultSet rs) {
		try {
			if (rs.next()) {
				return parseRS(rs);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected List<T> parseRsToList(ResultSet rs) {
		List<T> result = new LinkedList<T>();
		try {
			while (rs.next()) {
				result.add(parseRS(rs));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected int parseInt(ResultSet rs) {
		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected String parseString(ResultSet rs) {
		try {
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected int getLastInsertId() {
		String sql = "select LAST_INSERT_ID()";
		Object[] params = {};
		return parseInt(getSqlExecution().execSqlWithRS(sql, params));
	}
}
