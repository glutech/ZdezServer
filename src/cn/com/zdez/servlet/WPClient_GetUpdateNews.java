package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.service.NewsService;
import cn.com.zdez.vo.NewsVo;

import com.google.gson.Gson;

public class WPClient_GetUpdateNews extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public WPClient_GetUpdateNews() {
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
		String versionName = "1.0";
		try {
			versionName = request.getParameter("version_name");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("用户请求资讯信息，用户id为: " + user_id + ",客户端版本名称为："
				+ versionName);
		// int userId = Integer.parseInt(user_id);

		NewsService ns = new NewsService();
		ArrayList<NewsVo> newsList = new ArrayList<NewsVo>();

		try {

			newsList = (ArrayList<NewsVo>) ns.getNewsToUpdate(Integer
					.valueOf(user_id));
		} catch (NumberFormatException nfe) {
			System.out.println("no id passed from client...");
		}

		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String result = gson.toJson(newsList);
		result = new String(result.getBytes("utf-8"), "iso-8859-1");
		out.append(result);
		out.close();
		newsList = null;
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

		doGet(request, response);
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
