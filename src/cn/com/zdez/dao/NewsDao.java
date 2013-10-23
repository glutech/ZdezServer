package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.po.News;
import cn.com.zdez.service.SchoolMsgService;
import cn.com.zdez.service.SchoolService;
import cn.com.zdez.util.MassInsertion;
import cn.com.zdez.vo.NewsVo;

public class NewsDao {

	private SQLExecution sqlE = new SQLExecution();

	/**
	 * 新建新闻
	 * 
	 * @param n
	 * @return
	 */
	public boolean newNews(News n) {
		boolean flag = false;
		String sql = "insert into news(title, content) values(?,?)";
		Object[] params = { n.getTitle(), n.getContent() };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 获取最新的新闻id 用于写入与新闻相关的表以及对新闻进行缓存
	 * 
	 * @return
	 */
	public int getLatestNewsId() {
		int i = 0;
		String sql = "select * from news order by date desc limit 0,1";
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
	 * 向表news_receivers中写入记录，表明某条信息需要哪些用户接收
	 * 
	 * @param newsId
	 * @param destUsers
	 * @return
	 */
	public boolean newNews_Receivers(int newsId, List<Integer> destUsers) {
		boolean flag = true;
		
		String sqlLoadData = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE news_receivers (newsId, receiverStuId)";
		
		MassInsertion mi = new MassInsertion();
		System.out.println(mi.excuteMassInsertion(sqlLoadData, newsId, destUsers));
		
//		ConnectionFactory factor = ConnectionFactory.getInstatnce();
//		PreparedStatement pstmt = null;
//		Connection conn = null;
//		try {
//			conn = factor.getConnection();
//			String sql = "insert into news_receivers(newsId, receiverStuId) values (?,?)";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, newsId);
//			for (int i = 0, count = destUsers.size(); i < count; i++) {
//				System.out.println("inserting into newNews_Receivers....");
//				pstmt.setInt(2, destUsers.get(i));
//				if (pstmt.executeUpdate() > 0) {
//					flag = true;
//				}
//				if (flag == false) {
//					break;
//				}
//			}
//			pstmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			factor.freeConnection(conn);
//		}
		return flag;
	}

	/**
	 * 删除表news_receivers中的某些记录 用于新建news的过程中出现错误，防止数据库数据冗余
	 * 
	 * @param newsId
	 * @return
	 */
	public boolean deleteReceivers(int newsId) {
		boolean flag = false;
		String sql = "delete from news_receivers where newsId = ?";
		Object[] params = { newsId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 删除表news中的某条记录 用于新建news过程中出现错误，防止数据库数据冗余
	 * 
	 * @param newsId
	 * @return
	 */
	public boolean deleteNews(int newsId) {
		boolean flag = false;
		String sql = "delete from news where id = ?";
		Object[] params = { newsId };
		flag = sqlE.execSqlWithoutRS(sql, params);
		return flag;
	}

	/**
	 * 通过用户id获取该用户需要更新的NewsVo
	 * 
	 * @param stuId
	 * @return
	 */
	public List<NewsVo> getNewsToUpdate(int stuId) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		List<NewsVo> nList = this.getNewsByIdList(this
				.getNewsIdListtoUpdate(stuId));
		for (int i = 0, count = nList.size(); i < count; i++) {
			NewsVo s = new NewsVo();
			s.setId(nList.get(i).getId());
			s.setTitle(nList.get(i).getTitle());
			s.setContent(nList.get(i).getContent());
			s.setDate(nList.get(i).getDate().substring(0, 19));
			s.setCoverPath(new SchoolMsgService().getCoverPath(nList.get(i)
					.getContent()));
			list.add(s);
		}
		return list;
	}

	/**
	 * 根据用户id获取该用户需要更新的newsId列表
	 * 
	 * @param stuId
	 * @return
	 */
	public List<Integer> getNewsIdListtoUpdate(int stuId) {
		List<Integer> toReceive = new ArrayList<Integer>();
		List<Integer> received = new ArrayList<Integer>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		Connection conn = null;

		String sqlToReceive = "select newsId from news_receivers where receiverStuId = ? order by newsId desc";
		String sqlReceived = "select newsId from news_received where receivedStuId = ? order by newsId desc";

		try {
			conn = factory.getConnection();

			// 获取某一学生要接收的通知列表
			pstmt1 = conn.prepareStatement(sqlToReceive);
			pstmt1.setInt(1, stuId);
			ResultSet rsIdToReceive = pstmt1.executeQuery();
			while (rsIdToReceive.next()) {
				toReceive.add(rsIdToReceive.getInt(1));
			}

			// 获取某一学生已接收的通知id列表
			pstmt2 = conn.prepareStatement(sqlReceived);
			pstmt2.setInt(1, stuId);
			ResultSet rsIdReceived = pstmt2.executeQuery();
			while (rsIdReceived.next()) {
				received.add(rsIdReceived.getInt(1));
			}

			toReceive.removeAll(received);

			pstmt1.close();
			pstmt2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}

		return toReceive;
	}
	
	/*
	 * 获取用于查看新闻列表的数据
	 */
	public List<NewsVo> getNewsToDisplayByIdList(List<Integer> newsIdList) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select id, title, date from news where id = ?";
			pstmt = conn.prepareStatement(sql);
			String receivedNumSql = "select count(receivedStuId) from news_received where newsId = ?";
			pstmt1 = conn.prepareStatement(receivedNumSql);
			String receiverNumSql = "select count(receiverStuId) from news_receivers where newsId = ?";
			pstmt2 = conn.prepareStatement(receiverNumSql);
			for (int i = 0, count = newsIdList.size(); i < count; i++) {
				pstmt.setInt(1, newsIdList.get(i));
				pstmt1.setInt(1, newsIdList.get(i));
				pstmt2.setInt(1, newsIdList.get(i));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					NewsVo n = new NewsVo();
					n.setId(rs.getInt("id"));
					n.setTitle(rs.getString("title"));
//					n.setContent(rs.getString("content"));
					n.setDate(rs.getString("date").substring(0, 19));

					int receivedNum = -1;
					ResultSet rsReceived = pstmt1.executeQuery();
					while (rsReceived.next()) {
						receivedNum = rsReceived.getInt(1);
					}
					n.setReceivedNum(receivedNum);

					int receiverNum = -1;
					ResultSet rsReceiver = pstmt2.executeQuery();
					while (rsReceiver.next()) {
						receiverNum = rsReceiver.getInt(1);
					}
					n.setReceiverNum(receiverNum);

					list.add(n);
				}
			}
			pstmt.close();
			pstmt1.close();
			pstmt2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}
	
	/*
	 * 用于查看新闻的详细信息
	 */
	public List<NewsVo> getNewsDetialByIdList(List<Integer> newsIdList) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt3 = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select id, title, date from news where id = ?";
			pstmt = conn.prepareStatement(sql);
			String getSchoolIdsSql = "select distinct(id) from school where id = any"
					+ "(select schoolId from department where id = any"
					+ "(select departmentId from major where id = any"
					+ "(select majorId from student where id = any"
					+ "(select receiverStuId from news_receivers where newsId = ?))))";
			pstmt3 = conn.prepareStatement(getSchoolIdsSql);
			for (int i = 0, count = newsIdList.size(); i < count; i++) {
				pstmt.setInt(1, newsIdList.get(i));
				pstmt3.setInt(1, newsIdList.get(i));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					NewsVo n = new NewsVo();
					n.setId(rs.getInt("id"));
					n.setTitle(rs.getString("title"));
					n.setDate(rs.getString("date").substring(0, 19));

					List<String> destSchools = new ArrayList<String>();
					ResultSet rsDestSchools = pstmt3.executeQuery();
					List<Integer> schoolIdList = new ArrayList<Integer>();
					while (rsDestSchools.next()) {
						schoolIdList.add(rsDestSchools.getInt(1));
					}
					destSchools = new SchoolService()
							.getSchoolNameById(schoolIdList);
					n.setDestSchools(destSchools);

					list.add(n);
				}
			}
			pstmt.close();
			pstmt3.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
		return list;
	}

	/**
	 * 根据newsIdList获取对应的NewsVo 用于批量获取NewsVo时，利用idList是为了减少数据库连接
	 * 
	 * @param newsIdList
	 * @return
	 */
	public List<NewsVo> getNewsByIdList(List<Integer> newsIdList) {
		List<NewsVo> list = new ArrayList<NewsVo>();
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "select * from news where id = ?";
			pstmt = conn.prepareStatement(sql);
			String receivedNumSql = "select count(*) from news_received where newsId = ?";
			pstmt1 = conn.prepareStatement(receivedNumSql);
			String receiverNumSql = "select count(*) from news_receivers where newsId = ?";
			pstmt2 = conn.prepareStatement(receiverNumSql);
			String getSchoolIdsSql = "select distinct(id) from school where id = any"
					+ "(select schoolId from department where id = any"
					+ "(select departmentId from major where id = any"
					+ "(select majorId from student where id = any"
					+ "(select receiverStuId from news_receivers where newsId = ?))))";
			pstmt3 = conn.prepareStatement(getSchoolIdsSql);
			for (int i = 0, count = newsIdList.size(); i < count; i++) {
				pstmt.setInt(1, newsIdList.get(i));
				pstmt1.setInt(1, newsIdList.get(i));
				pstmt2.setInt(1, newsIdList.get(i));
				pstmt3.setInt(1, newsIdList.get(i));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					NewsVo n = new NewsVo();
					n.setId(rs.getInt("id"));
					n.setTitle(rs.getString("title"));
					n.setContent(rs.getString("content"));
					n.setDate(rs.getString("date").substring(0, 19));

					List<String> destSchools = new ArrayList<String>();
					ResultSet rsDestSchools = pstmt3.executeQuery();
					List<Integer> schoolIdList = new ArrayList<Integer>();
					while (rsDestSchools.next()) {
						schoolIdList.add(rsDestSchools.getInt(1));
					}
					destSchools = new SchoolService()
							.getSchoolNameById(schoolIdList);
					n.setDestSchools(destSchools);

					int receivedNum = -1;
					ResultSet rsReceived = pstmt1.executeQuery();
					while (rsReceived.next()) {
						receivedNum = rsReceived.getInt(1);
					}
					n.setReceivedNum(receivedNum);

					int receiverNum = -1;
					ResultSet rsReceiver = pstmt2.executeQuery();
					while (rsReceiver.next()) {
						receiverNum = rsReceiver.getInt(1);
					}
					n.setReceiverNum(receiverNum);

					list.add(n);
				}
			}
			pstmt.close();
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

	/**
	 * 更新表news_received中的内容，表明哪些新闻已经被哪个用户接收 已进行数据冗余处理（即某个stuId-newsId对不再会重复插入）
	 * 
	 * @param stuId
	 * @param newsIdList
	 */
	public void updateNewsReceived(int stuId, List<Integer> newsIdList) {
		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		Connection conn = null;
		try {
			conn = factory.getConnection();
			String sql = "insert into news_received (newsId, receivedStuId) values (?,?)";
			String sqlIsReceived = "select * from news_received where newsId = ? and receivedStuId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sqlIsReceived);
			pstmt.setInt(2, stuId);
			pstmt1.setInt(2, stuId);
			for (int i = 0, count = newsIdList.size(); i < count; i++) {
				pstmt.setInt(1, newsIdList.get(i));
				pstmt1.setInt(1, newsIdList.get(i));
				ResultSet rs = pstmt1.executeQuery();
				// 防止数据冗余
				if (!rs.next()) {
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			pstmt1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
		}
	}

	/**
	 * 获取news数，用于分页
	 * 
	 * @return
	 */
	public int getNewsCount() {
		int i = -1;
		String sql = "select count(*) from news";
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
	 * 根据分页获取对应范围内的newsId
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Integer> getNewsIdList(int start, int end) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from news order by date desc limit ?,?";
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
	 * 获得符合查询关键字news条数，用于分页
	 * 
	 * @param keyword
	 * @return
	 */
	public int getNewsQueryCount(String keyword) {
		int i = -1;
		String sql = "select count(*) from news where title like '%" + keyword
				+ "%'";
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
	 * 根据分页和查询关键字，获取符合条件的idList
	 * 
	 * @param start
	 * @param end
	 * @param keyword
	 * @return
	 */
	public List<Integer> getNewsIdList(int start, int end, String keyword) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select id from news where title like '%" + keyword
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
	 * 通过id获取news实体
	 * 
	 * @param newsId
	 * @return
	 */
	public News getNewsById(int newsId) {
		News n = new News();
		String sql = "select * from news where id = ?";
		Object[] params = { newsId };
		ResultSet rs = sqlE.execSqlWithRS(sql, params);
		try {
			while (rs.next()) {
				n.setTitle(rs.getString("title"));
				n.setContent(rs.getString("content"));
				n.setDate(rs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n;
	}

	/**
	 * 通过newsId获取该信息是发给哪些用户的，用于news重发
	 * 
	 * @param newsId
	 * @return
	 */
	public List<Integer> getDestUsersByNewsId(int newsId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select distinct(receiverStuId) from news_receivers where newsId = ?";
		Object[] params = { newsId };
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
