package cn.com.zdez.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.zdez.po.Student;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.util.MD5;

public class Admin_NewStudent extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Admin_NewStudent() {
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

		String username = request.getParameter("username");
		String psw = request.getParameter("psw");
		String nameOrigin = request.getParameter("name");
		String name = new String(nameOrigin.getBytes("ISO-8859-1"), "utf-8");
		String gender = request.getParameter("gender");
		String isTeacher = request.getParameter("isTeacher");
		String grade = request.getParameter("grade");
		String major = request.getParameter("major");

		if (username == null || psw == null || nameOrigin == null
				|| gender == null || isTeacher == null || grade == null
				|| major == null) {
			username = (String) request.getAttribute("username");
			psw = (String) request.getAttribute("psw");
			name = (String) request.getAttribute("nameOrigin");
			gender = (String) request.getAttribute("gender");
			isTeacher = (String) request.getAttribute("isTeacher");
			grade = (String) request.getAttribute("grade");
			major = (String) request.getAttribute("major");
		}
		hs.setAttribute("username", username);
		hs.setAttribute("psw", psw);
		hs.setAttribute("name", nameOrigin);
		hs.setAttribute("gender", gender);
		hs.setAttribute("isTeacher", isTeacher);
		hs.setAttribute("grade", grade);
		hs.setAttribute("major", major);

		Student stu = new Student();
		stu.setUsername(username);
		stu.setPassword(new MD5().toMD5String(psw));
		stu.setName(name);
		stu.setGender(gender);
		stu.setStatus(0);
		stu.setStaus("106289999");
		stu.setGradeId(Integer.parseInt(grade));
		stu.setIsTeacher(Integer.parseInt(isTeacher));
		stu.setMajorId(Integer.parseInt(major));

		// System.out.println(username);
		// System.out.println(psw);
		// System.out.println(name);
		// System.out.println(gender);
		// System.out.println(isTeacher);
		// System.out.println(grade);
		// System.out.println(major);
		if (new StudentService().newStudent(stu)) {
			request.setAttribute("errorMsg", "新增用户成功");
		} else {
			request.setAttribute("errorMsg", "新增用户失败");
		}

		request.getRequestDispatcher("admin_NewStudentResult.jsp").forward(
				request, response);
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
