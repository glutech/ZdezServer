package cn.com.zdez.service;

public class DBSyncService implements Runnable{
	
	public DBSyncService() {
		super();
	}
	
	private void dbSync() {
		new SchoolMsgService().writeIntoSchoolMsg_ReceivedStu();
		new ZdezMsgService().writeIntoZdezMsg_ReceivedStu();
		new NewsService().writeIntoNews_Received();
	}
	
	public void run() {
		System.out.println("Start dbSync....");
		dbSync();
	}

}
