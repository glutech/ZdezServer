package com.notnoop.mpns.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.SchoolMsgService;

public class EnsureReceiveMessage extends HttpServlet {

	/**
	 * @param messageID the messageID of the message
	 * @param user_id the user's id 
	 * @function ensure the message of messageID
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String messageID = request.getParameter("messageID");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String [] messageIDList = { messageID };
		SchoolMsgService schoolMsgService = new SchoolMsgService();
		schoolMsgService.updateSchoolMsgReceived(user_id,messageIDList);
		
	}

	/**
	 * doGet
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
