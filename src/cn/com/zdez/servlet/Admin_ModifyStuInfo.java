package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.Student;
import cn.com.zdez.service.StudentService;

public class Admin_ModifyStuInfo extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		int currentPage = 0;
		String keyword = "";
		int type = 0;

		currentPage = (Integer) session.getAttribute("currentPage");
		type = (Integer) session.getAttribute("type");

		//信息修改
		int stuId = Integer.parseInt(request.getParameter("stuId"));
		String name = (String) request.getParameter("name");
		name = new String(name.getBytes("ISO-8859-1"), "utf-8");
		String gender = (String) request.getParameter("gender");
		gender = new String(gender.getBytes("ISO-8859-1"), "utf-8");
		int gradeId = Integer.parseInt(request.getParameter("grade"));
		int majorId = Integer.parseInt(request.getParameter("major"));
		
		Student stu = new Student();
		stu.setId(stuId);
		stu.setName(name);
		stu.setGender(gender);
		stu.setGradeId(gradeId);
		stu.setMajorId(majorId);

		boolean flag = new StudentService().modifyStudentInfo(stu);
		if (type == 1 && flag) {
			request.getRequestDispatcher(
					"Admin_StudentManage?currentPage=" + currentPage).forward(
					request, response);
		} else if(type == 2 && flag){
			keyword = (String) session.getAttribute("keyword");
			request.getRequestDispatcher(
					"Admin_StudentManageQuery?currentPage=" + currentPage
							+ "&keyword=" + keyword).forward(request, response);
		}else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
