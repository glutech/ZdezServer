package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.SchoolMsgVo;

public class AndroidClient_GetUpdateSchoolMsg extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AndroidClient_GetUpdateSchoolMsg() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String versionName = "1.0";
		try {
			versionName = request.getParameter("version_name");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Android用户id为: " + user_id + ",客户端版本名称为："
				+ versionName);

		SchoolMsgService schoolMsgService = new SchoolMsgService();
		ArrayList<SchoolMsgVo> result = new ArrayList<SchoolMsgVo>();

		try {

			result = (ArrayList<SchoolMsgVo>) schoolMsgService
					.getMsgToUpdate(Integer.valueOf(user_id));
		} catch (NumberFormatException nfe) {
//			System.out.println("no id passed from client...");
		}

		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String resultStr = gson.toJson(result);
//		System.out.println("直接从数据库中取出的数据是：" + resultStr);
		resultStr = new String(resultStr.getBytes("UTF-8"), "iso-8859-1");
		out.append(resultStr);
		out.close();
		result = null;
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
