package cn.com.zdez.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.vo.SchoolMsgVo;

public class SchoolMsgCache {

	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();

	/**
	 * 对schoolMsg进行缓存
	 * 
	 * @param list
	 */
	public void cacheSchoolMsg(List<SchoolMsgVo> list) {
		try {
			for (int i = 0, count = list.size(); i < count; i++) {
				// 利用list控制每个发送帐号缓存的信息为40条
//				jedis.rpush("idList:" + schoolAdminUsername,
//						Integer.toString(list.get(i).getSchoolMsgId()));
//				if (jedis.llen("idList:" + schoolAdminUsername) > 40) {
//					// delete some data
//					// 移除最早存储的数据
//					String schoolMsgId = jedis.lpop("idList:"
//							+ schoolAdminUsername);
//					jedis.srem("schoolMsg:idList", schoolMsgId);
//					jedis.del("schoolMsg:" + schoolMsgId);
//					jedis.del("destGrade:" + schoolMsgId);
//					jedis.del("destMajor:" + schoolMsgId);
//					jedis.del("destDepartment:" + schoolMsgId);
//				}

				jedis.sadd("schoolMsg:idList",
						Integer.toString(list.get(i).getSchoolMsgId()));
				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
				map.put("id", Integer.toString(list.get(i).getSchoolMsgId()));
				map.put("title", list.get(i).getTitle());
				map.put("cover", new SchoolMsgService().getCoverPath(list
						.get(i).getContent()));
				map.put("content", list.get(i).getContent());
				map.put("date", list.get(i).getDate().toString());
				map.put("schoolName", list.get(i).getSchoolName());
				map.put("senderName", list.get(i).getSenderName());
				map.put("remarks", list.get(i).getRemarks());
				map.put("destGrade", "destGrade:"
						+ list.get(i).getSchoolMsgId());
				map.put("destMajor", "destMajor:"
						+ list.get(i).getSchoolMsgId());
				map.put("destDepartment", "destDepartment:"
						+ list.get(i).getSchoolMsgId());
				jedis.hmset("schoolMsg:" + list.get(i).getSchoolMsgId(), map);

				for (int n = 0, count2 = list.get(i).getDestGrade().size(); n < count2; n++) {
					jedis.sadd("destGrade:" + list.get(i).getSchoolMsgId(),
							list.get(i).getDestGrade().get(n));
				}

				for (int p = 0, count3 = list.get(i).getDestMajor().size(); p < count3; p++) {
					jedis.sadd("destMajor:" + list.get(i).getSchoolMsgId(),
							list.get(i).getDestMajor().get(p));
				}

				for (int q = 0, count4 = list.get(i).getDestDepartment().size(); q < count4; q++) {
					jedis.sadd(
							"destDepartment:" + list.get(i).getSchoolMsgId(),
							list.get(i).getDestDepartment().get(q));
				}
			}
		} finally {
			pool.returnResource(jedis);
		}

		pool.destroy();
	}

	/**
	 * 当缓存中没有要获取的信息时，将信息取出并放入临时缓存中
	 * 
	 * @param list
	 */
	public void cacheSchoolMsgTemp(List<SchoolMsgVo> list) {

		try {

			for (int i = 0, count = list.size(); i < count; i++) {

				jedis.sadd("temp:schoolMsg:idList",
						Integer.toString(list.get(i).getSchoolMsgId()));
				ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
				map.put("id", Integer.toString(list.get(i).getSchoolMsgId()));
				map.put("title", list.get(i).getTitle());
				map.put("cover", new SchoolMsgService().getCoverPath(list
						.get(i).getContent()));
				map.put("content", list.get(i).getContent());
				map.put("date", list.get(i).getDate().toString());
				map.put("schoolName", list.get(i).getSchoolName());
				map.put("senderName", list.get(i).getSenderName());
				map.put("remarks", list.get(i).getRemarks());
				map.put("destGrade", "temp:destGrade:"
						+ list.get(i).getSchoolMsgId());
				map.put("destMajor", "temp:destMajor:"
						+ list.get(i).getSchoolMsgId());
				map.put("destDepartment", "temp:destDepartment:"
						+ list.get(i).getSchoolMsgId());
				jedis.hmset("temp:schoolMsg:" + list.get(i).getSchoolMsgId(),
						map);

				for (int n = 0, count2 = list.get(i).getDestGrade().size(); n < count2; n++) {
					jedis.sadd(
							"temp:destGrade:" + list.get(i).getSchoolMsgId(),
							list.get(i).getDestGrade().get(n));
				}

				for (int p = 0, count3 = list.get(i).getDestMajor().size(); p < count3; p++) {
					jedis.sadd(
							"temp:destMajor:" + list.get(i).getSchoolMsgId(),
							list.get(i).getDestMajor().get(p));
				}

				for (int q = 0, count4 = list.get(i).getDestDepartment().size(); q < count4; q++) {
					jedis.sadd("temp:destDepartment:"
							+ list.get(i).getSchoolMsgId(), list.get(i)
							.getDestDepartment().get(q));
				}
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();

	}

	/**
	 * 通过msgId从缓存中获取相关信息
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public SchoolMsgVo getFromCache(int schoolMsgId) {

		SchoolMsgVo sMsgVo = new SchoolMsgVo();

		sMsgVo.setSchoolMsgId(schoolMsgId);
		List<String> temp = new ArrayList<String>();

		try {

			// 获取title
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"title");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setTitle(temp.get(q));
			}

			// 获取cover路径
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"cover");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setCoverPath(temp.get(q));
			}

			// 获取content
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"content");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setContent(temp.get(q));
			}

			// 获取date
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"date");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setDate(temp.get(q));
			}

			// 获取schoolName
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"schoolName");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setSchoolName(temp.get(q));
			}

			// 获取senderName
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"senderName");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setSenderName(temp.get(q));
			}

			// 获取remarks
			temp = jedis.hmget("schoolMsg:" + Integer.toString(schoolMsgId),
					"remarks");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setRemarks(temp.get(q));
			}

			// 获取目的年级
			Set<String> gradeSet = jedis.smembers("destGrade:"
					+ Integer.toString(schoolMsgId));
			temp = new ArrayList<String>();
			for (String grade : gradeSet) {
				temp.add(grade);
			}
			sMsgVo.setDestGrade(temp);

			// 获取目的学院
			Set<String> departmentSet = jedis.smembers("destDepartment:"
					+ Integer.toString(schoolMsgId));
			temp = new ArrayList<String>();
			for (String department : departmentSet) {
				temp.add(department);
			}
			sMsgVo.setDestDepartment(temp);

			// 获取目的专业
			Set<String> majorSet = jedis.smembers("destMajor:"
					+ Integer.toString(schoolMsgId));
			temp = new ArrayList<String>();
			for (String major : majorSet) {
				temp.add(major);
			}
			sMsgVo.setDestMajor(temp);

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
		return sMsgVo;
	}

	/**
	 * 通过msgId从临时缓存中获取相关信息
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public SchoolMsgVo getFromTempCache(int schoolMsgId) {
		SchoolMsgVo sMsgVo = new SchoolMsgVo();

		sMsgVo.setSchoolMsgId(schoolMsgId);
		List<String> temp = new ArrayList<String>();

		try {
			// 获取title
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId), "title");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setTitle(temp.get(q));
			}

			// 获取cover路径
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId), "cover");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setCoverPath(temp.get(q));
			}

			// 获取content
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId),
					"content");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setContent(temp.get(q));
			}

			// 获取date
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId), "date");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setDate(temp.get(q));
			}

			// 获取schoolName
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId),
					"schoolName");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setSchoolName(temp.get(q));
			}

			// 获取senderName
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId),
					"senderName");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setSenderName(temp.get(q));
			}

			// 获取remarks
			temp = jedis.hmget(
					"temp:schoolMsg:" + Integer.toString(schoolMsgId),
					"remarks");
			for (int q = 0, count1 = temp.size(); q < count1; q++) {
				sMsgVo.setRemarks(temp.get(q));
			}

			// 获取目的年级
			Set<String> gradeSet = jedis.smembers("temp:destGrade:"
					+ Integer.toString(schoolMsgId));
			temp = new ArrayList<String>();
			for (String grade : gradeSet) {
				temp.add(grade);
			}
			sMsgVo.setDestGrade(temp);

			// 获取目的学院
			Set<String> departmentSet = jedis.smembers("temp:destDepartment:"
					+ Integer.toString(schoolMsgId));
			temp = new ArrayList<String>();
			for (String department : departmentSet) {
				temp.add(department);
			}
			sMsgVo.setDestDepartment(temp);

			// 获取目的专业
			Set<String> majorSet = jedis.smembers("temp:destMajor:"
					+ Integer.toString(schoolMsgId));
			temp = new ArrayList<String>();
			for (String major : majorSet) {
				temp.add(major);
			}
			sMsgVo.setDestMajor(temp);

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();

		return sMsgVo;
	}

	/**
	 * 通过idList从缓存中获取相关信息
	 * 
	 * @param idList
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgFromCache(List<Integer> idList) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();

		List<Integer> idListGetMsgFromDB = new ArrayList<Integer>();

		try {

			// 根据通知id，从缓存或者数据库中取相关数据
			for (int i = 0, count = idList.size(); i < count; i++) {
				int schoolMsgId = idList.get(i);
				if (jedis.sismember("schoolMsg:idList",
						Integer.toString(schoolMsgId))) {
					// 缓存中有通知的具体内容，从缓存中取
					System.out.println("get schoolMsg from cache!");
					SchoolMsgVo sMsgVo = new SchoolMsgVo();
					sMsgVo = this.getFromCache(schoolMsgId);
					list.add(sMsgVo);
				} else if (jedis.sismember("temp:schoolMsg:idList",
						Integer.toString(schoolMsgId))) {
					// 临时缓存中有该数据，则从临时缓存中取
					System.out.println("get schoolMsg from temp cache!");
					SchoolMsgVo sMsgVo = new SchoolMsgVo();
					sMsgVo = this.getFromTempCache(schoolMsgId);
					list.add(sMsgVo);
				} else {
					// 缓存中没有通知的具体内容，从数据库中取
					// System.out.println("schoolMsg no info in cache. get it from DB please!");
					idListGetMsgFromDB.add(schoolMsgId);
				}
			}
			// 将从数据库中取的记录缓存进临时缓存
			List<SchoolMsgVo> schoolMsgToCacheList = new ArrayList<SchoolMsgVo>();
			if (idListGetMsgFromDB.size() > 0) {
				System.out.println("get schoolMsg from MySQL!");
				schoolMsgToCacheList = new SchoolMsgService()
						.getSchoolMsgAll(idListGetMsgFromDB);
				this.cacheSchoolMsgTemp(schoolMsgToCacheList);
				list.addAll(schoolMsgToCacheList);
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();

		return list;
	}

	/**
	 * 每次发送信息时，将receivers写入缓存。
	 * SchoolMsgDao.getMsgIdListtoUpdate获取要更新的信息列表时，不再经过MySQL
	 * 
	 * @param schoolMsgId
	 * @param destUsersId
	 */
	public void cacheSchoolMsg_Receivers(int schoolMsgId,
			List<Integer> destUsersId) {

		try {

			for (int i = 0, count = destUsersId.size(); i < count; i++) {
				String key = "schoolMsg:toReceive:";
				key = key + Integer.toString(destUsersId.get(i));
				jedis.sadd(key, Integer.toString(schoolMsgId));
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 将MySQL中表schoolMsg_receivers中的所有数据写入缓存 一般只在redis清空之后调用一次
	 * 由于表中数据量比较大，一般不要调用...
	 * 
	 * @param map
	 */
	public void cacheSchoolMsg_ReceiversAll(HashMap<Integer, List<Integer>> map) {

		try {

			Iterator<Map.Entry<Integer, List<Integer>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				String key = "schoolMsg:toReceive:";
				Map.Entry<Integer, List<Integer>> entry = (Map.Entry<Integer, List<Integer>>) it
						.next();
				String stuId = Integer.toString(entry.getKey());
				key = key + stuId;
				List<Integer> schoolMsgIdList = entry.getValue();
				for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
					jedis.sadd(key, Integer.toString(schoolMsgIdList.get(i)));
				}
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 缓存每个学生的信息接收列表，用于缓存中没有学生信息接收列表的时候
	 * 
	 * @param stuId
	 * @param list
	 */
	public void cacheSchoolMsg_toReceive(int stuId, List<Integer> list) {
		try {
			String key = "schoolMsg:toReceive:" + stuId;
			int count = list.size();
			for (int i = 0; i < count; i++) {
				jedis.sadd(key, Integer.toString(list.get(i)));
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 客户端返回已接收的确认信息时，将接受者id和接收到的信息的id写入缓存，并将某条信息的接收数+1
	 * 
	 * @param stuId
	 * @param schoolMsgIdList
	 */
	public void cacheSchoolMsg_ReceivedStu(int stuId,
			List<Integer> schoolMsgIdList) {

		try {

			String keyAll = "schoolMsg:received:stuIdAll";
			jedis.sadd(keyAll, Integer.toString(stuId));
			for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
				String key = "schoolMsg:received:";
				key = key + Integer.toString(stuId);
				jedis.sadd(key, Integer.toString(schoolMsgIdList.get(i)));
				jedis.hincrBy("schoolMsg:receivedNum",
						Integer.toString(schoolMsgIdList.get(i)), 1);
			}
		} finally {
			schoolMsgIdList = null;
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 将MySQL中表schoolMsg_receivedStu中的所有数据写入缓存 一般只在redis清空之后调用一次
	 * 
	 * @param map
	 *            存储学生id与已接收信息id的对应关系
	 */
	public void cacheSchoolMsg_ReceivedStuAll(
			HashMap<Integer, List<Integer>> map) {

		try {

			Iterator<Map.Entry<Integer, List<Integer>>> it = map.entrySet()
					.iterator();
			// 此key是为了记录redis中缓存了哪些stuId的已接收列表，为了后续将redis中的数据插入数据库
			String keyAll = "schoolMsg:received:stuIdAll";
			while (it.hasNext()) {
				String key = "schoolMsg:received:";
				Map.Entry<Integer, List<Integer>> entry = (Map.Entry<Integer, List<Integer>>) it
						.next();
				String stuId = Integer.toString(entry.getKey());
				jedis.sadd(keyAll, stuId);
				List<Integer> schoolMsgIdList = entry.getValue();
				key = key + stuId;
				for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
					jedis.sadd(key, Integer.toString(schoolMsgIdList.get(i)));
				}
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 从数据库中取出每条信息的接收数，写入redis，只在redis清空后调用一次 由
	 * SchoolMsgService.cacheSchoolMsgRecievedNum 调用
	 * 
	 * @param map
	 *            存储msgId与接收数
	 */
	public void cacheReceivedNum(HashMap<Integer, Integer> map) {

		try {

			String key = "schoolMsg:receivedNum";
			Iterator<Map.Entry<Integer, Integer>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it
						.next();
				String schoolMsgId = Integer.toString(entry.getKey());
				String receivedNum = Integer.toString(entry.getValue());
				jedis.hset(key, schoolMsgId, receivedNum);
			}

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

}
