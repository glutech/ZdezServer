package cn.com.zdez.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.zdez.service.SchoolMsgService;

public class DBSync implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		 new SchoolMsgService().writeIntoSchoolMsg_ReceivedStu();
//		 System.out.println("just test: " + new Date());
	}

}
