package cn.com.zdez.service;

public class DBSyncService implements Runnable{
	
	public DBSyncService() {
		super();
	}
	
	private void dbSync() {
		new SchoolMsgService().writeIntoSchoolMsg_ReceivedStu();
	}
	
	public void run() {
		System.out.println("Start dbSync....");
		dbSync();
	}

}
