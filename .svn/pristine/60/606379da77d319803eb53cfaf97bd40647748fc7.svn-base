package cn.com.zdez.service;

import cn.com.zdez.dao.AdminDao;
import cn.com.zdez.po.Admin;

public class AdminService {

	AdminDao dao = new AdminDao();

	public boolean newAdmin(Admin admin) {
		return dao.newAdmin(admin);
	}

	/**
	 * 将数据库中的明文密码转为md5加密格式 基本不会用到 除非数据库中所有密码均为明文
	 * 
	 * @return
	 */
	public boolean toMD5() {
		boolean flag = false;
		if (dao.setAdminPwdtoMD5() && dao.setSchoolAdminPwdtoMD5()
				&& dao.setStuPwdtoMD5()) {
			flag = true;
		}
		return flag;
	}

}
