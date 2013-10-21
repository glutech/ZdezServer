package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.ZdezMsg;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.service.ZdezMsgService;

public class Admin_NewZdezMsg extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		// 获取网页传过来的信息
		String[] grade = request.getParameterValues("nianji[]");
		String[] major = request.getParameterValues("zhuanye[]");
		String[] gender = request.getParameterValues("gender[]");

		String title = request.getParameter("zdezmessagetitle");
		String content = request.getParameter("zdezmessagecontent");

		// 解决reload时出错
		if (grade == null || major == null || title == null || content == null || gender == null) {
			grade = (String[]) hs.getAttribute("grade");
			major = (String[]) hs.getAttribute("major");
			gender = (String[]) hs.getAttribute("gender");
			title = (String) hs.getAttribute("title");
			content = (String) hs.getAttribute("content");
		}
		hs.setAttribute("grade", grade);
		hs.setAttribute("major", major);
		hs.setAttribute("title", title);
		hs.setAttribute("content", content);
		hs.setAttribute("gender", gender);
		// ---

		StudentService sService = new StudentService();
		// 根据专业和年级获得通知接收对象列表
		List<Integer> destUsers = sService.getStudentByGradeMajor(grade, major, gender);

		// send message here.
		Boolean flag = (Boolean) hs.getAttribute("adminUserLoginSucessFlag");

		if (flag == null) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {

			title = new String(title.getBytes("ISO-8859-1"), "utf-8");
			content = new String(content.getBytes("ISO-8859-1"), "utf-8");

			ZdezMsg zMsg = new ZdezMsg();
			zMsg.setTitle(title);
			zMsg.setContent(content);
			zMsg.setAdminId("luo");

			ZdezMsgService service = new ZdezMsgService();
			// 发送信息
			if (service.newZdezMsg(zMsg, grade, major, destUsers, (String)request.getSession().getAttribute("rootPath"))) {
				// 发送成功
				request.getRequestDispatcher("admin_NewZdezMsgSuccess.jsp")
						.forward(request, response);
			} else {
				// 发送失败
				request.setAttribute("errorMsg", "没有适合已选发送条件的对象，请重新选择");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
