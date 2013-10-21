package com.notnoop.mpns.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Student;
import cn.com.zdez.service.LoginService;

public class StuUserLoginServlet extends HttpServlet {

	/**
	 * @param username,password
	 * @return true for the user is legal,false for illegal
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Student student = new Student();
		student.setUsername(username);
		student.setPassword(password);
		LoginService loginService = new LoginService();
		PrintWriter out = response.getWriter();
		out.print(loginService.studentLoginCheck(student));
		out.flush();
		out.close();
	}

	/**
	 * doGet
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
