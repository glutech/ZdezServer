package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.SchoolAdminVo;
import cn.com.zdez.vo.SchoolMsgVo;

public class Admin_SchoolAdminManage extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//管理员查看学校发送的所有通知
		SchoolAdminService service = new SchoolAdminService();
		int all = service.getSchoolAdminCount();	//一共多少条记录
		
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
		
		List<SchoolAdminVo> sAdminToBeDisplayed = new ArrayList<SchoolAdminVo>();
		int start = (currentPage-1)*countPerPage;
		int end = 0;
		if(currentPage == allPage) {
			end = all-(currentPage-1)*countPerPage;
		}else {
			end = currentPage*countPerPage;
		}
		sAdminToBeDisplayed = service.getSchoolAdminByPage(start, end);
		
		
		request.setAttribute("allpage", allPage);
		request.setAttribute("all", all);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("schoolAdminList", sAdminToBeDisplayed);
		request.getRequestDispatcher("admin_SchoolAdminManage.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
