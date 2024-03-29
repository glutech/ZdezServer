package com.notnoop.mpns.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.SchoolMsgService;

public class Client_getOneMessageContext extends HttpServlet {

	/**
	 * @param messgeID of the message
	 * @param type as which table the message is in
	 * @function get the message's hall information 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String messageID = request.getParameter("messageID");
		String type = request.getParameter("type");
//		System.out.println("messageID : " + messageID);
//		System.out.println("type : " + type);
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.print("<content>");
		
		if(type.equals("free"))
		{
			//schoolMsg message sending HTML
			SchoolMsgService schoolMsgService = new SchoolMsgService();
			out.print(schoolMsgService.getSchoolMsgContentById(Integer.parseInt(messageID)));
		}
		else 
		{
			// news and ZDEZ html message sending 
			out.print("");
		}

		
		out.print("</content>");
		
	}

	/**
	 * doGet()
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
