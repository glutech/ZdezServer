package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Department;
import cn.com.zdez.po.Grade;
import cn.com.zdez.po.Major;
import cn.com.zdez.po.School;
import cn.com.zdez.po.Student;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.GradeService;
import cn.com.zdez.service.MajorService;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolService;
import cn.com.zdez.service.StudentService;

public class Admin_ModifyStuInfoHref extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int stuId = Integer.parseInt(request.getParameter("stuId"));
		Student student = new StudentService().getStudentById(stuId);

		List<School> schoolList = new SchoolService().getAll();
		List<Department> departmentList = new DepartmentService().getAll();
		List<Major> majorList = new MajorService().getAll();
		List<Grade> gradeList = new SchoolAdminService().getGradeList();
		int schoolId = new SchoolService().getSchoolIdByStu(student);
		int departmentId = new SchoolService().getDepartmentIdByStu(student);

		request.setAttribute("student", student);
		request.getSession().setAttribute("schoolList", schoolList);
		request.getSession().setAttribute("departmentList", departmentList);
		request.getSession().setAttribute("majorList", majorList);
		request.getSession().setAttribute("gradeList", gradeList);
		request.setAttribute("schoolId", schoolId);
		request.setAttribute("departmentId", departmentId);
		request.getRequestDispatcher("admin_ModifyStuInfo.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
