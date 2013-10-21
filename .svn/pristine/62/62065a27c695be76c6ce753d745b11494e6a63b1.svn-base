package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.util.Statistics;

public class Admin_StatisticsServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HashMap<String, HashMap<String, Integer>> statisticsMap = new HashMap<String, HashMap<String,Integer>>();
		Statistics s = new Statistics();
		statisticsMap = s.mapConvert(s.getStatisticsData());
		
		request.setAttribute("statisticsMap", statisticsMap);
		request.getRequestDispatcher("admin_Statistics.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
