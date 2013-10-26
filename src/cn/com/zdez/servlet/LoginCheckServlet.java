package cn.com.zdez.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.Admin;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.LoginService;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.util.MD5;

public class LoginCheckServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String errorMsg = "";
		try {
			if (request.getParameter("username") == null
					|| request.getParameter("password") == null
					|| request.getParameter("veryCode") == null
					|| "" == request.getParameter("veryCode")) {
				request.getRequestDispatcher("admin.jsp").forward(request,
						response);
			} else {
				// get validateCode
				String validateC = ((String) request.getSession().getAttribute(
						"validateCode")).toLowerCase();
				String veryCode = request.getParameter("veryCode")
						.toLowerCase();
				if (request.getParameter("username") == "") {
					errorMsg += "用户名不能为空！<br>";
				}
				if (request.getParameter("password") == "") {
					errorMsg += "密码不能为空！<br>";
				}
				if (veryCode == null || "".equals(veryCode)) {
					errorMsg += "验证码不能为空！<br>";
				} else {
					if (validateC.equals(veryCode)) {
					} else {
						request.setAttribute("uname",
								request.getParameter("username"));
						request.setAttribute("passwd",
								request.getParameter("password"));
						errorMsg += "验证码错误！<br>";
					}
				}

				if (errorMsg == "") {
					// 得到指定的参数
					String u = request.getParameter("username");
					String p = request.getParameter("password");
					p = new MD5().toMD5String(p);

					// 生成admin与schoolAdmin对象
					Admin admin = new Admin();
					SchoolAdmin schoolAdmin = new SchoolAdmin();
					admin.setUsername(u);
					admin.setPassword(p);
					schoolAdmin.setUsername(u);
					schoolAdmin.setPassword(p);

					// 比对是否为admin用户
					if (new LoginService().admin_loginCheck(admin)) {

						String keep = request.getParameter("keep");
						if (keep != null) {
							Cookie name = new Cookie("myname", u);
							Cookie passwd = new Cookie("mypasswd", p);
							name.setMaxAge(14 * 24 * 3600);
							passwd.setMaxAge(14 * 24 * 3600);
							response.addCookie(name);
							response.addCookie(passwd);
						}

						// 将用户名存入session中
						HttpSession hs = request.getSession(true);

						hs.setAttribute("uname", u);// used to display the
													// username in the pages
						// --- compulsory-login flag
						hs.setAttribute("adminUserLoginSucessFlag", true);

						// admin login success
						// 跳转到Admin_CacheStudentSchool是为了保持redis中用于统计的数据与数据库中一致，要不然会出错
						// request.getRequestDispatcher("Admin_CacheStudentSchool").forward(
						// request, response);

						request.getRequestDispatcher("admin.jsp").forward(
								request, response);
					} else if (new LoginService()
							.schoolAdmin_loginCheck(schoolAdmin)) {

						// getSchoolAdmin object by username
						SchoolAdmin sAdmin = new SchoolAdminService()
								.getSchoolAdminInfo(schoolAdmin.getUsername());

						// set up cookie
						String keep = request.getParameter("keep");
						if (keep != null) {

							Cookie name = new Cookie("myname", u);
							Cookie passwd = new Cookie("mypasswd", p);

							name.setMaxAge(14 * 24 * 3600);
							passwd.setMaxAge(14 * 24 * 3600);

							response.addCookie(name);
							response.addCookie(passwd);
						}

						HttpSession hs = request.getSession(true);

						hs.setAttribute("uname", u);// used to display the
													// username in the pages
						hs.setAttribute("schoolAdmin", sAdmin);
						// --- compulsory-login flag
						hs.setAttribute("schoolUserLoginSucessFlag", true);
						// ---

						request.getRequestDispatcher("school.jsp").forward(
								request, response);

					} else {
						request.setAttribute("message", "用户名或密码不正确");
						request.getRequestDispatcher("index.jsp").forward(
								request, response);
						;
					}
				} else {
					request.setAttribute("message", errorMsg);
					request.getRequestDispatcher("index.jsp").forward(request,
							response);
				}
			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
