package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.service.StudentService;

public class Admin_DeleteStudent extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int stuId = Integer.parseInt(request.getParameter("stuId"));
		HttpSession hs = request.getSession();
		StudentService service = new StudentService();
		if(service.deleteStudent(stuId)) {
			request.getRequestDispatcher("admin_StudentDeleteSuccess.jsp").forward(request, response);
		}else {
			System.out.println("error in delete student");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
