package cn.com.zdez.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.service.ZdezMsgService;
import cn.com.zdez.vo.ZdezMsgVo;

public class ZdezMsgCache {

	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();

	/**
	 * 缓存zdezMsg，每次发送时进行缓存，缓存40条
	 * 
	 * @param list
	 */
	public void cacheZdezMsg(List<ZdezMsgVo> list) {

		try {

			for (int i = 0, count = list.size(); i < count; i++) {
				int zdezMsgId = list.get(i).getZdezMsgId();
				// 利用list控制缓存的信息为40条
				jedis.rpush("idList:zdezMsg", Integer.toString(zdezMsgId));
				if (jedis.llen("idList:zdezMsg") > 40) {
					// delete some data
					// 移除最早存储的数据
					String msgId = jedis.lpop("idList:zdezMsg");
					jedis.srem("zdezMsg:idList", msgId);
					jedis.del("zdezMsg:" + msgId);
				}

				jedis.sadd("zdezMsg:idList", Integer.toString(zdezMsgId));
				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
				map.put("id", Integer.toString(zdezMsgId));
				map.put("title", list.get(i).getTitle());
				// getConverPath方法通用
				map.put("cover", new SchoolMsgService().getCoverPath(list
						.get(i).getContent()));

				map.put("content", list.get(i).getContent());
				map.put("date", list.get(i).getDate().toString());
				jedis.hmset("zdezMsg:" + zdezMsgId, map);
			}
		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();

	}

	/**
	 * 临时缓存zdezMsg，当用户要获取找得着信息，但是已缓存列表中没有时 将信息从数据库中取出并放到临时缓存中 临时缓存的尺寸多大还要再进行定义
	 * 
	 * @param list
	 */
	public void cacheZdezMsgTemp(List<ZdezMsgVo> list) {
		for (int i = 0, count = list.size(); i < count; i++) {
			int zdezMsgId = list.get(i).getZdezMsgId();

			try {

				jedis.sadd("temp:zdezMsg:idList", Integer.toString(zdezMsgId));
				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
				map.put("id", Integer.toString(zdezMsgId));
				map.put("title", list.get(i).getTitle());
				// getConverPath方法通用
				map.put("cover", new SchoolMsgService().getCoverPath(list
						.get(i).getContent()));

				map.put("content", list.get(i).getContent());
				map.put("date", list.get(i).getDate().toString());
				jedis.hmset("temp:zdezMsg:" + zdezMsgId, map);
			} finally {
				pool.returnResource(jedis);
			}

		}
		pool.destroy();
	}

	/**
	 * 通过msgId从缓存中获取相关信息
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public ZdezMsgVo getFromCache(int zdezMsgId) {

		ZdezMsgVo zMsgVo = new ZdezMsgVo();
		try {

			zMsgVo.setZdezMsgId(zdezMsgId);
			String title = jedis.hget("zdezMsg:" + Integer.toString(zdezMsgId),
					"title");
			String content = jedis.hget(
					"zdezMsg:" + Integer.toString(zdezMsgId), "content");
			String cover = jedis.hget("zdezMsg:" + Integer.toString(zdezMsgId),
					"cover");
			String date = jedis.hget("zdezMsg:" + Integer.toString(zdezMsgId),
					"date");
			zMsgVo.setTitle(title);
			zMsgVo.setContent(content);
			zMsgVo.setCoverPath(cover);
			zMsgVo.setDate(date);
		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
		return zMsgVo;
	}

	/**
	 * 通过msgId从临时缓存中获取相关信息
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public ZdezMsgVo getFromTempCache(int zdezMsgId) {
		ZdezMsgVo zMsgVo = new ZdezMsgVo();

		try {

			zMsgVo.setZdezMsgId(zdezMsgId);
			String title = jedis.hget(
					"temp:zdezMsg:" + Integer.toString(zdezMsgId), "title");
			String content = jedis.hget(
					"temp:zdezMsg:" + Integer.toString(zdezMsgId), "content");
			String cover = jedis.hget(
					"temp:zdezMsg:" + Integer.toString(zdezMsgId), "cover");
			String date = jedis.hget(
					"temp:zdezMsg:" + Integer.toString(zdezMsgId), "date");
			zMsgVo.setTitle(title);
			zMsgVo.setContent(content);
			zMsgVo.setCoverPath(cover);
			zMsgVo.setDate(date);
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();

		return zMsgVo;
	}

	/**
	 * 通过idList从缓存中获取信息
	 * 
	 * @param idList
	 * @return
	 */
	public List<ZdezMsgVo> getZdezMsgFromCache(List<Integer> idList) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		List<Integer> idListGetMsgFromDB = new ArrayList<Integer>();

		try {

			for (int i = 0, count = idList.size(); i < count; i++) {
				int zdezMsgId = idList.get(i);
				if (jedis.sismember("zdezMsg:idList",
						Integer.toString(zdezMsgId))) {
					// 缓存中有通知的具体内容，从缓存中取
					System.out.println("get zdezMsg from cache!");
					ZdezMsgVo zMsgVo = new ZdezMsgVo();
					zMsgVo = this.getFromCache(zdezMsgId);
					list.add(zMsgVo);
				} else if (jedis.sismember("temp:zdezMsg:idList",
						Integer.toString(zdezMsgId))) {
					// 临时缓存中有该数据，则从临时缓存中取
					System.out.println("get zdezMsg from temp cache!");
					ZdezMsgVo zMsgVo = new ZdezMsgVo();
					zMsgVo = this.getFromTempCache(zdezMsgId);
					list.add(zMsgVo);
				} else {
					// 缓存中没有通知的具体内容，从数据库中取
					idListGetMsgFromDB.add(zdezMsgId);
				}
			}
			// 将从数据库中取的记录缓存进临时缓存
			List<ZdezMsgVo> zdezMsgToCacheList = new ArrayList<ZdezMsgVo>();
			if (idListGetMsgFromDB.size() > 0) {
				System.out.println("get zdezMsg from MySQL!");
				zdezMsgToCacheList = new ZdezMsgService()
						.getZdezMsgAll(idListGetMsgFromDB);
				this.cacheZdezMsgTemp(zdezMsgToCacheList);
				list.addAll(zdezMsgToCacheList);
			}
		} finally {
			pool.returnBrokenResource(jedis);
		}

		pool.destroy();
		return list;
	}

	/**
	 * 每次发送信息时，将receivers写入缓存
	 * ZdezMsgDao.getMsgIdListtoUpdate获取要更新的信息列表时，不再经过MySQL
	 * 
	 * @param zdezMsgId
	 *            zdezMsg信息Id
	 * @param destUsersId
	 *            列表中存储此条信息的接收者id
	 */
	public void cacheZdezMsg_Receivers(int zdezMsgId, List<Integer> destUsersId) {

		try {

			for (int i = 0, count = destUsersId.size(); i < count; i++) {
				String key = "zdezMsg:toReceive:";
				key = key + Integer.toString(destUsersId.get(i));
				jedis.sadd(key, Integer.toString(zdezMsgId));
			}

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 将MySQL中表zdezMsg_receivers中的所有数据写入缓存 一般只在redis清空后执行一次
	 * 
	 * @param map
	 *            存储zdezMsgId和receiversID的对应关系
	 */
	public void cacheZdezMsg_ReceiversAll(HashMap<Integer, List<Integer>> map) {
		try {

			Iterator<Map.Entry<Integer, List<Integer>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				String key = "zdezMsg:toReceive:";
				Map.Entry<Integer, List<Integer>> entry = (Map.Entry<Integer, List<Integer>>) it
						.next();
				String stuId = Integer.toString(entry.getKey());
				key = key + stuId;
				List<Integer> zdezMsgIdList = entry.getValue();
				for (int i = 0, count = zdezMsgIdList.size(); i < count; i++) {
					jedis.sadd(key, Integer.toString(zdezMsgIdList.get(i)));
				}
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
	public void cacheZdezMsg_toReceive(int stuId, List<Integer> list) {
		try {
			String key = "zdezMsg:toReceive:" + stuId;
			int count = list.size();
			for (int i=0; i<count; i++) {
				jedis.sadd(key, Integer.toString(list.get(i)));
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 客户端返回已收到的确认信息时，将接收者id和接收到的信息id写入缓存，并将某条信息的接收数+1
	 * 
	 * @param stuId
	 * @param zdezMsgIdList
	 */
	public void cacheZdezMsg_ReceivedStu(int stuId, List<Integer> zdezMsgIdList) {
		try {

			String keyAll = "zdezMsg:received:stuIdAll";
			jedis.sadd(keyAll, Integer.toString(stuId));
			int count = zdezMsgIdList.size();
			for (int i = 0; i < count; i++) {
				String key = "zdezMsg:received:";
				key = key + Integer.toString(stuId);
				jedis.sadd(key, Integer.toString(zdezMsgIdList.get(i)));
				jedis.hincrBy("zdezMsg:receivedNum",
						Integer.toString(zdezMsgIdList.get(i)), 1);
			}

		} finally {
			zdezMsgIdList = null;
			pool.returnResource(jedis);
		}

		pool.destroy();
	}

	/**
	 * 将MySQL中表zdezMsg_receivedStu中的所有数据写入缓存 一般只在redis清空后调用一次
	 * 
	 * @param map
	 *            存储学生id与已接收信息id的对应关系
	 */
	public void cacheZdezMsg_ReceivedStuAll(
			HashMap<Integer, List<Integer>> map) {
		try {

			Iterator<Map.Entry<Integer, List<Integer>>> it = map.entrySet()
					.iterator();
			// 此key记录了redis中缓存了哪些stuId的已接收列表，为了后续将redis中的数据写入数据库（主要是回写zdezMsg_receivedStu）
			String keyAll = "zdezMsg:received:stuIdAll";
			while (it.hasNext()) {
				String key = "zdezMsg:received:";
				Map.Entry<Integer, List<Integer>> entry = (Map.Entry<Integer, List<Integer>>) it
						.next();
				String stuId = Integer.toString(entry.getKey());
				jedis.sadd(keyAll, stuId);
				List<Integer> zdezMsgIdList = entry.getValue();
				key = key + stuId;
				int count = zdezMsgIdList.size();
				for (int i = 0; i < count; i++) {
					jedis.sadd(key, Integer.toString(zdezMsgIdList.get(i)));
				}
			}

		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
	}
	
	/**
	 * 从数据库中取出每条信息的接收数， 写入redis，只在redis清空后调用一次
	 * 由ZdezMsgService.cacheZdezMsgReceivedNum调用
	 * @param map
	 */
	public void cacheReceivedNum(HashMap<Integer, Integer> map) {
		try {
			
			String key = "zdezMsg:receivedNum";
			Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
				String zdezMsgId = Integer.toString(entry.getKey());
				String receivedNum = Integer.toString(entry.getValue());
				jedis.hset(key, zdezMsgId, receivedNum);
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}
}
