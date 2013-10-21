package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.SchoolSys;
import cn.com.zdez.service.SchoolService;

public class Admin_getSchoolSysBySchool extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * AJAX 中使用，用于根据选中的学校更新学业层次列表
		 */

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");

		PrintWriter out = response.getWriter();

		String schoolIdList = URLDecoder.decode(request.getParameter("selectedId"),
				"UTF-8");
		String[] schoolIds = schoolIdList.split(", ");

		List<SchoolSys> schoolSysList = new SchoolService().getSchoolSysListBySchoolIds(schoolIds);
		
		out.println("<response>");
		for (int q = 0, count2 = schoolSysList.size(); q < count2; q++) {
				out.println("<res><id>" + schoolSysList.get(q).getId()
						+ "</id><name>" + schoolSysList.get(q).getSysName()
						+ "</name></res>");
		}
		out.println("</response>");
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
}
