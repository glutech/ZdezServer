package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.ZdezMsgService;
import cn.com.zdez.vo.ZdezMsgVo;

public class Admin_ViewZdezMsgDetail extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int zdezMsgId= Integer.parseInt(request.getParameter("zdezMsgId"));
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(zdezMsgId);
		List<ZdezMsgVo> list = new ZdezMsgService().getZdezMsgDetails(idList);
		
		ZdezMsgVo zdezMsgVo = new ZdezMsgVo();
		for(ZdezMsgVo item : list) {
			zdezMsgVo = item;
		}
		
		request.setAttribute("zdezMsg", zdezMsgVo);
		request.getRequestDispatcher("admin_ViewZdezMsgDetail.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}


}
