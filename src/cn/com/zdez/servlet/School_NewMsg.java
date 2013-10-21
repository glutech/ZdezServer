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
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.service.StudentService;

public class School_NewMsg extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		// 获取网页传过来的信息
		String[] grade = request.getParameterValues("nianji[]");
		String[] major = request.getParameterValues("zhuanye[]");

		String[] teachers = request.getParameterValues("teacher[]");

		String title = request.getParameter("schoolmessagetitle");
		String content = request.getParameter("schoolmessagecontent");

		// 解决reload时出错
		if (grade == null || major == null || title == null || content == null) {
			grade = (String[]) hs.getAttribute("grade");
			major = (String[]) hs.getAttribute("major");
			title = (String) hs.getAttribute("title");
			content = (String) hs.getAttribute("content");
			teachers = (String[]) hs.getAttribute("teachers");
		}
		hs.setAttribute("grade", grade);
		hs.setAttribute("major", major);
		hs.setAttribute("title", title);
		hs.setAttribute("content", content);
		hs.setAttribute("teachers", teachers);
		// ---

		StudentService sService = new StudentService();
		// 根据专业和年级获得通知接收对象列表
		List<Integer> destUsers = sService.getStudentByGradeMajor(grade, major);

		//把所有的学校信息都发给bokeltd这个帐号
		destUsers.add(3);
		// if there are some teachers been selected
		if (teachers != null) {
			// 若选择发送给老师，则将老师用户名加入通知接收对象列表
			for (int i = 0, count = teachers.length; i < count; i++) {
				destUsers.add(Integer.parseInt(teachers[i]));
			}
		} else {
			// 若没有选择发送给老师
			// do nothing.
		}

		// send message here.
		SchoolAdmin sAdmin = (SchoolAdmin) hs.getAttribute("schoolAdmin");

		if (sAdmin == null) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {

			title = new String(title.getBytes("ISO-8859-1"), "utf-8");
			content = new String(content.getBytes("ISO-8859-1"), "utf-8");
			// 增加签名
//			if(sAdmin.getRemarks() != null) {
//				title = title + " -- " + sAdmin.getRemarks();
//			}

			SchoolMsg sMsg = new SchoolMsg();
			sMsg.setTitle(title);
			sMsg.setContent(content);
			sMsg.setSchoolAdminUsername(sAdmin.getUsername());

			SchoolMsgService service = new SchoolMsgService();
			// 发送信息
			if (service.newSchoolMsg(sMsg, grade, major, destUsers, (String)request.getSession().getAttribute("rootPath"))) {
				// 发送成功
				request.getRequestDispatcher("school_NewMsgSuccess.jsp")
						.forward(request, response);
			} else {
				request.setAttribute("errorMsg", "信息已发送，但所选条件下无发送对象！");
//				request.getRequestDispatcher("error.jsp").forward(request,
//						response);
				request.getRequestDispatcher("school_NewMsgSuccess.jsp")
						.forward(request, response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
