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

import cn.com.zdez.po.Department;

public class Admin_getDepartmentBySchool extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * AJAX 中使用，用于根据选中的学校更新学院列表
		 */

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");

		HttpSession hs = request.getSession();
		List<Department> departmentList = (List<Department>) hs
				.getAttribute("departmentList");

		PrintWriter out = response.getWriter();

		String schoolId = URLDecoder.decode(request.getParameter("selectedId"),
				"UTF-8");
		String[] schoolIdList = schoolId.split(", ");

		out.println("<response>");
		if (hs.getAttribute("isSendZdezMsg") != null) {
			if ((Integer) hs.getAttribute("isSendZdezMsg") == 0) {
				out.println("<res><id>0</id><name>请选择</name></res>");
			}
		}
		for (int q = 0, count2 = departmentList.size(); q < count2; q++) {
			for (int m = 0, count3 = schoolIdList.length; m < count3; m++) {
				if (departmentList.get(q).getSchoolId() == Integer
						.parseInt(schoolIdList[m])) {
					out.println("<res><id>" + departmentList.get(q).getId()
							+ "</id><name>" + departmentList.get(q).getName()
							+ "</name></res>");
				}
			}
		}
		hs.setAttribute("isSendZdezMsg", 0);
		out.println("</response>");
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
}
