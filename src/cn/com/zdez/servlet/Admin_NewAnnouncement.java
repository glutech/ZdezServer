package cn.com.zdez.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.Announcement;
import cn.com.zdez.service.AnnouncementService;

public class Admin_NewAnnouncement extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Admin_NewAnnouncement() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession hs = request.getSession();

		String title = request.getParameter("anno_title");
		String content = request.getParameter("anno_content");
		String sign = request.getParameter("anno_sign");

		if (content == null || sign == null) {
			title = (String) hs.getAttribute("anno_title");
			content = (String) hs.getAttribute("anno_content");
			sign = (String) hs.getAttribute("anno_sign");
		}
		hs.setAttribute("anno_title", title);
		hs.setAttribute("anno_content", content);
		hs.setAttribute("anno_sign", sign);

		boolean flag = false;
		flag = (Boolean) hs.getAttribute("adminUserLoginSucessFlag");

		if (!flag) {
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} else {
			title = new String(title.getBytes("ISO-8859-1"), "utf-8");
			content = new String(content.getBytes("ISO-8859-1"), "utf-8");
			sign = new String(sign.getBytes("ISO-8859-1"), "utf-8");

			Announcement anno = new Announcement();
			anno.setTitle(title);
			anno.setContent(content);
//			anno.setSign("博客科技");
			anno.setSign(sign);

			if (new AnnouncementService().NewAnnouncement(anno, (String) request.getSession().getAttribute("rootPath")) ) {
				request.getRequestDispatcher("admin_NewAnnouncementSuccess.jsp")
						.forward(request, response);
			} else {
				request.setAttribute("errorMsg", "发送失败，请与技术人员联系");
				request.getRequestDispatcher("admin_NewAnnouncementSuccess.jsp")
						.forward(request, response);
			}
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
