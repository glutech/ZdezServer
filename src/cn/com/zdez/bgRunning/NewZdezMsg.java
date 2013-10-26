package cn.com.zdez.bgRunning;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.ZdezMsgCache;
import cn.com.zdez.dao.ZdezMsgDao;
import cn.com.zdez.vo.ZdezMsgVo;

public class NewZdezMsg implements Runnable{
	
	private int zdezMsgId;
	private List<Integer> destUsers;
	
	public NewZdezMsg() {
		
	}
	
	public NewZdezMsg(int zdezMsgId, List<Integer> destUsers) {
		this.zdezMsgId = zdezMsgId;
		this.destUsers = destUsers;
	}
	
	public void NewMsg() {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		List<Integer> zdezMsgIdList = new ArrayList<Integer>();
		zdezMsgIdList.add(zdezMsgId);
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
