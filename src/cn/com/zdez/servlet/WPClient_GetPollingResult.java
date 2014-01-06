package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.util.GenerateWPPollingResult;
import cn.com.zdez.vo.SchoolMsgVo;

public class WPClient_GetPollingResult extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public WPClient_GetPollingResult() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_id = request.getParameter("user_id");

		SchoolMsgService schoolMsgService = new SchoolMsgService();
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
		
		String result = "";

		try {

			list = (ArrayList<SchoolMsgVo>) schoolMsgService
					.getMsgToUpdate(Integer.valueOf(user_id));
			
			result = new GenerateWPPollingResult().generateWPPollingResult(list);
		} catch (NumberFormatException nfe) {
//			System.out.println("no id passed from client...");
		}

		PrintWriter out = response.getWriter();
		
		result = new String(result.getBytes("utf-8"), "iso-8859-1");
		
		out.append(result);
		out.close();
		result = null;
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
