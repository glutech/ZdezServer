package cn.com.zdez.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.service.SchoolAdminService;
import cn.com.zdez.vo.SchoolAdminVo;

public class Admin_SchoolAdminManageQuery extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		String keyword = new String(request.getParameter("keyword").getBytes(
				"ISO-8859-1"), "utf-8");

		hs.setAttribute("keyword", keyword);

		// 获取学校管理员本人已发送的校园通知
		SchoolAdminService service = new SchoolAdminService();
		int all = service.getSchoolAdminCount(keyword); // 一共多少条记录

		// 分页
		int currentPage = Integer.parseInt(request.getParameter("currentPage")); // 当前页码
		int allPage = 1; // 页数，默认1
		int countPerPage = 20; // 每页显示的条数
		if (all % countPerPage == 0) {
			// 记录一共有20的整数倍
			allPage = all / countPerPage;
		} else {
			allPage = all / countPerPage + 1;
		}

		List<SchoolAdminVo> sAdminToBeDisplayed = new ArrayList<SchoolAdminVo>();
		int start = (currentPage - 1) * countPerPage;
		int end = 0;
		if (currentPage == allPage) {
			end = all - (currentPage - 1) * countPerPage;
		} else {
			end = currentPage * countPerPage;
		}
		sAdminToBeDisplayed = service.getSchoolAdminByPage(start, end, keyword);

		request.setAttribute("allpage", allPage);
		request.setAttribute("all", all);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("schoolAdminList", sAdminToBeDisplayed);
		request.getRequestDispatcher("admin_SchoolAdminManageQuery.jsp").forward(
				request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
