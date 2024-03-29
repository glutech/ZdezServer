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
import java.util.TreeSet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.cache.SchoolMsgCache;
import cn.com.zdez.cache.SchoolStudentCache;
import cn.com.zdez.po.SchoolAdmin;
import cn.com.zdez.po.SchoolMsg;
import cn.com.zdez.util.MassInsertion;
import cn.com.zdez.util.Statistics;
import cn.com.zdez.vo.SchoolMsgVo;

public class SchoolMsgDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 学校发送信息时向表schoolMsg表中插入一条记录
	 * 
	 * @param msg
	 * @return true or false
	 */
	public boolean newSchoolMsg(SchoolMsg msg) {
		boolean flag = false;
		String sql = "insert into schoolMsg (title, content, schoolAdminId) values(?,?,?)";
		Object[] params = { msg.getTitle(), msg.getContent(),
				msg.getSchoolAdminUsername() };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 获取最新一条信息记录的id
	 * 
	 * @return latest schoolMsg Id
	 */
	public int getLatestSchoolMsgId() {
		int id = 0;
		SQLExecution sqlE = new SQLExecution();
		String sql = "select * from schoolMsg order by date desc limit 0,1";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 学校发送信息时向表schoolMsg_destGrade插入记录 用于记录该信息是发送给哪些年级的
	 * 
	 * @param schoolMsgId
	 * @param grade
	 * @return true or false
	 */
	public boolean newSchoolMsg_Grade(int schoolMsgId, String[] grade) {
		boolean flag = false;
		// 因为循环插入数据，所有不使用SQLExecution
		PreparedStatement pstmt = null;
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		String sql = "insert into schoolMsg_destGrade (schoolMsgId, gradeId) values (?,?)";
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, schoolMsgId);
			for (int i = 0, count = grade.length; i < count; i++) {
				flag = false;
				pstmt.setInt(2, Integer.parseInt(grade[i]));
				if (pstmt.executeUpdate() > 0) {
					flag = true;
				}
				if (flag == false) {
					System.out.println("grade error!");
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
	 * 学校发送信息时向表schoolMsg_destMajor中插入记录 用于记录该信息是发送给哪些专业的
	 * 
	 * @param schoolMsgId
	 * @param major
	 * @return true or false
	 */
	public boolean newSchoolMsg_Major(int schoolMsgId, String[] major) {
		boolean flag = true;

		String sqlLoadData = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE schoolMsg_destMajor (schoolMsgId, majorId)";

		MassInsertion mi = new MassInsertion();

		List<Integer> destMajor = new ArrayList<Integer>();
		for (int i = 0; i < major.length; i++) {
			destMajor.add(Integer.parseInt(major[i]));
		}

		System.out.println(mi.excuteMassInsertion(sqlLoadData, schoolMsgId,
				destMajor));

		// 因为循环插入数据，所有不使用SQLExecution
		// PreparedStatement pstmt = null;
		// ConnectionFactory factory = ConnectionFactory.getInstatnce();
		// Connection conn = null;
		// String sql =
		// "insert into schoolMsg_destMajor (schoolMsgId, majorId) values (?,?)";
		// try {
		// conn = factory.getConnection();
		// pstmt = conn.prepareStatement(sql);
		// pstmt.setInt(1, schoolMsgId);
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
	 * 学校发送信息时向表schoolMsg_receivers中插入记录 用于记录该信息是发送给哪些学生（老师）的
	 * 
	 * @param schoolMsgId
	 * @param destUsers
	 * @return true or false
	 */
	public boolean newSchoolMsg_Receivers(int schoolMsgId,
			List<Integer> destUsers) {
		boolean flag = true;
		// PreparedStatement pstmt = null;
		// ConnectionFactory factory = ConnectionFactory.getInstatnce();
		// Connection conn = null;
		// String sql =
		// "insert into schoolMsg_receivers (schoolMsgId, receiverId) values (?,?)";

		String sqlLoadData = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE schoolMsg_receivers (schoolMsgId, receiverId)";

		MassInsertion mi = new MassInsertion();
		System.out.println(mi.excuteMassInsertion(sqlLoadData, schoolMsgId,
				destUsers));

		// String sqlNew =
		// "insert into schoolMsg_receivers (schoolMsgId, receiverId) values ";
		//
		// for (int i=0, count1 = destUsers.size(); i<count1; i++) {
		// if (i == count1-1) {
		//
		// sqlNew = sqlNew + "" + schoolMsgId + "," + destUsers.get(i) + ");";
		// } else {
		// sqlNew = sqlNew + schoolMsgId + "," + destUsers.get(i) + "),";
		// }
		// }
		//
		// System.out.println(sqlNew);

		// try {
		// conn = factory.getConnection();
		// pstmt = conn.prepareStatement(sql);
		// pstmt.setInt(1, schoolMsgId);
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

	/**
	 * 发送信息过程出现错误时（一般是数据库异常），将整个发送过程回滚，使数据库恢复到未发送信息之前的状态
	 * 
	 * @param latestSchoolMsgId
	 * @return true or false
	 */
	public boolean roll_Back(int latestSchoolMsgId) {
		boolean flag = false;
		if (this.roll_Back_DestGrade(latestSchoolMsgId)
				&& this.roll_Back_DestMajor(latestSchoolMsgId)
				&& this.roll_Back_Receivers(latestSchoolMsgId)) {
			// 回滚过程无错误
			flag = true;
		} else {
			// 回滚过程出错
			// do nothing.
		}
		return flag;
	}

	/**
	 * 发送信息过程出现错误时，删除此次发送过程中已插入表schoolMsg中但实际无效的数据
	 * 
	 * @param latestSchoolMsgId
	 * @return true or false
	 */
	public boolean roll_Back_SchoolMsg(int latestSchoolMsgId) {
		boolean flag = false;
		String sql = "delete from schoolMsg where id=?";
		Object[] params = { latestSchoolMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 发送信息过程出现错误时，删除此次发送过程中已插入表schoolMsg_destGrade中但实际无效的数据
	 * 
	 * @param latestSchoolMsgId
	 * @return true or false
	 */
	public boolean roll_Back_DestGrade(int latestSchoolMsgId) {
		boolean flag = false;
		String sql = "delete from schoolMsg_destGrade where schoolMsgId=?";
		Object[] params = { latestSchoolMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 发送信息过程出现错误时，删除此次发送过程中已插入表schoolMsg_destMajor中但实际无效的数据
	 * 
	 * @param latestSchoolMsgId
	 * @return true or false
	 */
	public boolean roll_Back_DestMajor(int latestSchoolMsgId) {
		boolean flag = false;
		String sql = "delete from schoolMsg_destMajor where schoolMsgId=?";
		Object[] params = { latestSchoolMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 发送信息过程出现错误时，删除此次发送过程中已插入表schoolMsg_receivers中但实际无效的数据
	 * 
	 * @param latestSchoolMsgId
	 * @return true or false
	 */
	public boolean roll_Back_Receivers(int latestSchoolMsgId) {
		boolean flag = false;
		String sql = "delete from schoolMsg_receivers where schoolMsgId=?";
		Object[] params = { latestSchoolMsgId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 获得所有通知的条数，公司管理员使用
	 * 
	 * @param
	 * @return
	 */
	public int getSchoolMsgCount() {
		int i = -1;
		String sql = "select count(*) from schoolMsg";
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
	 * 获取某一学校管理员发送通知的数量
	 * 
	 * @param sAdmin
	 * @return
	 */
	public int getSchoolMsgCount(SchoolAdmin sAdmin) {
		int i = -1;
		String sql = "select count(*) from schoolMsg where schoolAdminId=?";
		Object[] params = { sAdmin.getUsername() };
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
	 * 获取某一学校管理员查询的发送消息的数量
	 * 
	 * @param sAdmin
	 * @param keyword
	 * @return
	 */
	public int getSchoolMsgQueryCount(SchoolAdmin sAdmin, String keyword) {
		int i = -1;
		String sql = "select count(*) from schoolMsg where schoolAdminId='"
				+ sAdmin.getUsername() + "' and title like '%" + keyword + "%'";
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
	 * 获取学校管理员发送消息的数量，公司管理员查询时用
	 * 
	 * @param keyword
	 * @return
	 */
	public int getSchoolMsgQueryCount(String keyword) {
		int i = -1;
		String sql = "select count(*) from schoolMsg where title like '%"
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
	 * 公司管理员使用，根据分页的页码获取要现实在该页的通知ID List
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Integer> getSchoolMsgIdList(int start, int end) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from schoolMsg order by date desc limit ?,?";
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
	 * 学校管理员使用，根据分页与管理员身份获取某一页面需要显示的通知ID List
	 * 
	 * @param start
	 * @param end
	 * @param sAdmin
	 * @return
	 */
	public List<Integer> getSchoolMsgIdList(int start, int end,
			SchoolAdmin sAdmin) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from schoolMsg where schoolAdminId = ? order by date desc limit ?,?";
		Object[] params = { sAdmin.getUsername(), start, end };
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
	 * 学校管理员使用，根据分页、管理员身份和查询的关键字获取某一页面需要显示的通知ID List
	 * 
	 * @param start
	 * @param end
	 * @param sAdmin
	 * @return
	 */
	public List<Integer> getSchoolMsgIdList(int start, int end,
			SchoolAdmin sAdmin, String keyword) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from schoolMsg where schoolAdminId = '"
				+ sAdmin.getUsername() + "' and title like '%" + keyword
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
	 * 公司管理员使用，根据分页和搜索关键字获得要显示的通知ID list， 搜索时使用
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<Integer> getSchoolMsgIdList(int start, int end, String keyword) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from schoolMsg where title like '%" + keyword
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

	/*
	 * 公司管理员用，查看学校已发信息
	 */
	public List<SchoolMsgVo> getMsgToDisplayAdmin(List<Integer> schoolMsgIdList) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = factory.getConnection();
			for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
				SchoolMsgVo sMsgVo = new SchoolMsgVo();

				// get schoolMsg basic info
				String sqlGetMsgInfo = "select schoolMsg.id, schoolMsg.title, schoolMsg.date, school.name as schoolName "
						+ "from schoolMsg, schoolAdmin, school "
						+ "where schoolAdmin.username = schoolMsg.schoolAdminId "
						+ "and schoolAdmin.schoolId = school.id and schoolMsg.id = ?";
				pstmt = conn.prepareStatement(sqlGetMsgInfo);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsMsgInfo = pstmt.executeQuery();
				while (rsMsgInfo.next()) {
					sMsgVo.setSchoolMsgId(rsMsgInfo.getInt("id"));
					sMsgVo.setTitle(rsMsgInfo.getString("title"));
					sMsgVo.setDate(rsMsgInfo.getString("date").substring(0, 19));
					sMsgVo.setSchoolName(rsMsgInfo.getString("schoolName"));
				}

				JedisPool pool = new RedisConnection().getConnection();
				Jedis jedis = pool.getResource();

				try {

					String num = jedis.hget("schoolMsg:receivedNum",
							Integer.toString(schoolMsgIdList.get(i)));

					if (num == null) {

						int receivedNum = 0;
						String sqlReceivedNum = "select count(*) from schoolMsg_receivedStu where schoolMsgId = ?";
						pstmt = conn.prepareStatement(sqlReceivedNum);
						pstmt.setInt(1, schoolMsgIdList.get(i));
						ResultSet rsReceivedNum = pstmt.executeQuery();
						while (rsReceivedNum.next()) {
							receivedNum = rsReceivedNum.getInt(1);
						}
						sMsgVo.setReceivedNum(receivedNum);
						jedis.hset("schoolMsg:receivedNum",
								Integer.toString(schoolMsgIdList.get(i)),
								Integer.toString(receivedNum));
					} else {
						sMsgVo.setReceivedNum(Integer.parseInt(num));
					}
				} finally {
					pool.returnResource(jedis);
				}

				pool.destroy();

				// get receiverNum
				String sqlReceiverNum = "select count(*) from schoolMsg_receivers where schoolMsgId = ?";
				pstmt = conn.prepareStatement(sqlReceiverNum);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsReceiverNum = pstmt.executeQuery();
				while (rsReceiverNum.next()) {
					sMsgVo.setReceiverNum(rsReceiverNum.getInt(1));
				}

				list.add(sMsgVo);
				pstmt.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/*
	 * 学校管理员用，查看本人帐号已发信息
	 */
	public List<SchoolMsgVo> getMsgToDisplaySchoolAdmin(
			List<Integer> schoolMsgIdList) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = factory.getConnection();
			for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
				SchoolMsgVo sMsgVo = new SchoolMsgVo();

				// get schoolMsg basic info
				String sqlGetMsgInfo = "select schoolMsg.id, schoolMsg.title, schoolMsg.date from schoolMsg where schoolMsg.id = ?";
				pstmt = conn.prepareStatement(sqlGetMsgInfo);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsMsgInfo = pstmt.executeQuery();
				while (rsMsgInfo.next()) {
					sMsgVo.setSchoolMsgId(rsMsgInfo.getInt("id"));
					sMsgVo.setTitle(rsMsgInfo.getString("title"));
					sMsgVo.setDate(rsMsgInfo.getString("date").substring(0, 19));
				}

				JedisPool pool = new RedisConnection().getConnection();
				Jedis jedis = pool.getResource();

				try {

					String num = jedis.hget("schoolMsg:receivedNum",
							Integer.toString(schoolMsgIdList.get(i)));

					if (num == null) {

						String sqlReceivedNum = "select count(*) from schoolMsg_receivedStu where schoolMsgId = ?";
						pstmt = conn.prepareStatement(sqlReceivedNum);
						pstmt.setInt(1, schoolMsgIdList.get(i));
						ResultSet rsReceivedNum = pstmt.executeQuery();
						sMsgVo.setReceivedNum(0);
						while (rsReceivedNum.next()) {
							sMsgVo.setReceivedNum(rsReceivedNum.getInt(1));
						}
					} else {
						sMsgVo.setReceivedNum(Integer.parseInt(num));
					}
				} finally {
					pool.returnResource(jedis);
				}

				pool.destroy();

				// get receiverNum
				String sqlReceiverNum = "select count(*) from schoolMsg_receivers where schoolMsgId = ?";
				pstmt = conn.prepareStatement(sqlReceiverNum);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsReceiverNum = pstmt.executeQuery();
				while (rsReceiverNum.next()) {
					sMsgVo.setReceiverNum(rsReceiverNum.getInt(1));
				}

				list.add(sMsgVo);
				pstmt.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 通过通知id列表获取通知的详细信息
	 * 
	 * @param schoolMsgIdList
	 * @return
	 */
	public List<SchoolMsgVo> getSchoolMsgAll(List<Integer> schoolMsgIdList) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = factory.getConnection();
			for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
				SchoolMsgVo sMsgVo = new SchoolMsgVo();

				// get schoolMsg basic info
				String sqlGetMsgInfo = "select schoolMsg.id, schoolMsg.title, schoolMsg.content, schoolMsg.date, "
						+ "schoolAdmin.name as schoolAdminName, school.name as schoolName "
						+ "from schoolMsg, schoolAdmin, school where schoolAdmin.username = schoolMsg.schoolAdminId "
						+ "and schoolAdmin.schoolId = school.id and schoolMsg.id = ?";
				pstmt = conn.prepareStatement(sqlGetMsgInfo);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsMsgInfo = pstmt.executeQuery();
				while (rsMsgInfo.next()) {
					sMsgVo.setSchoolMsgId(rsMsgInfo.getInt("id"));
					sMsgVo.setTitle(rsMsgInfo.getString("title"));
					sMsgVo.setContent(rsMsgInfo.getString("content"));
					sMsgVo.setDate(rsMsgInfo.getString("date").substring(0, 19));
					sMsgVo.setSchoolName(rsMsgInfo.getString("schoolAdminName"));
					sMsgVo.setSenderName(rsMsgInfo.getString("schoolName"));
				}

				// get destGrade
				String sqlGetGrade = "select distinct(grade.description) as grade from grade, schoolMsg_destGrade "
						+ "where grade.id = schoolMsg_destGrade.gradeId and schoolMsg_destGrade.schoolMsgId = ?";
				pstmt = conn.prepareStatement(sqlGetGrade);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsGrade = pstmt.executeQuery();
				List<String> destGrade = new ArrayList<String>();
				while (rsGrade.next()) {
					destGrade.add(rsGrade.getString("grade"));
				}
				sMsgVo.setDestGrade(destGrade);

				// get destDepartment
				String sqlGetDepartment = "select distinct(department.name) as department from department, schoolMsg_destMajor, "
						+ "major where department.id = any(select departmentId from major "
						+ "where id = any(select majorId from schoolMsg_destMajor where schoolMsg_destMajor.schoolMsgId = ?))";
				pstmt = conn.prepareStatement(sqlGetDepartment);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsDepartment = pstmt.executeQuery();
				List<String> destDepartment = new ArrayList<String>();
				while (rsDepartment.next()) {
					destDepartment.add(rsDepartment.getString("department"));
				}
				sMsgVo.setDestDepartment(destDepartment);

				// get destMajor
				String sqlGetMajor = "select distinct(major.name) as major from major, schoolMsg_destMajor "
						+ "where major.id = schoolMsg_destMajor.majorId and schoolMsg_destMajor.schoolMsgId = ?";
				pstmt = conn.prepareStatement(sqlGetMajor);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsMajor = pstmt.executeQuery();
				List<String> destMajor = new ArrayList<String>();
				while (rsMajor.next()) {
					destMajor.add(rsMajor.getString("major"));
				}
				sMsgVo.setDestMajor(destMajor);

				// get receivedNum
				// String sqlReceivedNum =
				// "select count(*) from schoolMsg_receivedStu where schoolMsgId = ?";
				// pstmt = conn.prepareStatement(sqlReceivedNum);
				// pstmt.setInt(1, schoolMsgIdList.get(i));
				// ResultSet rsReceivedNum = pstmt.executeQuery();
				// sMsgVo.setReceivedNum(0);
				// while (rsReceivedNum.next()) {
				// sMsgVo.setReceivedNum(rsReceivedNum.getInt(1));
				// }

				JedisPool pool = new RedisConnection().getConnection();
				Jedis jedis = pool.getResource();

				try {

					String num = jedis.hget("schoolMsg:receivedNum",
							Integer.toString(schoolMsgIdList.get(i)));

					if (num == null) {

						String sqlReceivedNum = "select count(*) from schoolMsg_receivedStu where schoolMsgId = ?";
						pstmt = conn.prepareStatement(sqlReceivedNum);
						pstmt.setInt(1, schoolMsgIdList.get(i));
						ResultSet rsReceivedNum = pstmt.executeQuery();
						sMsgVo.setReceivedNum(0);
						while (rsReceivedNum.next()) {
							sMsgVo.setReceivedNum(rsReceivedNum.getInt(1));
						}
					} else {
						sMsgVo.setReceivedNum(Integer.parseInt(num));
					}
				} finally {
					pool.returnResource(jedis);
				}

				pool.destroy();

				// get receiverNum
				String sqlReceiverNum = "select count(*) from schoolMsg_receivers where schoolMsgId = ?";
				pstmt = conn.prepareStatement(sqlReceiverNum);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsReceiverNum = pstmt.executeQuery();
				while (rsReceiverNum.next()) {
					sMsgVo.setReceiverNum(rsReceiverNum.getInt(1));
				}

				// get remarks
				String sqlRemarks = "select remarks from schoolAdmin where username = (select schoolAdminId from schoolMsg where id = ?)";
				pstmt = conn.prepareStatement(sqlRemarks);
				pstmt.setInt(1, schoolMsgIdList.get(i));
				ResultSet rsRemarks = pstmt.executeQuery();
				while (rsRemarks.next()) {
					sMsgVo.setRemarks(rsRemarks.getString(1));
				}

				list.add(sMsgVo);
				pstmt.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 根据通知id获得通知的目的年级
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getGradeIdListByMsgId(int schoolMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select distinct(gradeId) from schoolMsg_destGrade where schoolMsgId=?";
		Object[] params = { schoolMsgId };
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

	public List<Integer> getDepartmentIdListByMsgId(int schoolMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select distinct(departmentId) from major where id = any (select majorId from schoolMsg_destMajor where schoolMsgId = ?)";
		Object[] params = { schoolMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据通知id获得通知的目的专业
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getMajorIdListByMsgId(int schoolMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select distinct(majorId) from schoolMsg_destMajor where schoolMsgId=?";
		Object[] params = { schoolMsgId };
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
	 * 根据通知id获取接受者列表
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getDestUsersListByMsgId(int schoolMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select * from schoolMsg_receivers where schoolMsgId=?";
		Object[] params = { schoolMsgId };
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
	 * 根据通知id获取接收者中的老师id，用于信息重发
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public List<Integer> getDestTeachersByMsgId(int schoolMsgId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select receiverId from schoolMsg_receivers, student where student.id = schoolMsg_receivers.receiverId and student.isTeacher = 1 and schoolMsgId=?";
		Object[] params = { schoolMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过stuId获取该学生要接收的通知id列表
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
	// "select schoolMsgId from schoolMsg_receivers where receiverId = ? order by schoolMsgId desc";
	// String sqlReceived =
	// "select schoolMsgId from schoolMsg_receivedStu where receivedStuId = ? order by schoolMsgId desc";
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
		String key1 = "schoolMsg:toReceive:" + Integer.toString(stuId);
		String key2 = "schoolMsg:received:" + Integer.toString(stuId);

		try {

			List<Integer> msgIdList = new ArrayList<Integer>();
			SchoolMsgCache cache = new SchoolMsgCache();

			Set<String> receivedSet = jedis.smembers(key2);
			if (receivedSet.size() == 0) {
				msgIdList = this.getReceivedMsgIdsByStuId(stuId);
				cache.cacheSchoolMsg_ReceivedStu(stuId, msgIdList);
				msgIdList = this.getToReceiveMsgIdsByStuId(stuId);
				cache.cacheSchoolMsg_toReceive(stuId, msgIdList);
			}

			// 对比获得要更新的信息id

			Set<String> toReceivedSet = jedis.sdiff(key1, key2);
			TreeSet<String> ts = new TreeSet<String>(toReceivedSet);
			ts.comparator();
			Iterator<String> it = ts.iterator();
			while (it.hasNext()) {
				String str = it.next();
				toReceive.add(Integer.parseInt(str));
			}
			
//			jedis.hincrBy("unReadCount", Integer.toString(stuId), toReceive.size());

			if (msgIdList != null) {
				msgIdList = null;
			}
			if (receivedSet != null) {
				receivedSet = null;
			}
			if (toReceivedSet != null) {
				toReceivedSet = null;
			}
			if (ts != null) {
				ts = null;
			}

		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
		return toReceive;
	}

	/**
	 * 获取某一学生待接收信息的id列表
	 * 
	 * @param stuId
	 * @return
	 */
	public List<Integer> getToReceiveMsgIdsByStuId(int stuId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select schoolMsgId from schoolMsg_receivers where receiverId = ? order by schoolMsgId desc";
		Object[] params = { stuId };
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
	 * 获取某一学生已接收信息的id列表
	 * 
	 * @param stuId
	 * @return
	 */
	public List<Integer> getReceivedMsgIdsByStuId(int stuId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select schoolMsgId from schoolMsg_receivedStu where receivedStuId = ? order by schoolMsgId desc";
		Object[] params = { stuId };
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
	 * 根据用户id获取需要更新的通知
	 * 
	 * @param stuId
	 * @return
	 */
	public List<SchoolMsgVo> getMsgToUpdate(int stuId) {
		List<SchoolMsgVo> list = new ArrayList<SchoolMsgVo>();

		// 进行数据统计
		new Statistics().setStatisticsDate(stuId);

		// 缓存学生与学校的对应关系，防止统计出错
		new SchoolStudentCache().CacheStuSchool(stuId);

		// 根据用户id获取需要更新的通知id列表
		// 限制最多取10条信息
		List<Integer> tempList = this.getMsgIdListtoUpdate(stuId);
		List<Integer> idList = new ArrayList<Integer>();
		// 限制每次最多取10条信息
		if (tempList.size() > 10) {
			for (int i = 0; i < 10; i++) {
				idList.add(tempList.get(i));
			}
		} else {
			idList = tempList;
		}
		list = new SchoolMsgCache().getSchoolMsgFromCache(idList);
//		System.out.println("list size: " + list.size());
		return list;
	}

	/**
	 * 通过通知id获得通知内容
	 * 
	 * @param schoolMsgId
	 * @return
	 */
	public SchoolMsg getSchoolMsgById(int schoolMsgId) {
		SchoolMsg sMsg = new SchoolMsg();
		String sql = "select * from schoolMsg where id=?";
		Object[] params = { schoolMsgId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				sMsg.setId(rs.getInt("id"));
				sMsg.setTitle(rs.getString("title"));
				sMsg.setContent(rs.getString("content"));
				sMsg.setDate(rs.getString("date"));
				sMsg.setSchoolAdminUsername(rs.getString("schoolAdminId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sMsg;
	}

	/**
	 * 根据客户端返回的学生id和已收到的通知id列表，更新schoolMsg_received表
	 * 
	 * @param stuId
	 * @param schoolMsgIdList
	 */
	// public void updateSchoolMsgReceived(int stuId, List<Integer>
	// schoolMsgIdList) {
	// ConnectionFactory factory = ConnectionFactory.getInstatnce();
	// PreparedStatement pstmt = null;
	// PreparedStatement pstmt1 = null;
	// Connection conn = null;
	// try {
	// conn = factory.getConnection();
	// String sql =
	// "insert into schoolMsg_receivedStu (schoolMsgId, receivedStuId) values (?,?)";
	// String sqlIsReceived =
	// "select * from schoolMsg_receivedStu where schoolMsgId = ? and receivedStuId = ?";
	// pstmt = conn.prepareStatement(sql);
	// pstmt1 = conn.prepareStatement(sqlIsReceived);
	// pstmt.setInt(2, stuId);
	// pstmt1.setInt(2, stuId);
	// for (int i = 0, count = schoolMsgIdList.size(); i < count; i++) {
	// pstmt.setInt(1, schoolMsgIdList.get(i));
	// pstmt1.setInt(1, schoolMsgIdList.get(i));
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

	public void updateSchoolMsgReceived(int stuId, List<Integer> schoolMsgIdList) {
		new SchoolMsgCache().cacheSchoolMsg_ReceivedStu(stuId, schoolMsgIdList);
	}

	/**
	 * 将表schoolMsg_receivers 中的数据存入hashmap中，用于缓存。 仅在redis清空之后调用一次
	 * 
	 * @return
	 */
	public HashMap<Integer, List<Integer>> getSchoolMsgReceiverAll() {
		HashMap<Integer, List<Integer>> destMap = new HashMap<Integer, List<Integer>>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select distinct(receiverId) from schoolMsg_receivers";
		String sql1 = "select distinct(schoolMsgId) from schoolMsg_receivers where receiverId = ?";
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
	 * 将表schoolMsg_receivedStu 中的数据存入hashmap中，用于缓存。 仅在redis清空之后调用一次
	 * 
	 * @return
	 */
	public HashMap<Integer, List<Integer>> getSchoolMsgReceivedAll() {
		HashMap<Integer, List<Integer>> destMap = new HashMap<Integer, List<Integer>>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String sql = "select distinct(receivedStuId) from schoolMsg_receivedStu";
		String sql1 = "select distinct(schoolMsgId) from schoolMsg_receivedStu where receivedStuId = ?";
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
	 * 将有关schoolMsg_receivedStu的数据从redis中取出，并写入MySQL中
	 * 
	 * @return
	 */
	public boolean writeIntoSchoolMsg_ReceivedStu() {
		boolean flag = false;

		JedisPool pool = new RedisConnection().getConnection();
		Jedis jedis = pool.getResource();

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String sql1 = "select id from schoolMsg_receivedStu where schoolMsgId = ? and receivedStuId = ?";
		String sql2 = "insert into schoolMsg_receivedStu (schoolMsgId, receivedStuId) value (?, ?)";

		try {
			conn = factory.getConnection();
			Set<String> stuIdList = jedis
					.smembers("schoolMsg:received:stuIdAll");
			Iterator<String> it = stuIdList.iterator();
			while (it.hasNext()) {
				int stuId = Integer.parseInt(it.next());
				String key = "schoolMsg:received:" + stuId;
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
				if (schoolMsgIdList != null) {
					schoolMsgIdList = null;
				}
			}
			if (pstmt1 != null) {
				pstmt1.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
			if (stuIdList != null) {
				stuIdList = null;
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
	 * 统计校园通知已接收数，只在缓存清空之后执行一次
	 * 
	 * @return
	 */
	public HashMap<Integer, Integer> getRecievedNum() {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String sql1 = "select distinct(schoolMsgId) from schoolMsg_receivedStu";
		String sql2 = "select count(receivedStuId) from schoolMsg_receivedStu where schoolMsgId = ?";
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

	public List<Integer> getMsgIdAll() {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from schoolMsg";
		Object[] params = {};
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
