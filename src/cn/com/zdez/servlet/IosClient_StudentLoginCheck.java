package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.po.Student;
import cn.com.zdez.service.LoginService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.util.MD5;
import cn.com.zdez.vo.StudentVo;

import com.google.gson.Gson;

public class IosClient_StudentLoginCheck extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public IosClient_StudentLoginCheck() {
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
		doPost(request, response);
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
		// String errorMsg = "";
		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		String deviceid = request.getParameter("deviceid");

		System.out.println("用户请求登陆验证，用户名和密码为：" + userName + "~~" + password
				+ ",客户端版本名称为：" + deviceid);

		String result = "";
		LoginService ls = new LoginService();
		//改造StudentService使之能够添加对应的设备号
		StudentService ss = new StudentService();
		Student student = new Student();
		student.setUsername(userName);
		password = new MD5().toMD5String(password);
		student.setPassword(password);
		Gson gson = new Gson();
		if (ls.studentLoginCheck(student)) {
			StudentVo studentVo = ss.getStudentVoByUsername(userName);
			System.out.println("用户登陆成功，用户id为：" + studentVo.getId());
			result = gson.toJson(studentVo);
		} else {
			result = "fail";
		}

		PrintWriter out = response.getWriter();
		result = new String(result.getBytes("utf-8"), "gbk");
		out.append(result);
		out.close();
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
