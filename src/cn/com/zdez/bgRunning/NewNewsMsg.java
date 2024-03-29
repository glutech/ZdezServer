package cn.com.zdez.bgRunning;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.cache.NewsMsgCache;
import cn.com.zdez.dao.NewsDao;
import cn.com.zdez.service.NewsService;
import cn.com.zdez.service.StudentService;
import cn.com.zdez.vo.NewsVo;

public class NewNewsMsg implements Runnable{
	
	private int newsId;
	private String[] school;
	
	public NewNewsMsg() {
		
	}
	
	public NewNewsMsg(int newsId, String[] school) {
		this.newsId = newsId;
		this.school = school;
	}
	
	public void NewMsg() {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<Integer> newsIdList = new ArrayList<Integer>();
		newsIdList.add(newsId);

		StudentService sService = new StudentService();
		// 获取所有用户，新闻的发送对象应该是所有安装客户端并登录的用户
		List<Integer> destUsers = sService.getStuIdListBySchool(school);

		new NewsService().newNews_Receivers(newsId, destUsers);

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
