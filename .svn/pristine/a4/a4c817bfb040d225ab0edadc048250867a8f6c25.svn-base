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

public class Admin_NewSchoolAdmin extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Admin_NewSchoolAdmin() {
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
		String nameOrigin = request.getParameter("name");
		String name = new String(nameOrigin.getBytes("ISO-8859-1"), "utf-8");
		String telPhone = request.getParameter("telPhone");
		String auth = request.getParameter("auth");
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String remarks = request.getParameter("remarks");

		if (username == null) {
			username = (String) request.getAttribute("username");
			psw = (String) request.getAttribute("psw");
			name = (String) request.getAttribute("nameOrigin");
			telPhone = (String) request.getAttribute("telPhone");
			auth = (String) request.getAttribute("auth");
			school = (String) request.getAttribute("school");
			department = (String) request.getAttribute("department");
			major = (String) request.getAttribute("major");
			remarks = (String) request.getAttribute("remarks");
		}
		hs.setAttribute("username", username);
		hs.setAttribute("psw", psw);
		hs.setAttribute("name", nameOrigin);
		hs.setAttribute("telPhone", telPhone);
		hs.setAttribute("auth", auth);
		hs.setAttribute("school", school);
		hs.setAttribute("department", department);
		hs.setAttribute("major", major);
		hs.setAttribute("remarks", remarks);

		SchoolAdmin sAdmin = new SchoolAdmin();
		sAdmin.setUsername(username);
		sAdmin.setPassword(new MD5().toMD5String(psw));
		sAdmin.setName(name);
		sAdmin.setTelPhone(telPhone);
		sAdmin.setAuth(Integer.parseInt(auth));
		sAdmin.setSchoolId(Integer.parseInt(school));
		
		if(auth.equals("2")) {
			sAdmin.setDepartmentId(Integer.parseInt(department));
		}else if (auth.equals("3"))  {
			sAdmin.setDepartmentId(Integer.parseInt(department));
			sAdmin.setMajorId(Integer.parseInt(major));
		}
		sAdmin.setRemarks(remarks);
		
		if (new SchoolAdminService().newSchoolAdmin(sAdmin)) {
			request.setAttribute("errorMsg", "新增用户成功");
		} else {
			request.setAttribute("errorMsg", "新增用户失败");
		}

		request.getRequestDispatcher("admin_NewSchoolAdminResult.jsp").forward(
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
