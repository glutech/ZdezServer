package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.ZdezMsgService;
import cn.com.zdez.vo.AckType;

import com.google.gson.Gson;

public class WPClient_UpdateZdezMsgReceived extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public WPClient_UpdateZdezMsgReceived() {
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
		doPost(request, response);
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

		String ackStr = request.getParameter("ack");

		Gson gson = new Gson();
		AckType ack = gson.fromJson(ackStr, AckType.class);

		ZdezMsgService service = new ZdezMsgService();
		String[] ids = new String[ack.getMsgIds().size()];
		for (int i = 0; i < ack.getMsgIds().size(); i++)
			ids[i] = ack.getMsgIds().get(i);
		System.out.println("User id:" + ack.getUserId());
		service.updateZdezMsgReceived(ack.getUserId(), ids);
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