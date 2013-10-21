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
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.MajorService;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolService;

public class Admin_ModifySchoolAdminInfoHref extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String sAdminUsername= request.getParameter("sAdminUsername");
		SchoolAdminService s = new SchoolAdminService();
		SchoolAdmin sAdmin = s.getSchoolAdminInfo(sAdminUsername);

		List<School> schoolList = new SchoolService().getAll();
		List<Department> departmentList = new DepartmentService().getAll();
		List<Major> majorList = new MajorService().getAll();
		int schoolId = s.getSchoolIdByUsername(sAdminUsername);
		int departmentId = s.getDepartmentIdByUsername(sAdminUsername);
		int majorId = s.getMajorIdByUsername(sAdminUsername);

		request.setAttribute("schoolAdmin", sAdmin);
		request.getSession().setAttribute("schoolList", schoolList);
		request.getSession().setAttribute("departmentList", departmentList);
		request.getSession().setAttribute("majorList", majorList);
		request.setAttribute("schoolId", schoolId);
		request.setAttribute("departmentId", departmentId);
		request.setAttribute("majorId", majorId);
		request.getRequestDispatcher("admin_ModifySchoolAdminInfo.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
