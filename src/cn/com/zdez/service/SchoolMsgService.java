package cn.com.zdez.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.bgRunning.NewSchoolMsg;
import cn.com.zdez.cache.SchoolMsgCache;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.dao.SchoolMsgDao;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.util.ContentOperation;
import cn.com.zdez.vo.SchoolMsgVo;

import com.notnoop.mpns.sendnoti.SendToastNotification;

public class SchoolMsgService {

	SchoolMsgDao dao = new SchoolMsgDao();

	/**
	 * 学校发送信息时，向表schoolMsg、schoolMsg_destGrade、schoolMsg_destMajor、
	 * schoolMsg_receivers插入相应的数据
	 * 若在数据写入过程中出现错误，则将所有已插入的数据（数据无用）删除，使数据库恢复到此次发送校园通知之前的状态
	 * 
	 * @param msg
	 * @param grade
	 * @param major
	 * @param destUsers
	 * @return true or false
	 */
	public synchronized boolean newSchoolMsg(SchoolMsg msg, String[] grade,
			String[] major, List<Integer> destUsers, String rootPath) {
		boolean flag = false;
		if (dao.newSchoolMsg(msg)) {
			// 若表schoolMsg信息写入成功
			// 获取刚插入的校园通知的id
			int schoolMsgId = dao.getLatestSchoolMsgId();
			// 进行信息关联表的数据写入
			if (dao.newSchoolMsg_Grade(schoolMsgId, grade) == true
					&& dao.newSchoolMsg_Major(schoolMsgId, major) == true
					&& dao.newSchoolMsg_Receivers(schoolMsgId, destUsers) == true) {
				// 信息写入数据库成功

				// 将内容写入html文件，用于网页显示
				if (new ContentOperation().SaveContent("schoolMsg",
						schoolMsgId, msg.getContent(), rootPath)) {
					flag = true;
				}
				
				NewSchoolMsg n = new NewSchoolMsg(msg, schoolMsgId, destUsers);
				Thread thread = new Thread(n);
				thread.start();

//				List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
//				List<Integer> schoolMsgIdList = new ArrayList<Integer>();
//				schoolMsgIdList.add(schoolMsgId);
//				list = dao.getSchoolMsgAll(schoolMsgIdList);
//
//				// 给微软服务器发送
//				List<Student> stuList = new StudentService()
//						.getStudentByIdList(destUsers);
//				for (Student stu : stuList) {
//					for (SchoolMsgVo sMsgVo : list) {
//						if (stu.getStaus().contains("http://")) {
//							new SchoolMsgService().sendMsgToWP(
//									stu.getUsername(), sMsgVo, stu.getStaus());
//						} else {
//							// for iOS
//						}
//					}
//				}
//
//				// 写入Redis缓存
//				SchoolMsgCache cache = new SchoolMsgCache();
//				cache.cacheSchoolMsg(list, msg.getSchoolAdminUsername());
//				cache.cacheSchoolMsg_Receivers(schoolMsgId, destUsers);
			} else {
				// 信息写入失败，回滚
				// roll back.
				dao.roll_Back(schoolMsgId);
			}

		} else {
			// 如果数据写入失败
			// do nothing.
		}
		return flag;
	}

	/**
	 * 获取公司管理员发送通知的数量
	 * 
	 * @param
	 * @return
	 */
	public int getSchoolMsgCount() {
		return dao.getSchoolMsgCount();
	}

	/**
	 * 获取某一学校管理员查询的发送消息的数量
	 * 
	 * @param sAdmin
	 * @param keyword
	 * @return
	 */
	public int getSchoolMsgQueryCount(SchoolAdmin sAdmin, String keyword) {
		return dao.getSchoolMsgQueryCount(sAdmin, keyword);
	}

	/**
	 * 获取学校管理员发送消息的数量，公司管理员查询时用
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSchoolMsgQueryCount(String keyword) {
		return dao.getSchoolMsgQueryCount(keyword);
	}

	/**
	 * 获取某一学校管理员发送信息的数量
	 * 
	 * @param sAdmin
	 * @return
	 */
	public int getSchoolMsgCount(SchoolAdmin sAdmin) {
		return dao.getSchoolMsgCount(sAdmin);
	}

	/**
	 * 获得学校已发的通知，给公司管理员使用
	 * 
	 * @param
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgByPage(int start, int end) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
		List<Integer> schoolMsgIdList = dao.getSchoolMsgIdList(start, end);
//		list = new SchoolMsgCache().getSchoolMsgFromCache(schoolMsgIdList);
//		list = dao.getSchoolMsgAll(schoolMsgIdList);
		list = dao.getMsgToDisplayAdmin(schoolMsgIdList);
		return list;
	}

	/**
	 * 获得某一管理员已发通知，学校管理员使用
	 * 
	 * @param start
	 * @param end
	 * @param sAdmin
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgByPage(int start, int end,
			SchoolAdmin sAdmin) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
		List<Integer> schoolMsgIdList = dao.getSchoolMsgIdList(start, end,
				sAdmin);
//		list = new SchoolMsgCache().getSchoolMsgFromCache(schoolMsgIdList);
//		list = dao.getSchoolMsgAll(schoolMsgIdList);
		list = dao.getMsgToDisplaySchoolAdmin(schoolMsgIdList);
		return list;
	}

	/**
	 * 获得某一管理员已发通知，学校管理员使用，查询用
	 * 
	 * @param start
	 * @param end
	 * @param sAdmin
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgQueryByPage(int start, int end,
			SchoolAdmin sAdmin, String keyword) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
		List<Integer> schoolMsgIdList = dao.getSchoolMsgIdList(start, end,
				sAdmin, keyword);
//		list = dao.getSchoolMsgAll(schoolMsgIdList);
		list = dao.getMsgToDisplaySchoolAdmin(schoolMsgIdList);
		return list;
	}

	/**
	 * 公司管理员搜索学校信息时，按照分页和关键字获得要显示的学校通知
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgQueryByPage(int start, int end,
			String keyword) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();
		List<Integer> schoolMsgIdList = dao.getSchoolMsgIdList(start, end,
				keyword);
//		list = dao.getSchoolMsgAll(schoolMsgIdList);
		list = dao.getMsgToDisplayAdmin(schoolMsgIdList);
		return list;
	}

	/**
	 * 根据用户id获取需要更新的通知
	 * 
	 * @param stuId
	 * @return
	 */
	public List<SchoolMsgVo> getMsgToUpdate(int stuId) {
		return dao.getMsgToUpdate(stuId);
	}

	/**
	 * 通过通知id列表获取通知的详细信息
	 * 
	 * @param schoolMsgIdList
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgAll(List<Integer> schoolMsgIdList) {
		return dao.getSchoolMsgAll(schoolMsgIdList);
	}

	/**
	 * 通过通知id获得通知内容
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public SchoolMsg getSchoolMsgById(int schoolMsgId) {
		return dao.getSchoolMsgById(schoolMsgId);
	}

	/**
	 * 根据通知id获得通知的目的年级
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getGradeIdListByMsgId(int schoolMsgId) {
		return dao.getGradeIdListByMsgId(schoolMsgId);
	}

	/**
	 * 根据通知id获得通知的目的专业
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getMajorIdListByMsgId(int schoolMsgId) {
		return dao.getMajorIdListByMsgId(schoolMsgId);
	}

	/**
	 * 根据通知id获取接受者列表
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getDestUsersListByMsgId(int schoolMsgId) {
		return dao.getDestUsersListByMsgId(schoolMsgId);
	}

	/**
	 * 根据客户端返回的学生id和已收到的通知id列表，更新schoolMsg_received表
	 * 
	 * @param stuId
	 * @param schoolMsgIdList
	 */
	public void updateSchoolMsgReceived(int stuId, String[] schoolMsgIdArray) {
		List<Integer> schoolMsgIdList = new ArrayList<Integer>();
		for (int i = 0, count = schoolMsgIdArray.length; i < count; i++) {
			System.out.println(schoolMsgIdArray[i]);
			schoolMsgIdList.add(Integer.parseInt(schoolMsgIdArray[i]));
		}
		dao.updateSchoolMsgReceived(stuId, schoolMsgIdList);
	}

	/**
	 * 如果通知内容中有图片，则把第一张图片作为通知的封面，用于在客户端信息列表显示
	 * 
	 * @param schoolMsgContent
	 * @return
	 */
	public String getCoverPath(String schoolMsgContent) {
		String coverPath = "";
		Document doc = Jsoup.parse(schoolMsgContent);
		Elements imgs = doc.getElementsByTag("img");
		if (!imgs.isEmpty()) {
			Element img = imgs.first();
			coverPath = img.attr("src");
		}

		return coverPath;
	}

	/**
	 * 通过消息id获得消息的内容，先从缓存中取，缓存中没有则从数据库中取
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public String getSchoolMsgContentById(int schoolMsgId) {
		String content = "";
		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();

		try {

			List<String> temp = jedis.hmget(
					"schoolMsg:" + Integer.toString(schoolMsgId), "content");
			if (temp.size() == 0) {
				System.out
						.println("no cache in Redis! get it from MySQL please.");
			} else {
				for (String str : temp) {
					content = str;
				}
			}
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
		return content;
	}

	/**
	 * @param username
	 *            as user's name
	 * @param schoolMsgVo
	 *            as the message that need to send
	 * @param status
	 *            as the WP user's URI channel
	 * @function sending message to the WP user
	 * */
	public void sendMsgToWP(String username, SchoolMsgVo schoolMsgVo,
			String status) {
		SendToastNotification sendToastNotification = new SendToastNotification();
		schoolMsgVo
				.setContent(schoolMsgVo.getContent().replaceAll("\r\n", " "));
		if (schoolMsgVo.getContent().length() > 34) {
			sendToastNotification.pushShortFreeNotification(status, username,
					String.valueOf(schoolMsgVo.getSchoolMsgId()),
					schoolMsgVo.getSenderName(), schoolMsgVo.getDate(),
					schoolMsgVo.getContent().substring(0, 34),
					schoolMsgVo.getTitle());
		} else {
			sendToastNotification.pushShortFreeNotification(status, username,
					String.valueOf(schoolMsgVo.getSchoolMsgId()),
					schoolMsgVo.getSenderName(), schoolMsgVo.getDate(),
					schoolMsgVo.getContent(), schoolMsgVo.getTitle());
		}
	}

	/**
	 * 缓存schoolMsg_receivers和schoolMsg_receivedStu两张表中的数据。 只有在redis清空之后才调用一次
	 */
	public void cacheReceiversAndReceived() {
		SchoolMsgCache cache = new SchoolMsgCache();
		HashMap<Integer, List<Integer>> map = dao.getSchoolMsgReceivedAll();
		cache.cacheSchoolMsg_ReceivedStuAll(map);
		map = dao.getSchoolMsgReceiverAll();
		cache.cacheSchoolMsg_ReceiversAll(map);
	}

	/**
	 * 将有关schoolMsg_receivedStu的数据从redis中取出，并写入MySQL中
	 * 
	 * @return
	 */
	public boolean writeIntoSchoolMsg_ReceivedStu() {
		return dao.writeIntoSchoolMsg_ReceivedStu();
	}

	/**
	 * 统计校园通知已接收数，只在缓存(redis)清空之后执行一次
	 * 
	 * @return
	 */
	public void cacheSchoolMsgRecievedNum() {
		HashMap<Integer, Integer> map = dao.getRecievedNum();
		new SchoolMsgCache().cacheReceivedNum(map);
	}

}
