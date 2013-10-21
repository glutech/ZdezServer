package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.School;
import cn.com.zdez.service.SchoolService;

public class Admin_NewNewsHref extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<School> schoolList = new ArrayList<School>();
		schoolList = new SchoolService().getAll();
		
		HttpSession hs = request.getSession();
		hs.setAttribute("schoolList", schoolList);
		request.getRequestDispatcher("admin_NewNews.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
