package com.notnoop.mpns.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.SchoolMsgVo;

public class ReLoginStudent extends HttpServlet {

	/**
	 * @param username , uri
	 * @return send the recently unreceived message to the phone
	 * @function decide whether there are unreceived messages of the WPphone in the service 
	 *            if yes, resend the message with the MPNS or push it to the WPphone by HTTP 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String user_id = request.getParameter("user_id");
		String uri = request.getParameter("uri");
		SchoolMsgService schoolMsgService = new SchoolMsgService();
		// uri for MPNS notification can't be null
		if(!(uri == null))
		{
			//schoolMsgVo sending
			ArrayList<SchoolMsgVo> result = (ArrayList<SchoolMsgVo>) schoolMsgService.getMsgToUpdate(Integer.valueOf(user_id));
			for(SchoolMsgVo schoolMsgVo:result)
			{
				schoolMsgService.sendMsgToWP(username, schoolMsgVo, uri);
		
			}
		}
			
		
	}

	/**
	 * do as the doGet
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
