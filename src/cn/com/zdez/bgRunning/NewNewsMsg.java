package cn.com.zdez.bgRunning;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.NewsMsgCache;
import cn.com.zdez.dao.NewsDao;
import cn.com.zdez.vo.NewsVo;

public class NewNewsMsg implements Runnable{
	
	private int newsId;
	private List<Integer> destUsers;
	
	public NewNewsMsg() {
		
	}
	
	public NewNewsMsg(int newsId, List<Integer> destUsers) {
		this.newsId = newsId;
		this.destUsers = destUsers;
	}
	
	public void NewMsg() {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<Integer> newsIdList = new ArrayList<Integer>();
		newsIdList.add(newsId);
		list = new NewsDao().getNewsByIdList(newsIdList);
		
		// 给微软服务器发送
		
		//  缓存
		NewsMsgCache cache = new NewsMsgCache();
		cache.cacheNewsMsg(list);
		cache.cacheNewsMsg_Receivers(newsId, destUsers);
	}
	
	public void run() {
		System.out.println("Starting create a new News Message...");
		NewMsg();
	}

}
