package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.cache.SchoolMsgCache;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.SchoolMsgVo;

public class Admin_ViewSchoolMsgDetail extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int schoolMsgId = Integer.parseInt(request.getParameter("schoolMsgId"));
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(schoolMsgId);
//		List<SchoolMsgVo> list = new SchoolMsgService().getSchoolMsgAll(idList);
		List<SchoolMsgVo> list = new SchoolMsgCache().getSchoolMsgFromCache(idList);
		
		SchoolMsgVo sMsgVo = new SchoolMsgVo();
		for(SchoolMsgVo item : list) {
			sMsgVo = item;
		}
		
		request.setAttribute("schoolMsg", sMsgVo);
		request.getRequestDispatcher("admin_ViewSchoolMsgDetail.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}


}
