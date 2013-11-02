package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.SchoolMsgVo;

public class School_SendedMsg extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();
		// 把从session中获取管理员信息改为从缓存中获取（因为西南林大权限失灵）
//		SchoolAdmin sAdmin = (SchoolAdmin) hs.getAttribute("schoolAdmin");
		SchoolAdmin sAdmin = new SchoolAdminService().getSchoolAdminInfo((String) hs.getAttribute("uname"));
		
//		System.out.println(sAdmin.getUsername());
//		System.out.println(sAdmin.getPassword());
//		System.out.println(sAdmin.getTelPhone());
//		System.out.println(sAdmin.getName());
//		System.out.println(sAdmin.getSchoolId());
//		System.out.println(sAdmin.getDepartmentId());
//		System.out.println(sAdmin.getMajorId());
//		System.out.println(sAdmin.getRemarks());
		
		//获取学校管理员本人已发送的校园通知
		SchoolMsgService service = new SchoolMsgService();
		int all = service.getSchoolMsgCount(sAdmin);	//一共多少条记录
		
		//分页
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));	//当前页码
		int allPage = 1;	//页数，默认1
		int countPerPage = 20;	//每页显示的条数
		if(all%countPerPage == 0) {
			//记录一共有20的整数倍
			allPage = all/countPerPage;
		}else {
			allPage = all/countPerPage +1;
		}
		
		List<SchoolMsgVo> sMsgToBeDisplayed = new ArrayList<SchoolMsgVo>();
		int start = (currentPage-1)*countPerPage;
		int end = 0;
		if(currentPage == allPage) {
			end = all-(currentPage-1)*countPerPage;
		}else {
			end = currentPage*countPerPage;
		}
		sMsgToBeDisplayed = service.getSchoolMsgByPage(start, end, sAdmin);
		
		request.setAttribute("allpage", allPage);
		request.setAttribute("all", all);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("schoolMsgList", sMsgToBeDisplayed);
		request.getRequestDispatcher("school_ViewMsg.jsp").forward(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
