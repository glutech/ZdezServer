package cn.com.zdez.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.util.NewSchoolMsgHelper;

public class School_NewMsg extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		// 获取网页传过来的信息
		String[] grade = request.getParameterValues("nianji[]");
		String[] department = request.getParameterValues("xueyuan[]");
		String[] major = request.getParameterValues("zhuanye[]");

		String[] teachers = request.getParameterValues("teacher[]");
		
		String[] stuAffairsTeachers = request.getParameterValues("xueshengchu[]");
		String[] empTeachers = request.getParameterValues("jiuyechu[]");
		String[] yccTeachers = request.getParameterValues("tuanwei[]");
		String[] sdTeachers = request.getParameterValues("baoweichu[]");

		String title = request.getParameter("schoolmessagetitle");
		String content = request.getParameter("schoolmessagecontent");

		// 解决reload时出错
		if (grade == null || department == null || major == null || title == null || content == null) {
			grade = (String[]) hs.getAttribute("grade");
			department = (String[]) hs.getAttribute("department");
			major = (String[]) hs.getAttribute("major");
			title = (String) hs.getAttribute("title");
			content = (String) hs.getAttribute("content");
			teachers = (String[]) hs.getAttribute("teachers");
			stuAffairsTeachers = (String[]) hs.getAttribute("stuAffairsTeachers");
			empTeachers = (String[]) hs.getAttribute("empTeachers");
			yccTeachers = (String[]) hs.getAttribute("yccTeachers");
			sdTeachers = (String[]) hs.getAttribute("sdTeachers");
		}
		hs.setAttribute("grade", grade);
		hs.setAttribute("department", department);
		hs.setAttribute("major", major);
		hs.setAttribute("title", title);
		hs.setAttribute("content", content);
		hs.setAttribute("teachers", teachers);
		hs.setAttribute("stuAffairsTeachers", stuAffairsTeachers);
		hs.setAttribute("empTeachers", empTeachers);
		hs.setAttribute("yccTeachers", yccTeachers);
		hs.setAttribute("sdTeachers", sdTeachers);
		// ---

		// send message here.
		SchoolAdmin sAdmin = (SchoolAdmin) hs.getAttribute("schoolAdmin");

		if (sAdmin == null) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {
			
			// 将所有抄送的对象的id存到teachers中
//			if (teachers != null) {
//				teachers = together(teachers, stuAffairsTeachers, empTeachers, yccTeachers, sdTeachers);
//			} else {
//				teachers = initialTeachers(stuAffairsTeachers, empTeachers, yccTeachers, sdTeachers);
//				teachers = together(teachers, stuAffairsTeachers, empTeachers, yccTeachers, sdTeachers);
//			}
			teachers = new NewSchoolMsgHelper().CCObjectsGenerator(teachers, stuAffairsTeachers, empTeachers, yccTeachers, sdTeachers);
			
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
			if (service.newSchoolMsg(sMsg, grade, department, major, teachers, sAdmin, (String)request.getSession().getAttribute("rootPath"))) {
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
