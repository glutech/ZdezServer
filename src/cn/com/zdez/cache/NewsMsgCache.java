package cn.com.zdez.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.service.NewsService;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.NewsVo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class NewsMsgCache {
	
	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();
	
	public void cacheNewsMsg(List<NewsVo> list) {
		try {
			
			for (int i=0, count = list.size(); i <count; i++) {
				int newsId = list.get(i).getId();
				
				jedis.sadd("newsMsg:idList", Integer.toString(newsId));
				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
				map.put("id", Integer.toString(newsId));
				map.put("title", list.get(i).getTitle());
				map.put("cover", new SchoolMsgService().getCoverPath(list.get(i).getContent()));
				map.put("content", list.get(i).getContent());
				map.put("date", list.get(i).getDate());
				map.put("destSchool", "destSchool:" + list.get(i).getId());
				map.put("isTop", Integer.toString(list.get(i).getIsTop()));
				jedis.hmset("newsMsg:" + newsId, map);
				
				for (int n=0, count1 = list.get(i).getDestSchools().size(); n < count1;n++) {
					jedis.sadd("destSchool:" + list.get(i).getId(), list.get(i).getDestSchools().get(n));
				}
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}
	
	/**
	 * 通过msgId从缓存中获取相关信息
	 * @param newsId
	 * @return
	 */
	public NewsVo getFromCache(int newsId) {
		NewsVo newsVo = new NewsVo();
		try {
			newsVo.setId(newsId);
			String title = jedis.hget("newsMsg:" + Integer.toString(newsId), "title");
			String content = jedis.hget("newsMsg:" + Integer.toString(newsId), "content");
			String cover = jedis.hget("newsMsg:" + Integer.toString(newsId), "cover");
			String isTop = jedis.hget("newsMsg:" + Integer.toString(newsId), "isTop");
			
			String date = jedis.hget("newsMsg:" + Integer.toString(newsId), "date");
			
			Set<String> schoolSet = jedis.smembers("destSchool:" + Integer.toString(newsId));
			List<String> temp = new ArrayList<String>();
			for (String school : schoolSet) {
				temp.add(school);
			}
			
			newsVo.setTitle(title);
			newsVo.setContent(content);
			newsVo.setCoverPath(cover);
			newsVo.setDate(date);
			newsVo.setIsTop(Integer.parseInt(isTop));
			newsVo.setDestSchools(temp);
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
		return newsVo;
	}
	
	public List<NewsVo> getNewsMsgFromCache(List<Integer> idList) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<Integer> idListGetMsgFromDB = new ArrayList<Integer>();
		
		try {
			for (int i=0, count = idList.size(); i<count; i++) {
				int newsId = idList.get(i);
				if (jedis.sismember("newsMsg:idList", Integer.toString(newsId))) {
					System.out.println("get news from cache!");
					NewsVo newsVo = new NewsVo();
					newsVo = this.getFromCache(newsId);
					list.add(newsVo);
				} else {
					idListGetMsgFromDB.add(newsId);
				}
			}
			
			// 将从数据库中取出的记录放入缓存
			List<NewsVo> newsMsgToCacheList = new ArrayList<NewsVo>();
			if (idListGetMsgFromDB.size() > 0) {
				System.out.println("get news from MySQL!");
				newsMsgToCacheList = new NewsService().getNewsByIdList(idListGetMsgFromDB);
				this.cacheNewsMsg(newsMsgToCacheList);
				list.addAll(newsMsgToCacheList);
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
		return list;
	}
	
	/**
	 * 每次发送信息时，将receivers写入缓存
	 * @param newsId
	 * @param destUsers
	 */
	public void cacheNewsMsg_Receivers(int newsId, List<Integer> destUsers) {
		try {
			
			for (int i=0, count = destUsers.size(); i<count; i++) {
				String key = "newsMsg:toReceive:";
				key = key + Integer.toString(destUsers.get(i));
				jedis.sadd(key, Integer.toString(newsId));
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}
	
	/**
	 * 缓存每个学生的信息接收列表，用于缓存中没有学生信息接收列表的时候
	 * @param stuId
	 * @param list
	 */
	public void cacheNewsMsg_toReceive(int stuId, List<Integer> list) {
		try {
			
			String key = "newsMsg:toReceive:" + stuId;
			for (int i=0, count = list.size(); i<count; i++) {
				jedis.sadd(key, Integer.toString(list.get(i)));
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}
	
	/**
	 * 客户端返回已收到的确认信息时，将接收者id和接收到的信息id写入缓存，并将某条信息的接收数+1
	 * @param stuId
	 * @param list
	 */
	public void cacheNewsMsg_ReceivedStu(int stuId, List<Integer> list) {
		try {
			
			String keyAll = "newsMsg:received:stuIdAll";
			jedis.sadd(keyAll, Integer.toString(stuId));
			int count = list.size();
			for (int i=0; i<count; i++) {
				String key = "newsMsg:received:";
				key = key + Integer.toString(stuId);
				jedis.sadd(key, Integer.toString(list.get(i)));
				jedis.hincrBy("newsMsg:receivedNum", Integer.toString(list.get(i)), 1);
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}

}
