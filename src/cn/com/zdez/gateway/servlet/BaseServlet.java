package cn.com.zdez.gateway.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {

	/**
	 * 获取请求IP地址（不一定准确），获取失败时返回空字符串
	 */
	protected String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		if (ip == null) {
			ip = "";
		}
		return ip;
	}

	/**
	 * 响应JSON内容，调用前不得有其它输出
	 */
	protected void outJson(Object object, HttpServletResponse response)
			throws IOException {
		// 构造JSON串
		String jsonString = new Gson().toJson(object);
		// jsonString = new String(jsonString.getBytes("utf-8"), "iso-8859-1");
		// 设置HTTP头
		response.setHeader("Content-Type", "application/json; charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		// 输出正文
		PrintWriter out = response.getWriter();
		out.append(jsonString);
		out.close();
	}

	/*
	 * 仅允许POST请求，其它请求报403错误
	 */

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(403);
		// doPost(req, resp); // DEBUG ONLY
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(403);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(403);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(403);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(403);
	}

	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(403);
	}
}
