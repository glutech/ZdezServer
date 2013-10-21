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

import cn.com.zdez.po.Major;

public class Admin_getMajorListBySelectedDepartments extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * AJAX 中使用，用于根据选中的学院和学业层次更新专业列表
		 */

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/xml; charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");

		HttpSession hs = request.getSession();
		List<Major> majorList = (List<Major>) hs.getAttribute("majorList");

		PrintWriter out = response.getWriter();

		String departmentIdList = URLDecoder.decode(
				request.getParameter("selectedId"), "UTF-8");
		String schoolSysList = URLDecoder.decode(
				request.getParameter("schoolSysId"), "UTF-8");

		String[] departmentsId = departmentIdList.split(", ");
		String[] schoolSysId = schoolSysList.split(",");

		if (departmentsId.length > 0) {
			out.println("<response>");
			for (int i = 0, count1 = departmentsId.length; i < count1; i++) {
				for (int p = 0, count3 = schoolSysId.length; p < count3; p++) {
					for (int q = 0, count2 = majorList.size(); q < count2; q++) {
						if (majorList.get(q).getDepartmentId() == Integer
								.parseInt(departmentsId[i])
								&& majorList.get(q).getSchoolSysId() == Integer
										.parseInt(schoolSysId[p])) {
							out.println("<res><id>" + majorList.get(q).getId()
									+ "</id><name>"
									+ majorList.get(q).getName()
									+ "</name></res>");
						}
					}
				}
			}
			out.println("</response>");
			out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
}
