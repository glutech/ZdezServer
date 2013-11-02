package cn.com.zdez.bgRunning;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.ZdezMsgCache;
import cn.com.zdez.dao.ZdezMsgDao;
import cn.com.zdez.service.ZdezMsgService;
import cn.com.zdez.vo.ZdezMsgVo;

public class NewZdezMsg implements Runnable{
	
	private int zdezMsgId;
	private List<Integer> destUsers;
	private String[] grade;
	private String[] major;
	
	public NewZdezMsg() {
		
	}
	
	public NewZdezMsg(int zdezMsgId, List<Integer> destUsers, String[] grade, String[] major) {
		this.zdezMsgId = zdezMsgId;
		this.destUsers = destUsers;
		this.grade = grade;
		this.major = major;
	}
	
	public void NewMsg() {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		List<Integer> zdezMsgIdList = new ArrayList<Integer>();
		zdezMsgIdList.add(zdezMsgId);
		
		// 数据写入相关的表
		ZdezMsgService service = new ZdezMsgService();
		service.newZdezMsg_Grade(zdezMsgId, grade);
		service.newZdezMsg_Major(zdezMsgId, major);
		service.newZdezMsg_Receivers(zdezMsgId, destUsers);
		
		list = new ZdezMsgDao().getZdezMsgAll(zdezMsgIdList);

		// 给微软服务器发送

		// 缓存
		ZdezMsgCache cache = new ZdezMsgCache();
		cache.cacheZdezMsg(list);
		cache.cacheZdezMsg_Receivers(zdezMsgId, destUsers);
	}
	
	public void run() {
		System.out.println("Starting create a new Zdez Message...");
		NewMsg();
	}

}
