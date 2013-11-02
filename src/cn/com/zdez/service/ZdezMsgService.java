package cn.com.zdez.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.zdez.bgRunning.NewZdezMsg;
import cn.com.zdez.cache.ZdezMsgCache;
import cn.com.zdez.dao.ZdezMsgDao;
import cn.com.zdez.po.ZdezMsg;
import cn.com.zdez.util.ContentOperation;
import cn.com.zdez.vo.ZdezMsgVo;

public class ZdezMsgService {

	ZdezMsgDao dao = new ZdezMsgDao();

	/**
	 * 新建zdezMsg，涉及相关表格数据的插入以及将content单独存储为一个html文件，便于网页短显示
	 * 
	 * @param zMsg
	 * @param grade
	 * @param major
	 * @param destUsers
	 * @param rootPath
	 * @return
	 */
	public synchronized boolean newZdezMsg(ZdezMsg zMsg, String[] grade,
			String[] major, List<Integer> destUsers, String rootPath) {
		boolean flag = false;
		if (dao.newZdezMsg(zMsg)) {
			int zdezMsgId = dao.getLatestZdezMsgId();
			// 将内容写入html文件，用于网页显示
			if (new ContentOperation().SaveContent("zdezMsg", zdezMsgId,
					zMsg.getContent(), rootPath)) {
				flag = true;
			}
			NewZdezMsg n = new NewZdezMsg(zdezMsgId, destUsers, grade, major);
			Thread thread = new Thread(n);
			thread.start();
		}
		return flag;
	}

	public boolean newZdezMsg_Grade(int zdezMsgId, String[] grade) {
		return dao.newZdezMsg_Grade(zdezMsgId, grade);
	}

	public boolean newZdezMsg_Major(int zdezMsgId, String[] major) {
		return dao.newZdezMsg_Major(zdezMsgId, major);
	}

	public boolean newZdezMsg_Receivers(int zdezMsgId, List<Integer> destUsers) {
		return dao.newZdezMsg_Receivers(zdezMsgId, destUsers);
	}

	/**
	 * 获取要更新的zdezMsg列表
	 * 
	 * @param stuId
	 * @return
	 */
	public List<ZdezMsgVo> getMsgToUpdate(int stuId) {
		return dao.getMsgToUpdate(stuId);
	}

	/**
	 * 更新信息接收列表，记录某一账户已经收到的信息
	 * 
	 * @param stuId
	 * @param zdezMsgIdArray
	 */
	public void updateZdezMsgReceived(int stuId, String[] zdezMsgIdArray) {
		List<Integer> zdezMsgIdList = new ArrayList<Integer>();
		for (int i = 0, count = zdezMsgIdArray.length; i < count; i++) {
			zdezMsgIdList.add(Integer.parseInt(zdezMsgIdArray[i]));
		}
		dao.updateZdezMsgReceived(stuId, zdezMsgIdList);
	}

	/**
	 * 根据idList获取信息的相关内容
	 * 
	 * @param zdezMsgIdList
	 * @return
	 */
	public List<ZdezMsgVo> getZdezMsgAll(List<Integer> zdezMsgIdList) {
		return dao.getZdezMsgAll(zdezMsgIdList);
	}

	/**
	 * 获取信息数量，用于分页
	 * 
	 * @return
	 */
	public int getZdezMsgCount() {
		return dao.getZdezMsgCount();
	}

	/**
	 * 按照页码获取对应的信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<ZdezMsgVo> getZdezMsgByPage(int start, int end) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		List<Integer> idList = dao.getZdezMsgIdList(start, end);
		list = dao.getZdezMsgAll(idList);
		return list;
	}

	public List<ZdezMsgVo> getZdezMsgDetails(List<Integer> zdezMsgIdList) {
		return dao.getZdezMsgDetails(zdezMsgIdList);
	}

	/**
	 * 根据查询关键字，返回符合查询条件的记录数，用于分页
	 * 
	 * @param keyword
	 * @return
	 */
	public int getZdezMsgQueryCount(String keyword) {
		return dao.getZdezMsgQueryCount(keyword);
	}

	/**
	 * 根据查询关键字和分页返回应对的记录
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<ZdezMsgVo> getZdezMsgQueryByPage(int start, int end,
			String keyword) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		List<Integer> idList = dao.getZdezMsgIdList(start, end, keyword);
		list = dao.getZdezMsgAll(idList);
		return list;
	}

	/**
	 * 通过msgId获取信息的内容
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public ZdezMsg getZdezMsgById(int zdezMsgId) {
		return dao.getZdezMsgById(zdezMsgId);
	}

	/**
	 * 通过msgId获取目标年级，用于信息的重发
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public List<Integer> getGradeIdListByMsgId(int zdezMsgId) {
		return dao.getGradeIdListByMsgId(zdezMsgId);
	}

	/**
	 * 通过msgId获取目的专业，用于信息的重发
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public List<Integer> getMajorIdListByMsgId(int zdezMsgId) {
		return dao.getMajorIdListByMsgId(zdezMsgId);
	}

	/**
	 * 通过msgId获取目标用户，用于信息的重发
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public List<Integer> getDestUsersListByMsgId(int zdezMsgId) {
		return dao.getDestUsersListByMsgId(zdezMsgId);
	}

	/**
	 * 将有关zdezMsg_receivedStu的数据从redis中取出，并写入MySQL中，用于数据同步
	 * 
	 * @return
	 */
	public boolean writeIntoZdezMsg_ReceivedStu() {
		return dao.writeIntoZdezMsg_ReceivedStu();
	}

	/**
	 * 统计找得着信息已接收数，将数据写入缓存，只在redis清空后执行一次
	 */
	public void cacheZdezMsgReceivedNum() {
		HashMap<Integer, Integer> map = dao.getRecievedNum();
		new ZdezMsgCache().cacheReceivedNum(map);
	}

}
