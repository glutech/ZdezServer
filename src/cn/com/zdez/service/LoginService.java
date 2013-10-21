package cn.com.zdez.service;

import cn.com.zdez.dao.LoginDao;
import cn.com.zdez.po.Admin;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.Student;

public class LoginService {
	LoginDao dao = new LoginDao();

	/**
	 * 公司管理员账户登录验证
	 * 
	 * @param admin
	 * @return
	 */
	public boolean admin_loginCheck(Admin admin) {
		return dao.admin_loginCheck(admin);
	}

	/**
	 * 学校管理员账户登录验证
	 * 
	 * @param schoolAdmin
	 * @return
	 */
	public boolean schoolAdmin_loginCheck(SchoolAdmin schoolAdmin) {
		return dao.schoolAdmin_loginCheck(schoolAdmin);
	}

	/**
	 * 学生用户账户登录验证
	 * 
	 * @param student
	 * @return
	 */
	public boolean studentLoginCheck(Student student) {
		return dao.studentLoginCheck(student);
	}

}
