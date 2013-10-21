package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.ZdezMsg;
import cn.com.zdez.service.ZdezMsgService;

public class Admin_ZdezMsgResend extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();
		// resend here

		String zdezMsgId = request.getParameter("zdezMsgId");
		
		//解决reload时错误
		if(zdezMsgId == null) {
			zdezMsgId = (String)hs.getAttribute("zdezMsgId");
		}
		hs.setAttribute("zdezMsgId", zdezMsgId);
		
		int zdezMsgIdInt = Integer.parseInt(zdezMsgId);

		// get school message here
		ZdezMsgService service  = new ZdezMsgService();
		ZdezMsg zMsg = service.getZdezMsgById(zdezMsgIdInt);
		ZdezMsg zMsgNew = new ZdezMsg();
		zMsgNew.setTitle(zMsg.getTitle());
		zMsgNew.setContent(zMsg.getContent());
		
		List<Integer> gradeIdList = service.getGradeIdListByMsgId(zdezMsgIdInt);
		List<Integer> majorIdList = service.getMajorIdListByMsgId(zdezMsgIdInt);
		
		String[] grade = new String[gradeIdList.size()];
		String[] major = new String[majorIdList.size()];
		for(int i=0, count=gradeIdList.size(); i<count; i++) {
			grade[i] = gradeIdList.get(i).toString();
		}
		for(int i=0, count=majorIdList.size(); i<count; i++) {
			major[i] = majorIdList.get(i).toString();
		}
		
		List<Integer> destUsers = service.getDestUsersListByMsgId(zdezMsgIdInt);
		
		// 发送信息
		if (service.newZdezMsg(zMsgNew, grade, major, destUsers, (String) request.getSession().getAttribute("rootPath"))) {
			// 发送成功
			request.getRequestDispatcher("admin_NewZdezMsgSuccess.jsp").forward(
					request, response);
		} else {
			request.setAttribute("errorMsg", "信息已发送，但所选条件下无发送对象！");
//			request.getRequestDispatcher("error.jsp").forward(request,
//					response);
			request.getRequestDispatcher("admin_NewZdezMsgSuccess.jsp")
					.forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
