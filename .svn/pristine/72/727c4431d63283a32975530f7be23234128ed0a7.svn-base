package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.com.zdez.util.ParseXmlService;
import cn.com.zdez.vo.ClientVersionUpdateType;

public class AndroidClient_CheckAPKUpdate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AndroidClient_CheckAPKUpdate() {
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
		ClientVersionUpdateType version = new ClientVersionUpdateType();

		ParseXmlService service = new ParseXmlService();
		InputStream inStream = ParseXmlService.class.getClassLoader()
				.getResourceAsStream("version.xml");
		// System.out.println("Getted version.xml from server: "
		// + inStream.toString());
		HashMap<String, String> mHashMap = null;
		try {
			mHashMap = service.parseXml(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String result = "";

		if (null != mHashMap) {
			version.setVersion(Integer.valueOf(mHashMap.get("version")));
			version.setVersionName(mHashMap.get("name"));
			version.setUpdateUrl(mHashMap.get("url"));
			Gson gson = new Gson();
			result = gson.toJson(version);
		} else {
			result = "error";
		}

		PrintWriter out = response.getWriter();
		out.append(result);
		out.close();

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

		doGet(request, response);
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
