package cn.com.zdez.bgRunning;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.NewsMsgCache;
import cn.com.zdez.dao.NewsDao;
import cn.com.zdez.service.NewsService;
import cn.com.zdez.vo.NewsVo;

public class NewsResend implements Runnable{
	
	private int newsId;
	private int news_old_id;
	
	public NewsResend() {
		
	}
	
	public NewsResend(int newsId, int news_old_id) {
		this.newsId = newsId;
		this.news_old_id = news_old_id;
	}
	
	public void Resend() {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<Integer> newsIdList = new ArrayList<Integer>();
		newsIdList.add(newsId);
		
		NewsService service = new NewsService();
		List<Integer> destUsers = service.getDestUsersByNewsId(news_old_id);
		
		service.newNews_Receivers(newsId, destUsers);
		
		list = new NewsDao().getNewsByIdList(newsIdList);
		
		//给微软服务器发送
		
		// 缓存
		NewsMsgCache cache = new NewsMsgCache();
		cache.cacheNewsMsg(list);
		cache.cacheNewsMsg_Receivers(newsId, destUsers);
	}
	
	public void run() {
		System.out.println("Starting resend news message....");
		Resend();
	}

}
