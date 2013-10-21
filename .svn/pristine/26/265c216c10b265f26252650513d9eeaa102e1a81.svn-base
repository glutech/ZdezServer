package cn.com.zdez.job;

import java.util.TimerTask;

import cn.com.zdez.service.DBSyncService;

public class DBSyncTimer extends TimerTask{
	
	private static boolean isRunning = true;
	
	@Override
	public void run() {
		if (isRunning) {
			new Thread(new DBSyncService()).start();
		}
	}

}
