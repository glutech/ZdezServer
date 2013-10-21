package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.News;
import cn.com.zdez.service.NewsService;
import cn.com.zdez.service.StudentService;

public class Admin_NewNewsServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		// 获取网页传过来的信息

		String[] school = request.getParameterValues("xuexiao[]");
		String title = request.getParameter("news_title");
		String content = request.getParameter("news_content");

		// 解决reload时出错
		if (title == null || content == null || school == null) {
			title = (String) hs.getAttribute("title");
			content = (String) hs.getAttribute("content");
			school = (String[]) hs.getAttribute("school");
		}
		hs.setAttribute("title", title);
		hs.setAttribute("content", content);
		hs.setAttribute("school", school);
		// ---

		StudentService sService = new StudentService();
		// 获取所有用户，新闻的发送对象应该是所有安装客户端并登录的用户
		List<Integer> destUsers = sService.getStuIdListBySchool(school);

		// send message here.
		boolean  flag = false;
		flag = (Boolean) hs.getAttribute("adminUserLoginSucessFlag");

		if (!flag) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {
			title = new String(title.getBytes("ISO-8859-1"), "utf-8");
			content = new String(content.getBytes("ISO-8859-1"), "utf-8");
			
			News n = new News();
			n.setTitle(title);
			n.setContent(content);
			
			NewsService nService = new NewsService();
			// 发送信息
			if (nService.newNews(n, destUsers, (String)request.getSession().getAttribute("rootPath"))) {
				// 发送成功
				request.getRequestDispatcher("admin_NewNewsSuccess.jsp")
						.forward(request, response);
			} else {
				// 发送失败
				request.setAttribute("errorMsg", "信息已发送，但所选条件下无发送对象！");
				request.getRequestDispatcher("admin_NewNewsSuccess.jsp").forward(request,
						response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
