package cn.com.zdez.bgRunning;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.SchoolMsgCache;
import cn.com.zdez.dao.SchoolMsgDao;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.po.Student;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.vo.SchoolMsgVo;

public class NewSchoolMsg implements Runnable{
	
	private SchoolMsg msg;
	private int schoolMsgId;
	private List<Integer> destUsers;
	
	public NewSchoolMsg() {

	}
	
	public NewSchoolMsg(SchoolMsg msg, int schoolMsgId, List<Integer> destUsers) {
		this.msg = msg;
		this.schoolMsgId = schoolMsgId;
		this.destUsers = destUsers;
	}
	
	private void NewMsg() {

		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
		List<Integer> schoolMsgIdList = new ArrayList<Integer>();
		schoolMsgIdList.add(schoolMsgId);
		list = new SchoolMsgDao().getSchoolMsgAll(schoolMsgIdList);

		// 给微软服务器发送
//		List<Student> stuList = new StudentService()
//				.getStudentByIdList(destUsers);
//		for (Student stu : stuList) {
//			for (SchoolMsgVo sMsgVo : list) {
//				if (stu.getStaus().contains("http://")) {
//					new SchoolMsgService().sendMsgToWP(
//							stu.getUsername(), sMsgVo, stu.getStaus());
//				} else {
//					// for iOS
//				}
//			}
//		}

		// 写入Redis缓存
		SchoolMsgCache cache = new SchoolMsgCache();
		cache.cacheSchoolMsg(list, msg.getSchoolAdminUsername());
		cache.cacheSchoolMsg_Receivers(schoolMsgId, destUsers);
	}
	
	public void run() {
		System.out.println("Starting create a new School Message....");
		NewMsg();
	}
	
}
