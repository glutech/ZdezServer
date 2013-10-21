package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Department;
import cn.com.zdez.po.Major;
import cn.com.zdez.po.School;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.MajorService;
import cn.com.zdez.service.SchoolService;

public class Admin_NewSchoolAdminHref extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Admin_NewSchoolAdminHref() {
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

		List<School> schoolList = new SchoolService().getAll();
		List<Department> departmentList = new DepartmentService().getAll();
		List<Major> majorList = new MajorService().getAll();
		
		request.getSession().setAttribute("schoolList", schoolList);
		request.getSession().setAttribute("departmentList", departmentList);
		request.getSession().setAttribute("majorList", majorList);
		
		request.getSession().setAttribute("isSendZdezMsg", 0);
		
		request.getRequestDispatcher("admin_NewSchoolAdmin.jsp").forward(request, response);
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
