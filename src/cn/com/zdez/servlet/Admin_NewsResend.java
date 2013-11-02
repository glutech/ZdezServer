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

public class Admin_NewsResend extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		String newsId = request.getParameter("newsId");

		if (newsId == null) {
			newsId = (String) hs.getAttribute("newsId");
		}
		hs.setAttribute("newsId", newsId);

		int newsIdInt = Integer.parseInt(newsId);

		NewsService service = new NewsService();
		News newsOld = service.getNewsById(newsIdInt);
		News newsNews = new News();
		newsNews.setTitle(newsOld.getTitle());
		newsNews.setContent(newsOld.getContent());

		List<Integer> destUsers = service.getDestUsersByNewsId(newsIdInt);

		// send message here.
		boolean flag = false;
		flag = (Boolean) hs.getAttribute("adminUserLoginSucessFlag");

		if (!flag) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {

			// 发送信息
			if (service.newsResend(newsNews, newsIdInt, (String) request.getSession()
					.getAttribute("rootPath"))) {
				// 发送成功
				request.getRequestDispatcher("admin_NewNewsSuccess.jsp")
						.forward(request, response);
			} else {
				// 发送失败
				request.setAttribute("errorMsg", "没有适合已选发送条件的对象，请重新选择");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
