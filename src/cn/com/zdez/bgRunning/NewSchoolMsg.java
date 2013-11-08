package cn.com.zdez.bgRunning;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import cn.com.zdez.cache.SchoolMsgCache;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.GradeService;
import cn.com.zdez.service.MajorService;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.service.SchoolService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.vo.SchoolMsgVo;

/*导入ios发送所需的包*/
import javapns.Push;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.transmission.PushQueue;

public class NewSchoolMsg implements Runnable {

	private SchoolMsg msg;
	private int schoolMsgId;
	private String[] grade;
	private String[] department;
	private String[] major;
	private String[] teachers;
	private SchoolAdmin schoolAdmin;
	List<Integer> destIosUsers;
	List<Integer> destWpUsers;
	
	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();

	public NewSchoolMsg() {

		this.destIosUsers = new ArrayList<Integer>();
		this.destWpUsers = new ArrayList<Integer>();
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
		
		this.destIosUsers = new ArrayList<Integer>();
		this.destWpUsers = new ArrayList<Integer>();
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
		
		//区分设备类型，填充ios, wp列表
		fillIosWpLists(destUsers);
		
		//发送ios信息给ios用户
		sendIosMessage(msg.getTitle(),msg.getId(),destIosUsers);
		
		//发送wp信息给wp用户
		sendWpMessage();
		
		destUsers = null;
		list = null;
	}
	
	/**
	 * 用于获取设备型号，ios返回1，winphone返回2, android返回0
	 * @param id
	 * @return
	 */
	public int checkBrand(int id){
		String pattern = getDeviceId(id);
		int i = -1;
		if(pattern != null){
			if(pattern.equals("106289999")){
				i = 0;
			}else if(pattern.startsWith("http")){
				i = 2;
			}else {
				i = 1;
			}
		}
		return i;
	}
	
	/**
	 * 用于对设备进行分类，同是对两个组别ios, wp进行列表进行赋值
	 */
	public void fillIosWpLists(List<Integer> destUsers){
		for(int i = 1; i < destUsers.size(); i++){
			int tempusr = destUsers.get(i);
			System.out.println("tempUser:~~~ " +tempusr);
			if(checkBrand(tempusr) == 1){
				System.out.println("destIosUsrs: "+destIosUsers.toString());
				destIosUsers.add(tempusr);
			}else if(checkBrand(tempusr) == 2){
				destWpUsers.add(tempusr);
			}
		}
	}
	
	/**
	 * 用于发送苹果通知的方法
	 * @param title
	 * @param msg_id
	 * @param destIosUsers
	 */
	public void sendIosMessage(String title, int msg_id, List<Integer> destIosUsers){
		/* Build a blank payload to customize 准备苹果发送消息*/ 
		String keystore = "zdez_dev.p12";
		String password = "www.zdez.com.cn9";
		boolean production = false;
		
		try{
	        PushNotificationPayload payload = PushNotificationPayload.complex();
	 
	       /* Customize the payload */ 
			payload.addAlert(title);
			payload.addBadge(1);
			payload.addSound("default");
		    payload.addCustomDictionary(String.valueOf(msg_id), "1234567");
			
	       /* Decide how many threads you want your queue to use */ 
	        int threads = 30;	 
	
	       /* Create the queue */ 
	        PushQueue queue = Push.queue(keystore, password, production, threads);
	        
		    /* Start the queue (all threads and connections and initiated) */ 
	        System.out.println("queue starting.......");
	        queue.start();	
	       
	        System.out.println("queue started.......");
	        
			//此处开始分设备发送,先通过循环筛选出ios及winphone设备
	 		for(int i = 0; i < destIosUsers.size(); i++){
				int usrid = destIosUsers.get(i);
				//String deviceid = "0d10908d98e72c5e5f57cd3b7e3720463c05ea3119ba2e2a5fff45606190c1c5";
				String deviceid = getDeviceId(usrid); 
	
				/* Add a notification for the queue to push */ 
				queue.add(payload, deviceid);
			}
 		
	    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeystoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDeviceTokenFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 用于发送wp通知的方法
	 */
	public void sendWpMessage(){
		
	}
	
	/**
	 * 用于从缓存获取用户的设备id
	 * @param id
	 * @return
	 */
	public String getDeviceId(int id){
		String key = "stu:id:username";
		String username = jedis.hget(key, String.valueOf(id));
		System.out.println(username);
		key = "student:"+ username;
		String pattern = jedis.hget(key, "staus");
		
		System.out.println(pattern);
		
		return pattern;
	}

	public void run() {
		System.out.println("Starting create a new School Message....");
		NewMsg();
	}

}
