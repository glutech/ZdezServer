package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Student;
import cn.com.zdez.service.LoginService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.util.MD5;
import cn.com.zdez.vo.StudentVo;

import com.google.gson.Gson;

public class Ios_Client_NoNotificationAnyMore extends HttpServlet {

	public Ios_Client_NoNotificationAnyMore() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String staus = request.getParameter("staus");
		
		String result = "";
		//改造StudentService使之能够添加对应的设备号
		StudentService ss = new StudentService();
		Gson gson = new Gson();
		if (ss.modifyStaus(userName, staus)) {
			result = gson.toJson(true);
		} else {
			result = "fail";
		}

		PrintWriter out = response.getWriter();
		result = new String(result.getBytes("utf-8"), "iso-8859-1");
		out.append(result);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
