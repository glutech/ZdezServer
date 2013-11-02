package cn.com.zdez.service;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.bgRunning.NewNewsMsg;
import cn.com.zdez.bgRunning.NewsResend;
import cn.com.zdez.cache.NewsMsgCache;
import cn.com.zdez.dao.NewsDao;
import cn.com.zdez.po.News;
import cn.com.zdez.util.ContentOperation;
import cn.com.zdez.vo.NewsVo;

public class NewsService {

	NewsDao dao = new NewsDao();

	/**
	 * 新增news，写入相关表格。把content单独处理，存成html格式，便于网页端的显示
	 * 
	 * @param n
	 * @param destUsers
	 * @param rootPath
	 * @return
	 */
	public boolean newNews(News n, String[] school, String rootPath) {
		boolean flag = false;
		if (dao.newNews(n)) {
			int newsId = dao.getLatestNewsId();
			// 将内容写入html文件，用于网页显示
			if (new ContentOperation().SaveContent("news", newsId,
					n.getContent(), rootPath)) {
				flag = true;
			}

			// 新起线程进行新闻资讯的发送
			NewNewsMsg news = new NewNewsMsg(newsId, school);
			Thread thread = new Thread(news);
			thread.start();

		}
		return flag;
	}

	public boolean newsResend(News n, int news_old_id, String rootPath) {
		boolean flag = false;
		if (dao.newNews(n)) {
			int newsId = dao.getLatestNewsId();
			// 将内容写入html文件，用于网页显示
			if (new ContentOperation().SaveContent("news", newsId,
					n.getContent(), rootPath)) {
				flag = true;
			}
			
			// 新起线程进行新闻资讯的发送
			NewsResend ns = new NewsResend(newsId, news_old_id);
			Thread thread = new Thread(ns);
			thread.start();
		}

		return flag;
	}

	public boolean newNews_Receivers(int newsId, List<Integer> destUsers) {
		return dao.newNews_Receivers(newsId, destUsers);
	}

	/**
	 * 插入相关表格过程中出错，进行无效信息的删除
	 * 
	 * @param newsId
	 */
	public void roll_back(int newsId) {
		dao.deleteReceivers(newsId);
		dao.deleteNews(newsId);
	}

	/**
	 * 根据用户id获取要更新的news
	 * 
	 * @param stuId
	 * @return
	 */
	public List<NewsVo> getNewsToUpdate(int stuId) {
		return dao.getNewsToUpdate(stuId);
		// List<NewsVo> list = new ArrayList<NewsVo>();
		// return list;
	}

	/**
	 * 更新已接收表，记录用户id和已接收的信息id
	 * 
	 * @param stuId
	 * @param newsIdArray
	 */
	public void updateNewsReceived(int stuId, String[] newsIdArray) {
		List<Integer> newsIdList = new ArrayList<Integer>();
		for (int i = 0, count = newsIdArray.length; i < count; i++) {
			newsIdList.add(Integer.parseInt(newsIdArray[i]));
		}
		dao.updateNewsReceived(stuId, newsIdList);
	}

	/**
	 * 获取news记录数，用于分页
	 * 
	 * @return
	 */
	public int getNewsCount() {
		return dao.getNewsCount();
	}

	/**
	 * 按照分页获取news
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<NewsVo> getNewsByPage(int start, int end) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<Integer> newsIdList = dao.getNewsIdList(start, end);
		// list = dao.getNewsByIdList(newsIdList);
		list = dao.getNewsToDisplayByIdList(newsIdList);
		return list;
	}

	public List<NewsVo> getNewsDetailByIdList(List<Integer> idList) {
		return dao.getNewsDetialByIdList(idList);
	}

	/**
	 * 获取符合查询条件的news记录数
	 * 
	 * @param keyword
	 * @return
	 */
	public int getNewsQueryCount(String keyword) {
		return dao.getNewsQueryCount(keyword);
	}

	/**
	 * 根据查询条件和分页获取news
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<NewsVo> getNewsQueryByPage(int start, int end, String keyword) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<Integer> idList = dao.getNewsIdList(start, end, keyword);
		list = dao.getNewsByIdList(idList);
		return list;
	}

	/**
	 * 根据idList批量获取news
	 * 
	 * @param newsIdList
	 * @return
	 */
	public List<NewsVo> getNewsByIdList(List<Integer> newsIdList) {
		return dao.getNewsByIdList(newsIdList);
	}

	/**
	 * 根据id获取news
	 * 
	 * @param newsId
	 * @return
	 */
	public News getNewsById(int newsId) {
		return dao.getNewsById(newsId);
	}

	/**
	 * 获取某一news的目标用户，用于重发
	 * 
	 * @param newsId
	 * @return
	 */
	public List<Integer> getDestUsersByNewsId(int newsId) {
		return dao.getDestUsersByNewsId(newsId);
	}

	/**
	 * 将缓存中的数据写入数据库，用于数据同步
	 */
	public void writeIntoNews_Received() {
		dao.writeIntoNews_Received();
	}

	/**
	 * 缓存数据库中所有新闻资讯，只在redis清空后运行一次
	 */
	public void cacheNewsAll() {
		List<NewsVo> list = dao.getNewsAll();
		new NewsMsgCache().cacheNewsMsg(list);
	}

}
