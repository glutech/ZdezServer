package com.notnoop.mpns.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import cn.com.zdez.service.StudentService;
import cn.com.zdez.vo.StudentVo;

public class GetUserEntityJsonByUserName extends HttpServlet {

	/**
	 * @param username , uri
	 * @return the student entity of username in json type
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StudentService studentService = new StudentService();
		String userName = request.getParameter("userName");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Gson gson =new Gson();
		out.print(gson.toJson(studentService.getStudentVoByUsername(userName)));
		out.flush();
		out.close();
	}

	/**
	 * doGet();
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
