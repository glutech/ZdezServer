package cn.com.zdez.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AndroidClient_CrashsReport extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AndroidClient_CrashsReport() {
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

		String fileName = request.getParameter("filename");
		String stacktrace = request.getParameter("stacktrace");

		File fDir = new File(File.separator);
		String absoluteFooWebPath = getServletContext().getRealPath("/");
		String basePath = absoluteFooWebPath + "crash_logs" + File.separator;
		try {
			if (!(new File(basePath).isDirectory())) {
				System.out.println(basePath + " is not a directory!");
				new File(basePath).mkdir();
				System.out.println(!(new File(basePath).isDirectory()));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		String strFile = basePath + fileName;
		File f = new File(fDir, strFile);
		System.out.println("The file path is: " + strFile);
		try {
			FileOutputStream fs = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(fs, "GBK");
			byte[] b = stacktrace.getBytes();
			fs.write(b);
			fs.flush();
			fs.close();
			System.out.println("Write into temp success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
