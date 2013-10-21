package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.StudentService;

public class Admin_DeleteSchoolAdmin extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String sAdminUsername= request.getParameter("sAdminUsername");
		HttpSession hs = request.getSession();
		SchoolAdminService service = new SchoolAdminService();
		if(service.deleteSchoolAdmin(sAdminUsername)) {
			request.getRequestDispatcher("admin_SchoolAdminDeleteSuccess.jsp").forward(request, response);
		}else {
			System.out.println("error in delete school admin");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
