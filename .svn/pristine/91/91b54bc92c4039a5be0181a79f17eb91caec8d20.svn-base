package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.cache.SchoolInfoCache;
import cn.com.zdez.util.Statistics;

public class Admin_StatisticsDetails extends HttpServlet {

	public Admin_StatisticsDetails() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		SchoolInfoCache cache = new SchoolInfoCache();
		cache.SchoolCache();
		cache.DepartmentInfoCache();
		
		Statistics s = new Statistics();
		
		HashMap<String, List<HashMap<String, Integer>>> statisticsIdMap = s.getStatisticsDetails();

		HashMap<String, List<HashMap<String, Integer>>> statisticsNameMap = s.convertIdtoName(statisticsIdMap);
		
		
		request.setAttribute("statisticsDetailsMap", statisticsNameMap);
		statisticsIdMap = null;
		statisticsNameMap = null;
		request.getRequestDispatcher("admin_StatisticsDetails.jsp").forward(request, response);
//		request.getRequestDispatcher("index.jsp").forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
		
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
