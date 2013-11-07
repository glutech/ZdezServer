package com.notnoop.mpns.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.StudentService;

public class Student_UpdateStuas extends HttpServlet {

	/**
	 * @param stuId as the id of WP Client user who want to update the channel
	 * @param staus as WP Client user's identify channel
	 * @function update the identify channel of the WP Client user
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String stuId = request.getParameter("user_id");
		String staus = request.getParameter("Uri");
		StudentService studentService = new StudentService();
//		studentService.modifyStaus(Integer.parseInt(stuId), staus);
	}

	/**
	 * doGet();
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
