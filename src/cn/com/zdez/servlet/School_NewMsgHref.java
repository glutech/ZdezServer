package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Department;
import cn.com.zdez.po.Grade;
import cn.com.zdez.po.Major;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.po.Student;
import cn.com.zdez.service.SchoolAdminService;

public class School_NewMsgHref extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("schoolAdmin") == null) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {
			SchoolAdminService service = new SchoolAdminService();
			List<SchoolSys> schoolSysList = new ArrayList<SchoolSys>();
			List<Grade> gradeList = new ArrayList<Grade>();
			List<Department> departmentList = new ArrayList<Department>();
			List<Major> majorList = new ArrayList<Major>();
			List<Student> teacherList = new ArrayList<Student>();
			List<Student> stuAffairsTeacherList = new ArrayList<Student>();
			List<Student> empTeacherList = new ArrayList<Student>();
			List<Student> yccTeacherList = new ArrayList<Student>();
			List<Student> sdTeacherList = new ArrayList<Student>();
			// 用于分组显示教职人员
//			HashMap<Integer, List<Student>> dptStu = new HashMap<Integer, List<Student>>();
			// SchoolAdmin sAdmin = (SchoolAdmin) request.getSession()
			// .getAttribute("schoolAdmin");
			// 把之前从session中获取管理员信息改为从缓存中获取，不知道对西南林大权限失灵的问题会不会有帮助...
			SchoolAdmin sAdmin = new SchoolAdminService()
					.getSchoolAdminInfo((String) request.getSession()
							.getAttribute("uname"));

			// 获取学业层次
			schoolSysList = service.getSchoolSysList(sAdmin);
			// 获取毕业年度
			gradeList = service.getGradeList();
			// 获取学院列表
			departmentList = service.getDepartmentList(sAdmin);
			// 获取专业列表
			majorList = service.getMajorList(sAdmin);
			// 获取教师列表
			teacherList = service.getTeacherList(sAdmin);
			// 获取部门id和老师列表的hashmap
//			dptStu = service.getDepartmentStudentByAuth(sAdmin);
			
			String idTag = "";
			
			// 获取学生处老师列表
			idTag = "stuAffairsId";
			stuAffairsTeacherList = service.getDptTeacherList(sAdmin, idTag);
			
			// 获取就业处老师列表
			idTag = "employmentId";
			empTeacherList = service.getDptTeacherList(sAdmin, idTag);
			
			// 获取团委老师列表
			idTag = "youthCorpsCommitteeId";
			yccTeacherList = service.getDptTeacherList(sAdmin, idTag);
			
			// 获取保卫处老师列表
			idTag = "securityDeptId";
			sdTeacherList = service.getDptTeacherList(sAdmin, idTag);

			request.getSession().setAttribute("schoolSysList", schoolSysList);
			request.getSession().setAttribute("gradeList", gradeList);
			request.getSession().setAttribute("departmentList", departmentList);
			request.getSession().setAttribute("majorList", majorList);
//			request.getSession().setAttribute("teacherList", teacherList);
			request.getSession().setAttribute("stuAffairsTeacherList", stuAffairsTeacherList);
			request.getSession().setAttribute("empTeacherList", empTeacherList);
			request.getSession().setAttribute("yccTeacherList", yccTeacherList);
			request.getSession().setAttribute("sdTeacherList", sdTeacherList);
//			request.getSession().setAttribute("dptStu", dptStu);

			request.getRequestDispatcher("school_NewMsg.jsp").forward(request,
					response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
