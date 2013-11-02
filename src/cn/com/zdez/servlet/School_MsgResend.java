package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolMsgService;

public class School_MsgResend extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();
		// resend here

		String schoolMsgId = request.getParameter("schoolMsgId");

		// 解决reload时错误
		if (schoolMsgId == null) {
			schoolMsgId = (String) hs.getAttribute("schoolMsgId");
		}
		hs.setAttribute("schoolMsgId", schoolMsgId);

		int schoolMsgIdInt = Integer.parseInt(schoolMsgId);

		// get school message here
		SchoolMsgService sService = new SchoolMsgService();
		SchoolMsg sMsg = sService.getSchoolMsgById(schoolMsgIdInt);
		SchoolMsg sMsgNew = new SchoolMsg();
		sMsgNew.setTitle(sMsg.getTitle());
		sMsgNew.setContent(sMsg.getContent());
		sMsgNew.setSchoolAdminUsername(sMsg.getSchoolAdminUsername());

		List<Integer> gradeIdList = sService
				.getGradeIdListByMsgId(schoolMsgIdInt);
		List<Integer> departmentIdList = sService
				.getDepartmentIdListByMsgId(schoolMsgIdInt);
		List<Integer> majorIdList = sService
				.getMajorIdListByMsgId(schoolMsgIdInt);
		List<Integer> teachersList = sService.getDestTeachersByMsgId(Integer
				.parseInt(schoolMsgId));

		String[] grade = new String[gradeIdList.size()];
		String[] department = new String[departmentIdList.size()];
		String[] major = new String[majorIdList.size()];
		String[] teachers = new String[teachersList.size()];
		for (int i = 0, count = gradeIdList.size(); i < count; i++) {
			grade[i] = gradeIdList.get(i).toString();
		}
		for (int i = 0, count = departmentIdList.size(); i < count; i++) {
			department[i] = departmentIdList.get(i).toString();
		}
		for (int i = 0, count = majorIdList.size(); i < count; i++) {
			major[i] = majorIdList.get(i).toString();
		}
		for (int i = 0, count = teachersList.size(); i < count; i++) {
			teachers[i] = teachersList.get(i).toString();
		}

		SchoolAdmin sAdmin = new SchoolAdminService()
				.getSchoolAdminInfo((String) request.getSession().getAttribute(
						"uname"));

		// 发送信息
		if (sService.newSchoolMsg(sMsgNew, grade, department, major, teachers,
				sAdmin, (String) request.getSession().getAttribute("rootPath"))) {
			// 发送成功
			request.getRequestDispatcher("school_NewMsgSuccess.jsp").forward(
					request, response);
		} else {
			request.setAttribute("errorMsg", "信息已发送，但所选条件下无发送对象！");
			// request.getRequestDispatcher("error.jsp").forward(request,
			// response);
			request.getRequestDispatcher("school_NewMsgSuccess.jsp").forward(
					request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
