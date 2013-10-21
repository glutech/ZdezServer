package cn.com.zdez.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.zdez.job.DBSyncTimer;

public class DBSyncServlet extends HttpServlet {
	
	private DBSyncTimer dbSyncTimer;
	private Timer timer;

	public DBSyncServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
		if (timer != null) {
			timer.cancel();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void init() throws ServletException {
		System.out.println("DBSync Initializing...");
		//定时器开关
		String startTask = getInitParameter("startTask");
		//开始运行时间
		Calendar calender = Calendar.getInstance();
		//缓冲时间（秒）
		Long intervalTime = Long.parseLong(getInitParameter("intervalTime"));
		
		//启动定时器
		if (startTask.equals("true")) {
			timer = new Timer(true);
			dbSyncTimer = new DBSyncTimer();
			timer.schedule(dbSyncTimer, calender.getTime(), intervalTime * 1000);
		}
	}

}
