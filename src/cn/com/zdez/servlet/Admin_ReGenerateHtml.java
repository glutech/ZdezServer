package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.dao.ConnectionFactory;
import cn.com.zdez.dao.SQLExecution;
import cn.com.zdez.util.ContentOperation;

public class Admin_ReGenerateHtml extends HttpServlet {

	public Admin_ReGenerateHtml() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String rootPath = (String) request.getSession()
				.getAttribute("rootPath");
		String sql = "select id, content from schoolMsg";
		Object[] params = {};
		SQLExecution sqlE = new SQLExecution();
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		ContentOperation co = new ContentOperation();
		try {
			while (rs.next()) {
				co.SaveContent("schoolMsg", rs.getInt(1), rs.getString(2),
						rootPath);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
