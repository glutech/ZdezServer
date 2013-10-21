package cn.com.zdez.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import cn.com.zdez.job.InitScheduler;
import cn.com.zdez.service.SchoolMsgService;

public class Admin_AutoRun extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Admin_AutoRun() {
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
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		SchoolMsgService service = new SchoolMsgService();
		service.writeIntoSchoolMsg_ReceivedStu();
		service.cacheReceiversAndReceived();
		service.cacheSchoolMsgRecievedNum();
//		new InitScheduler().init();
	}

}
