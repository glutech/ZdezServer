package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Department;
import cn.com.zdez.po.Grade;
import cn.com.zdez.po.Major;
import cn.com.zdez.po.School;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.GradeService;
import cn.com.zdez.service.MajorService;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolService;

public class Admin_NewZdezMsgHref extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("adminUserLoginSucessFlag") == null) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {
			List<School> schoolList = new ArrayList<School>();
			List<SchoolSys> schoolSysList = new ArrayList<SchoolSys>();
			List<Grade> gradeList = new ArrayList<Grade>();
			List<Department> departmentList = new ArrayList<Department>();
			List<Major> majorList = new ArrayList<Major>();

			// 获取学校
			schoolList = new SchoolService().getAll();
			// 获取学业层次
			schoolSysList = new SchoolService().getSchoolSysAll();
			// 获取毕业年度
			gradeList = new SchoolAdminService().getGradeList();
			// 获取学院列表
			departmentList = new DepartmentService().getAll();
			// 获取专业列表
			majorList = new MajorService().getAll();

			request.getSession().setAttribute("schoolList", schoolList);
			request.getSession().setAttribute("schoolSysList", schoolSysList);
			request.getSession().setAttribute("gradeList", gradeList);
			request.getSession().setAttribute("departmentList", departmentList);
			request.getSession().setAttribute("majorList", majorList);
			
			request.getSession().setAttribute("isSendZdezMsg", 1);

			request.getRequestDispatcher("admin_NewZdezMsg.jsp").forward(request,
					response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
