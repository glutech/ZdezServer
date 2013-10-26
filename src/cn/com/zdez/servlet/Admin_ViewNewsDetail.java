package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.cache.NewsMsgCache;
import cn.com.zdez.service.NewsService;
import cn.com.zdez.vo.NewsVo;

public class Admin_ViewNewsDetail extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int newsId= Integer.parseInt(request.getParameter("newsId"));
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(newsId);
		List<NewsVo> list = new NewsMsgCache().getNewsMsgFromCache(idList);
		
		NewsVo newsVo = new NewsVo();
		for(NewsVo item : list) {
			newsVo = item;
		}
		
		request.setAttribute("news", newsVo);
		request.getRequestDispatcher("admin_ViewNewsDetail.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}


}
