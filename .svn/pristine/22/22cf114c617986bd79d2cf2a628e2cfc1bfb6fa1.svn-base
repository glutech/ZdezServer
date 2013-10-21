package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.dao.AdminDao;
import cn.com.zdez.po.Admin;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.util.MD5;

public class Admin_NewAdmin extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Admin_NewAdmin() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		String username = request.getParameter("username");
		String psw = request.getParameter("psw");

		if (username == null) {
			username = (String) request.getAttribute("username");
			psw = (String) request.getAttribute("psw");
		}
		hs.setAttribute("username", username);
		hs.setAttribute("psw", psw);
		
		Admin admin = new Admin();
		admin.setUsername(username);
		admin.setPassword(new MD5().toMD5String(psw));
		
		if (new AdminDao().newAdmin(admin)) {
			request.setAttribute("errorMsg", "新增用户成功");
		} else {
			request.setAttribute("errorMsg", "新增用户失败");
		}

		request.getRequestDispatcher("admin_NewAdminResult.jsp").forward(
				request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
