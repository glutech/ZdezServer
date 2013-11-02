package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.cache.ZdezMsgCache;
import cn.com.zdez.po.ZdezMsg;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.util.MassInsertion;
import cn.com.zdez.vo.ZdezMsgVo;

public class ZdezMsgDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 根据传入的ZdezMsg实体，向biaozdezMsg中插入一条记录
	 * 
	 * @param zMsg
	 * @return true or false
	 */
	public boolean newZdezMsg(ZdezMsg zMsg) {
		boolean flag = false;
		String sql = "insert into zdezMsg (title, content, adminId) values (?,?,?)";
		Object[] params = { zMsg.getTitle(), zMsg.getContent(),
				zMsg.getAdminId() };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 获取最新一条ZdezMsg信息的id，用于插入与zdezMsg相关的其它表
	 * 
	 * @return the latest zdezMsgId
	 */
	public int getLatestZdezMsgId() {
		int i = 0;
		SQLExecution sqlE = new SQLExecution();
		String sql = "select id from zdezMsg order by date desc limit 0,1";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 向表zdezMsg_destGrade中插入相关记录，用于新建ZdezMsg和重发ZdezMsg
	 * 
	 * @param zdezMsgId
	 * @param grade
	 * @return
	 */
	public boolean newZdezMsg_Grade(int zdezMsgId, String[] grade) {
		boolean flag = false;
		// 因为循环插入数据，所有不使用SQLExecution
		PreparedStatement pstmt = null;
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		String sql = "insert into zdezMsg_destGrade (zdezMsgId, gradeId) values (?,?)";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, zdezMsgId);
			for (int i = 0, count = grade.length; i < count; i++) {
				flag = false;
				pstmt.setInt(2, Integer.parseInt(grade[i]));
				if (pstmt.executeUpdate() > 0) {
					flag = true;
				}
				if (flag == false) {
					break;
				}
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return flag;
	}

	/**
	 * 向表zdezMsg_destMajor中插入相关记录，用于新建ZdezMsg和重发ZdezMsg
	 * 
	 * @param zdezMsgId
	 * @param major
	 * @return
	 */
	public boolean newZdezMsg_Major(int zdezMsgId, String[] major) {
		boolean flag = true;

		String sqlLoadData = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE zdezMsg_destMajor (zdezMsgId, majorId)";

		MassInsertion mi = new MassInsertion();

		List<Integer> destMajor = new ArrayList<Integer>();
		for (int i = 0; i < major.length; i++) {
			destMajor.add(Integer.parseInt(major[i]));
		}

		System.out.println(mi.excuteMassInsertion(sqlLoadData, zdezMsgId,
				destMajor));

		// 因为循环插入数据，所有不使用SQLExecution
		// PreparedStatement pstmt = null;
		// ConnectionFactory factory = ConnectionFactory.getInstatnce();
		// Connection conn = null;
		// String sql =
		// "insert into zdezMsg_destMajor (zdezMsgId, majorId) values (?,?)";
		// try {
		// conn = factory.getConnection();
		// pstmt = conn.prepareStatement(sql);
		// pstmt.setInt(1, zdezMsgId);
		// for (int i = 0, count = major.length; i < count; i++) {
		// flag = false;
		// pstmt.setInt(2, Integer.parseInt(major[i]));
		// if (pstmt.executeUpdate() > 0) {
		// flag = true;
		// }
		// if (flag == false) {
		// break;
		// }
		// }
		// pstmt.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// factory.freeConnection(conn);
		// }
		return flag;
	}

	/**
	 * 向表zdezMsg_receivers中插入相关记录，用于新建ZdezMsg和重发ZdezMsg
	 * 
	 * @param zdezMsgId
	 * @param destUsers
	 * @return
	 */
	public boolean newZdezMsg_Receivers(int zdezMsgId, List<Integer> destUsers) {
		boolean flag = true;

		String sqlLoadData = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE zdezMsg_receivers (zdezMsgId, receiverId)";

		MassInsertion mi = new MassInsertion();
		System.out.println(mi.excuteMassInsertion(sqlLoadData, zdezMsgId,
				destUsers));

		// PreparedStatement pstmt = null;
		// ConnectionFactory factory = ConnectionFactory.getInstatnce();
		// Connection conn = null;
		// String sql =
		// "insert into zdezMsg_receivers (zdezMsgId, receiverId) values (?,?)";
		// try {
		// conn = factory.getConnection();
		// pstmt = conn.prepareStatement(sql);
		// pstmt.setInt(1, zdezMsgId);
		// for (int i = 0, count = destUsers.size(); i < count; i++) {
		// flag = false;
		// pstmt.setInt(2, destUsers.get(i));
		// if (pstmt.executeUpdate() > 0) {
		// flag = true;
		// }
		// if (flag == false) {
		// break;
		// }
		// }
		// pstmt.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// factory.freeConnection(conn);
		// }
		return flag;
	}

	/*
	 * 用于显示找得着信息列表
	 */
	public List<ZdezMsgVo> getZdezMsgToDisplay(List<Integer> zdezMsgIdList) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String sql = "select id, title, date from zdezMsg where id = ?";
		String getReceivedNumSql = "select count(distinct(receivedStuId)) from zdezMsg_receivedStu where zdezMsgId = ?";
		String getReceiverNumSql = "select count(distinct(receiverId)) from zdezMsg_receivers where zdezMsgId = ?";
		try {
			conn = factory.getConnection();
			pstmt1 = conn.prepareStatement(sql);
			pstmt2 = conn.prepareStatement(getReceivedNumSql);
			pstmt3 = conn.prepareStatement(getReceiverNumSql);
			for (int i = 0, count = zdezMsgIdList.size(); i < count; i++) {
				pstmt1.setInt(1, zdezMsgIdList.get(i));
				pstmt2.setInt(1, zdezMsgIdList.get(i));
				pstmt3.setInt(1, zdezMsgIdList.get(i));
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					ZdezMsgVo zMsgVo = new ZdezMsgVo();
					zMsgVo.setZdezMsgId(rs.getInt("id"));
					zMsgVo.setTitle(rs.getString("title"));
					zMsgVo.setDate(rs.getString("date").substring(0, 19));

					// 获取已接收数
					int receivedNum = -1;
					ResultSet rsReceived = pstmt2.executeQuery();
					while (rsReceived.next()) {
						receivedNum = rsReceived.getInt(1);
					}
					zMsgVo.setReceivedNum(receivedNum);

					// 获取发送总数
					int receiverNum = -1;
					ResultSet rsReceiver = pstmt3.executeQuery();
					while (rsReceiver.next()) {
						receiverNum = rsReceiver.getInt(1);
					}
					zMsgVo.setReceiverNum(receiverNum);

					list.add(zMsgVo);
				}
			}
			pstmt1.close();
			pstmt2.close();
			pstmt3.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/*
	 * 查看找得着信息的详细信息
	 */
	public List<ZdezMsgVo> getZdezMsgDetails(List<Integer> zdezMsgIdList) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		String sql = "select id, title, content, date from zdezMsg where id = ?";
		try {
			conn = factory.getConnection();
			pstmt1 = conn.prepareStatement(sql);
			for (int i = 0, count = zdezMsgIdList.size(); i < count; i++) {
				pstmt1.setInt(1, zdezMsgIdList.get(i));
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					ZdezMsgVo zMsgVo = new ZdezMsgVo();
					zMsgVo.setZdezMsgId(rs.getInt("id"));
					zMsgVo.setTitle(rs.getString("title"));
					zMsgVo.setContent(rs.getString("content"));
					zMsgVo.setDate(rs.getString("date").substring(0, 19));

					list.add(zMsgVo);
				}
			}
			pstmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 根据zdezMsgIdList获取对应的ZdezMsgVo 输入idList是为了减少批量获取ZdezMsgVo时频繁的数据库连接
	 * 用于查看ZdezMsg和写入缓存
	 * 
	 * @param zdezMsgIdList
	 * @return
	 */
	public List<ZdezMsgVo> getZdezMsgAll(List<Integer> zdezMsgIdList) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String sql = "select * from zdezMsg where id = ?";
		String getReceivedNumSql = "select count(distinct(receivedStuId)) from zdezMsg_receivedStu where zdezMsgId = ?";
		String getReceiverNumSql = "select count(distinct(receiverId)) from zdezMsg_receivers where zdezMsgId = ?";
		try {
			conn = factory.getConnection();
			pstmt1 = conn.prepareStatement(sql);
			pstmt2 = conn.prepareStatement(getReceivedNumSql);
			pstmt3 = conn.prepareStatement(getReceiverNumSql);
			
			SchoolMsgService smService = new SchoolMsgService();
			
			for (int i = 0, count = zdezMsgIdList.size(); i < count; i++) {
				pstmt1.setInt(1, zdezMsgIdList.get(i));
				ResultSet rs = pstmt1.executeQuery();
				while (rs.next()) {
					ZdezMsgVo zMsgVo = new ZdezMsgVo();
					zMsgVo.setZdezMsgId(rs.getInt("id"));
					zMsgVo.setTitle(rs.getString("title"));
					zMsgVo.setContent(rs.getString("content"));
					zMsgVo.setDate(rs.getString("date").substring(0, 19));
					zMsgVo.setCoverPath(smService.getCoverPath(rs.getString("content")));

					// 获取已接收数

					JedisPool pool = new RedisConnection().getConnection();
					Jedis jedis = pool.getResource();

					try {

						String num = jedis.hget("zdezMsg:receivedNum",
								Integer.toString(zdezMsgIdList.get(i)));

						if (num == null) {

							int receivedNum = 0;
							pstmt2.setInt(1, zdezMsgIdList.get(i));
							ResultSet rsReceived = pstmt2.executeQuery();
							while (rsReceived.next()) {
								receivedNum = rsReceived.getInt(1);
							}
							zMsgVo.setReceivedNum(receivedNum);
							jedis.hset("zdezMsg:receivedNum",
									Integer.toString(zdezMsgIdList.get(i)),
									Integer.toString(receivedNum));
						} else {
							zMsgVo.setReceivedNum(Integer.parseInt(num));
						}

					} finally {
						pool.returnResource(jedis);
					}

					pool.destroy();

					// 获取发送总数
					int receiverNum = -1;
					pstmt3.setInt(1, zdezMsgIdList.get(i));
					ResultSet rsReceiver = pstmt3.executeQuery();
					while (rsReceiver.next()) {
						receiverNum = rsReceiver.getInt(1);
					}
					zMsgVo.setReceiverNum(receiverNum);

					list.add(zMsgVo);
				}
			}
			if (pstmt1 != null) {
				pstmt1.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
			if (pstmt3 != null) {
				pstmt3.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 将表zdezMsg_receivedStu 中的数据存入hashmap中，用于缓存。 仅在redis清空之后调用一次
	 * 
	 * @return
	 */
	public HashMap<Integer, List<Integer>> getZdezMsgReceivedAll() {
		HashMap<Integer, List<Integer>> destMap = new HashMap<Integer, List<Integer>>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select distinct(receivedStuId) from zdezMsg_receivedStu";
		String sql1 = "select distinct(zdezMsgId) from zdezMsg_receivedStu where receivedStuId = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int key = rs.getInt(1);
				List<Integer> tempList = new ArrayList<Integer>();
				pstmt1.setInt(1, key);
				ResultSet rs1 = pstmt1.executeQuery();
				while (rs1.next()) {
					tempList.add(rs1.getInt(1));
				}
				destMap.put(key, tempList);
			}
			pstmt.close();
			pstmt1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return destMap;
	}

	/**
	 * 获取zdezMsg_receivers表中stuId的数量，用户分段缓存
	 * 
	 * @return
	 */
	public int getReceiversCount() {
		int i = 0;
		String sql = "select count(distinct(receiverId)) from zdezMsg_receivers";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 将表zdezMsg_receivers 中的数据存入hashmap中，用于缓存。 仅在redis清空之后调用一次
	 * 为了减少数据库连接，没有严格符合的MVC。
	 * 
	 * @return
	 */
	public HashMap<Integer, List<Integer>> cacheZdezMsgReceiverAll() {
		HashMap<Integer, List<Integer>> destMap = new HashMap<Integer, List<Integer>>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select distinct(receiverId) from zdezMsg_receivers limit ?, ?";
		String sql1 = "select distinct(zdezMsgId) from zdezMsg_receivers where receiverId = ?";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);

			ZdezMsgCache cache = new ZdezMsgCache();
			int idCount = this.getReceiversCount();
			int mod = idCount % 200;
			int count = idCount / 200;
			for (int i = 1; i <= count; i++) {
				int start = (i - 1) * 200;
				int end = i * 200;
				pstmt.setInt(1, start);
				pstmt.setInt(2, end);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					int key = rs.getInt(1);
					List<Integer> tempList = new ArrayList<Integer>();
					pstmt1.setInt(1, key);
					ResultSet rs1 = pstmt1.executeQuery();
					while (rs1.next()) {
						tempList.add(rs1.getInt(1));
					}
					destMap.put(key, tempList);
				}
				cache.cacheZdezMsg_ReceiversAll(destMap);
			}
			if (mod != 0) {
				int start = count * 200;
				int end = start + mod;
				pstmt.setInt(1, start);
				pstmt.setInt(2, end);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					int key = rs.getInt(1);
					List<Integer> tempList = new ArrayList<Integer>();
					pstmt1.setInt(1, key);
					ResultSet rs1 = pstmt1.executeQuery();
					while (rs1.next()) {
						tempList.add(rs1.getInt(1));
					}
					destMap.put(key, tempList);
				}
				cache.cacheZdezMsg_ReceiversAll(destMap);
			}
			pstmt.close();
			pstmt1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return destMap;
	}

	/**
	 * 根据zdezMsgId删除zdezMsg_destGrade表中相关记录 用于新建ZdezMsg过程中出现错误，进行回滚操作，防止冗余数据
	 * 
	 * @param latestzdezMsgId
	 * @return
	 */
	public boolean roll_Back_DestGrade(int latestzdezMsgId) {
		boolean flag = false;
		String sql = "delete from zdezMsg_destGrade where zdezMsgId=?";
		Object[] params = { latestzdezMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 根据zdezMsgId删除zdezMsg_destMajor表中相关记录 用于新建ZdezMsg过程中出现错误，进行回滚操作，防止冗余数据
	 * 
	 * @param latestzdezMsgId
	 * @return
	 */
	public boolean roll_Back_DestMajor(int latestzdezMsgId) {
		boolean flag = false;
		String sql = "delete from zdezMsg_destMajor where zdezMsgId=?";
		Object[] params = { latestzdezMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 根据zdezMsgId删除zdezMsg_receivers表中相关记录 用于新建ZdezMsg过程中出现错误，进行回滚操作，防止冗余数据
	 * 
	 * @param latestzdezMsgId
	 * @return
	 */
	public boolean roll_Back_Receivers(int latestzdezMsgId) {
		boolean flag = false;
		String sql = "delete from zdezMsg_receivers where zdezMsgId=?";
		Object[] params = { latestzdezMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 根据zdezMsgId删除zdezMsg表中相关记录 用于新建ZdezMsg过程中出现错误，进行回滚操作，防止冗余数据
	 * 
	 * @param latestzdezMsgId
	 * @return
	 */
	public boolean roll_Back_ZdezMsg(int latestZdezMsgId) {
		boolean flag = false;
		String sql = "delete from zdezMsg where id=?";
		Object[] params = { latestZdezMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 新建ZdezMsg过程中出现错误，进行回滚，防止数据库冗余数据
	 * 
	 * @param latestZdezMsgId
	 * @return
	 */
	public boolean roll_Back(int latestZdezMsgId) {
		boolean flag = false;
		if (this.roll_Back_DestGrade(latestZdezMsgId)
				&& this.roll_Back_DestMajor(latestZdezMsgId)
				&& this.roll_Back_Receivers(latestZdezMsgId)
				&& this.roll_Back_ZdezMsg(latestZdezMsgId)) {
			// 回滚过程无错误
			flag = true;
		} else {
			// 回滚过程出错
			// do nothing.
		}
		return flag;
	}

	/**
	 * 根据学生id获取需要更新的ZdezMsgVo
	 * 
	 * @param stuId
	 * @return
	 */
	public List<ZdezMsgVo> getMsgToUpdate(int stuId) {
		List<ZdezMsgVo> list = new ArrayList<ZdezMsgVo>();

		// 根据用户id获取需要更新的通知id列表
		List<Integer> idList = this.getMsgIdListtoUpdate(stuId);

		// 根据通知id，从缓存或者数据库中取相关数据
		list = new ZdezMsgCache().getZdezMsgFromCache(idList);
		return list;
	}

	/**
	 * 根据学生id获取需要更新的ZdezMsg的id列表
	 * 
	 * @param stuId
	 * @return
	 */
	// public List<Integer> getMsgIdListtoUpdate(int stuId) {
	// List<Integer> toReceive = new ArrayList<Integer>();
	// List<Integer> received = new ArrayList<Integer>();
	// ConnectionFactory factory = ConnectionFactory.getInstatnce();
	// PreparedStatement pstmt1 = null;
	// PreparedStatement pstmt2 = null;
	// Connection conn = null;
	//
	// String sqlToReceive =
	// "select zdezMsgId from zdezMsg_receivers where receiverId = ? order by zdezMsgId desc";
	// String sqlReceived =
	// "select zdezMsgId from zdezMsg_receivedStu where receivedStuId = ? order by zdezMsgId desc";
	//
	// try {
	// conn = factory.getConnection();
	//
	// // 获取某一学生要接收的通知列表
	// pstmt1 = conn.prepareStatement(sqlToReceive);
	// pstmt1.setInt(1, stuId);
	// ResultSet rsIdToReceive = pstmt1.executeQuery();
	// while (rsIdToReceive.next()) {
	// toReceive.add(rsIdToReceive.getInt(1));
	// }
	//
	// // 获取某一学生已接收的通知id列表
	// pstmt2 = conn.prepareStatement(sqlReceived);
	// pstmt2.setInt(1, stuId);
	// ResultSet rsIdReceived = pstmt2.executeQuery();
	// while (rsIdReceived.next()) {
	// received.add(rsIdReceived.getInt(1));
	// }
	//
	// toReceive.removeAll(received);
	//
	// pstmt1.close();
	// pstmt2.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// factory.freeConnection(conn);
	// }
	//
	// return toReceive;
	// }
	public List<Integer> getMsgIdListtoUpdate(int stuId) {
		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();
		List<Integer> toReceive = new ArrayList<Integer>();
		String key1 = "zdezMsg:toReceive:" + Integer.toString(stuId);
		String key2 = "zdezMsg:received:" + Integer.toString(stuId);

		try {

			List<Integer> msgIdList = new ArrayList<Integer>();
			ZdezMsgCache cache = new ZdezMsgCache();

			// 缓存中没有数据时，从数据库中获取并写入缓存
			// 以未拉取过数据为准
			
			Set<String> receivedSet = jedis.smembers(key2);
			if (receivedSet.size() == 0) {
				msgIdList = this.getReceivedMsgIdsByStuId(stuId);
				cache.cacheZdezMsg_ReceivedStu(stuId, msgIdList);
				msgIdList = this.getToReceiveMsgIdsByStuId(stuId);
				cache.cacheZdezMsg_toReceive(stuId, msgIdList);
			}
			
			// 对比获得要更新的信息id
			
			Set<String> toReceivedSet = jedis.sdiff(key1, key2);
			Iterator<String> it = toReceivedSet.iterator();
			while (it.hasNext()) {
				String str = it.next();
				toReceive.add(Integer.parseInt(str));
			}

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
		return toReceive;
	}
	
	/**
	 * 获取某个学生的待接收信息id列表，用于缓存
	 * @param stuId
	 * @return
	 */
	public List<Integer> getToReceiveMsgIdsByStuId(int stuId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select zdezMsgId from zdezMsg_receivers where receiverId = ? order by zdezMsgId desc";
		Object[] params = {stuId};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取某个学生的已接收信息列表，用于缓存
	 * @param stuId
	 * @return
	 */
	public List<Integer> getReceivedMsgIdsByStuId(int stuId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select zdezMsgId from zdezMsg_receivedStu where receivedStuId = ? order by zdezMsgId desc";
		Object[] params = {stuId};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据学生id和ZdezIdList向表zdez_receivedStu中写入数据，表明该学生已经收到了哪些信息
	 * 会产生重复记录，这个问题还要处理（先判断是不是已经有某项stuId-zdezMsgId对）
	 * 
	 * @param stuId
	 * @param zdezMsgIdList
	 */
	// public void updateZdezMsgReceived(int stuId, List<Integer> zdezMsgIdList)
	// {
	// ConnectionFactory factory = ConnectionFactory.getInstatnce();
	// PreparedStatement pstmt = null;
	// PreparedStatement pstmt1 = null;
	// Connection conn = null;
	// try {
	// conn = factory.getConnection();
	// String sql =
	// "insert into zdezMsg_receivedStu (zdezMsgId, receivedStuId) values (?,?)";
	// String sqlIsReceived =
	// "select * from zdezMsg_receivedStu where zdezMsgId = ? and receivedStuId = ?";
	// pstmt = conn.prepareStatement(sql);
	// pstmt1 = conn.prepareStatement(sqlIsReceived);
	// pstmt.setInt(2, stuId);
	// pstmt1.setInt(2, stuId);
	// for (int i = 0, count = zdezMsgIdList.size(); i < count; i++) {
	// pstmt.setInt(1, zdezMsgIdList.get(i));
	// pstmt1.setInt(1, zdezMsgIdList.get(i));
	// ResultSet rs = pstmt1.executeQuery();
	// if (!rs.next()) {
	// pstmt.executeUpdate();
	// }
	// }
	// pstmt.close();
	// pstmt1.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// factory.freeConnection(conn);
	// }
	// }

	public void updateZdezMsgReceived(int stuId, List<Integer> zdezMsgIdList) {
		new ZdezMsgCache().cacheZdezMsg_ReceivedStu(stuId, zdezMsgIdList);
	}

	/**
	 * 获取ZdezMsg信息数量，用于显示所有ZdezMsg列表时进行分页与计数
	 * 
	 * @return
	 */
	public int getZdezMsgCount() {
		int i = -1;
		String sql = "select count(id) from zdezMsg";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 根据传入的分页起始与结束记录位置，获取对应范围内的ZdezMsgIds
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Integer> getZdezMsgIdList(int start, int end) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from zdezMsg order by date desc limit ?,?";
		Object[] params = { start, end };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取符合某一查询关键字的ZdezMsg记录条数，用于分页
	 * 
	 * @param keyword
	 * @return
	 */
	public int getZdezMsgQueryCount(String keyword) {
		int i = -1;
		String sql = "select count(id) from zdezMsg where title like '%"
				+ keyword + "%'";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				i = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 根据分页起始与结束位置和查询关键字，获取符合所有条件的ZdezMsgIds
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<Integer> getZdezMsgIdList(int start, int end, String keyword) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from zdezMsg where title like '%" + keyword
				+ "%' order by date desc limit " + start + "," + end + "";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过zdezMsgId获取ZdezMsg实体
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public ZdezMsg getZdezMsgById(int zdezMsgId) {
		ZdezMsg sMsg = new ZdezMsg();
		String sql = "select * from zdezMsg where id=?";
		Object[] params = { zdezMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				sMsg.setId(rs.getInt("id"));
				sMsg.setTitle(rs.getString("title"));
				sMsg.setContent(rs.getString("content"));
				sMsg.setDate(rs.getString("date"));
				sMsg.setAdminId(rs.getString("adminId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sMsg;
	}

	/**
	 * 根据zdezMsgId获取该信息是发给哪些年级的，用于信息重发
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public List<Integer> getGradeIdListByMsgId(int zdezMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select distinct(gradeId) from zdezMsg_destGrade where zdezMsgId=?";
		Object[] params = { zdezMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				int temp = rs.getInt("gradeId");
				list.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过zdezMsgId获取该信息是发给哪些专业的，用于信息重发
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public List<Integer> getMajorIdListByMsgId(int zdezMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select distinct(majorId) from zdezMsg_destMajor where zdezMsgId=?";
		Object[] params = { zdezMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				int temp = rs.getInt("majorId");
				list.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过zdezMsgId获取该信息是发给哪些用户的，用于信息重发
	 * 
	 * @param zdezMsgId
	 * @return
	 */
	public List<Integer> getDestUsersListByMsgId(int zdezMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select * from zdezMsg_receivers where zdezMsgId=?";
		Object[] params = { zdezMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt("receiverId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将有关zdezMsg_receivedStu的数据从redis中取出，并写入MySQL中
	 * 
	 * @return
	 */
	public boolean writeIntoZdezMsg_ReceivedStu() {
		boolean flag = false;

		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String sql1 = "select id from zdezMsg_receivedStu where zdezMsgId = ? and receivedStuId = ?";
		String sql2 = "insert into zdezMsg_receivedStu (zdezMsgId, receivedStuId) value (?, ?)";

		try {
			conn = factory.getConnection();
			Set<String> stuIdList = jedis.smembers("zdezMsg:received:stuIdAll");
			Iterator<String> it = stuIdList.iterator();
			while (it.hasNext()) {
				int stuId = Integer.parseInt(it.next());
				String key = "zdezMsg:received:" + stuId;
				Set<String> schoolMsgIdList = jedis.smembers(key);
				Iterator<String> itSchoolMsgId = schoolMsgIdList.iterator();
				while (itSchoolMsgId.hasNext()) {
					int schoolMsgId = Integer.parseInt(itSchoolMsgId.next());
					pstmt1 = conn.prepareStatement(sql1);
					pstmt1.setInt(1, schoolMsgId);
					pstmt1.setInt(2, stuId);
					ResultSet rs1 = pstmt1.executeQuery();
					if (!rs1.next()) {
						pstmt2 = conn.prepareStatement(sql2);
						pstmt2.setInt(1, schoolMsgId);
						pstmt2.setInt(2, stuId);
						if (pstmt2.executeUpdate() > 0) {
							flag = true;
						} else {
							break;
						}
					}
				}
			}
			if (pstmt1 != null) {
				pstmt1.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
			pool.returnResource(jedis);
		}
		pool.destroy();
		return flag;
	}

	/**
	 * 统计找得着信息已接收数，只在缓存清空之后执行一次
	 * 
	 * @return
	 */
	public HashMap<Integer, Integer> getRecievedNum() {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String sql1 = "select distinct(zdezMsgId) from zdezMsg_receivedStu";
		String sql2 = "select count(receivedStuId) from zdezMsg_receivedStu where zdezMsgId = ?";
		try {
			conn = factory.getConnection();
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, rs1.getInt(1));
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					map.put(rs1.getInt(1), rs2.getInt(1));
				}
			}
			if (pstmt1 != null) {
				pstmt1.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return map;
	}

}
