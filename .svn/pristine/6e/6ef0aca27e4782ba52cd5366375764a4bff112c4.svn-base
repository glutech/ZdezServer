package cn.com.zdez.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.util.MD5;

public class School_AdminPSWModify extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SchoolAdmin schoolAdmin = new SchoolAdmin();
		HttpSession hs = request.getSession();

		//从session中获取用户对象
		schoolAdmin = (SchoolAdmin)hs.getAttribute("schoolAdmin");
		
		//获取网页中输入的内容
		String psw = request.getParameter("password");
		String newPsw = request.getParameter("newPassword");
		String confirmNewPsw = request.getParameter("confirmNewPassword");
		
		String errorMsg = "";
		
		if (schoolAdmin.getUsername() == null || schoolAdmin.getPassword() == null) {
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else if(psw == null || newPsw == null || confirmNewPsw ==null) {
			request.getRequestDispatcher("School_AdminPasswdModify.jsp").forward(request, response);
		}else {
			if ("".equals(psw) || psw == null) {
				errorMsg += "原密码不能为空！<br>";
			}
			if ("".equals(newPsw) || newPsw == null) {
				errorMsg += "新密码不能为空！<br>";
			}
			if (!schoolAdmin.getPassword().equals(new MD5().toMD5String(psw))) {
				errorMsg+="原密码不正确！<br>";
			}
			if(!newPsw.equals(confirmNewPsw)){
				errorMsg+="两次密码输入不一致!<br>";
			}

			if (errorMsg == "") {
				schoolAdmin.setPassword(new MD5().toMD5String(confirmNewPsw));
				boolean flag = false;
				flag = new SchoolAdminService().modifyPassword(schoolAdmin);
				if(flag) {
					errorMsg += "修改成功！<br>";
					request.setAttribute("message", errorMsg);
					request.getRequestDispatcher("school_AdminPasswdModify.jsp").forward(request,
							response);
				}else {
					request.setAttribute("message", "后台操作有误，请重试。");
					request.getRequestDispatcher("school_AdminPasswdModify.jsp").forward(request,
							response);
				}
				
			} else {
				request.setAttribute("message", errorMsg);
				request.getRequestDispatcher("school_AdminPasswdModify.jsp").forward(request,
						response);
			}
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
