package cn.com.zdez.bgRunning;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.zdez.cache.SchoolMsgCache;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.GradeService;
import cn.com.zdez.service.MajorService;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.service.SchoolService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.vo.SchoolMsgVo;

public class NewSchoolMsg implements Runnable {

	private SchoolMsg msg;
	private int schoolMsgId;
	private String[] grade;
	private String[] department;
	private String[] major;
	private String[] teachers;
	private SchoolAdmin schoolAdmin;

	public NewSchoolMsg() {

	}

	public NewSchoolMsg(SchoolMsg msg, int schoolMsgId, String[] teachers,
			String[] grade, String[] department, String[] major,
			SchoolAdmin sAdmin) {
		this.msg = msg;
		this.schoolMsgId = schoolMsgId;
		this.teachers = teachers;
		this.grade = grade;
		this.department = department;
		this.major = major;
		this.schoolAdmin = sAdmin;
	}

	private void NewMsg() {

		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();

		StudentService sService = new StudentService();

		// 根据专业和年级获得通知接收对象列表
		List<Integer> destUsers = sService.getStudentByGradeMajor(grade, major);

		// 把所有的学校信息都发给bokeltd这个帐号
		destUsers.add(3);
		// if there are some teachers been selected
		if (teachers != null) {
			// 若选择发送给老师，则将老师用户名加入通知接收对象列表
			for (int i = 0, count = teachers.length; i < count; i++) {
				destUsers.add(Integer.parseInt(teachers[i]));
			}
		} else {
			// 若没有选择发送给老师
			// do nothing.
		}
		// 到此处为止，destUsers已经全部处理完毕

		SchoolMsgVo sMsgVo = new SchoolMsgVo();
		
		// 设置msgId
		sMsgVo.setSchoolMsgId(schoolMsgId);
		
		// 设置缩略图路径
		sMsgVo.setCoverPath(new SchoolMsgService().getCoverPath(msg
				.getContent()));
		
		// 设置标题
		sMsgVo.setTitle(msg.getTitle());
		
		// 设置内容
		sMsgVo.setContent(msg.getContent());

		// 设置信息发送时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sMsgVo.setDate(df.format(new Date()));

		// 设置信息的发送学校
		sMsgVo.setSchoolName(new SchoolService().getSchoolNameById(schoolAdmin
				.getSchoolId()));
		
		// 设置信息的发送者姓名
		sMsgVo.setSenderName(schoolAdmin.getName());

		// 设置目的年级
		
		sMsgVo.setDestGrade(new GradeService().getDescriptionById(grade));

		// 设置目的学院
		
		sMsgVo.setDestDepartment(new DepartmentService().getNameById(department));

		// 设置目的专业
		
		sMsgVo.setDestMajor(new MajorService().getNameById(major));

		// 设置已接收数
		sMsgVo.setReceivedNum(0);

		// 设置receiverNum
		
		sMsgVo.setReceiverNum(destUsers.size());

		// remarks
		
		sMsgVo.setRemarks(schoolAdmin.getRemarks());
		
		list.add(sMsgVo);

		// 进行相关数据表的写入
		SchoolMsgService smsgService = new SchoolMsgService();
		smsgService.newSchoolMsg_Grade(schoolMsgId, grade);
		smsgService.newSchoolMsg_Major(schoolMsgId, major);
		smsgService.newSchoolMsg_Receivers(schoolMsgId, destUsers);

		// 给微软服务器发送
		// List<Student> stuList = new StudentService()
		// .getStudentByIdList(destUsers);
		// for (Student stu : stuList) {
		// for (SchoolMsgVo sMsgVo : list) {
		// if (stu.getStaus().contains("http://")) {
		// new SchoolMsgService().sendMsgToWP(
		// stu.getUsername(), sMsgVo, stu.getStaus());
		// } else {
		// // for iOS
		// }
		// }
		// }

		// 写入Redis缓存
		SchoolMsgCache cache = new SchoolMsgCache();
		cache.cacheSchoolMsg(list);
		cache.cacheSchoolMsg_Receivers(schoolMsgId, destUsers);
		
		destUsers = null;
		list = null;
	}

	public void run() {
		System.out.println("Starting create a new School Message....");
		NewMsg();
	}

}
