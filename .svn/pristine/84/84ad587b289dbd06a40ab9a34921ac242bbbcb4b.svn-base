package cn.com.zdez.service;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.bgRunning.NewZdezMsg;
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
			if (dao.newZdezMsg_Grade(zdezMsgId, grade)
					&& dao.newZdezMsg_Major(zdezMsgId, major)
					&& dao.newZdezMsg_Receivers(zdezMsgId, destUsers)) {

				// 将内容写入html文件，用于网页显示
				if (new ContentOperation().SaveContent("zdezMsg", zdezMsgId,
						zMsg.getContent(), rootPath)) {
					flag = true;
				}
				
				NewZdezMsg n = new NewZdezMsg(zdezMsgId);
				Thread thread = new Thread(n);
				thread.start();

//				List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
//				List<Integer> zdezMsgIdList = new ArrayList<Integer>();
//				zdezMsgIdList.add(zdezMsgId);
//				list = dao.getZdezMsgAll(zdezMsgIdList);
//
//				// 给微软服务器发送
//
//				// 缓存
//				new ZdezMsgCache().cacheZdezMsg(list);
			} else {
				// 数据插入过程出错，进行错误信息的删除
				dao.roll_Back(zdezMsgId);
			}
		}
		return flag;
	}

	/**
	 * 获取要更新的zdezMsg列表
	 * 
	 * @param stuId
	 * @return
	 */
	public synchronized List<ZdezMsgVo> getMsgToUpdate(int stuId) {
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

}
