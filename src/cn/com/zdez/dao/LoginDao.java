package cn.com.zdez.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.zdez.po.Admin;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.Student;

public class LoginDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 验证公司管理员用户
	 * 
	 * @param admin
	 * @return true or false
	 */
	public boolean admin_loginCheck(Admin admin) {
		boolean flag = false;
		String sql = "select username from admin where username=? and password=?";
		Object[] params = { admin.getUsername(), admin.getPassword() };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 验证学校管理员用户
	 * 
	 * @param schoolAdmin
	 * @return true or false
	 */
	public boolean schoolAdmin_loginCheck(SchoolAdmin schoolAdmin) {
		boolean flag = false;
		String sql = "select username from schoolAdmin where username=? and password=?";
		Object[] params = { schoolAdmin.getUsername(),
				schoolAdmin.getPassword() };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 验证客户端学生用户登陆
	 * @param student
	 * @return true or false
	 */
	public boolean studentLoginCheck(Student student) {
		boolean flag = false;
		String sql = "select id from student where username=? and password=?";
		Object[] params = { student.getUsername(), student.getPassword() };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}
}
