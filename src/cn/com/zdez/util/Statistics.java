package cn.com.zdez.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.dao.ConnectionFactory;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.po.School;
import cn.com.zdez.service.SchoolService;

/**
 * 对活动用户数进行统计 按照学校，分别统计各个学校日、周、月活动用户数 最后返回的结果是按照日、周、月分别存储的各个学校的在线人数
 * 
 * @author jokinryou
 * 
 */
public class Statistics {

	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();

	/**
	 * 向redis中写入统计数据
	 * 
	 * @param stuId
	 */
	public void setStatisticsDate(int stuId) {

		String stuIdString = Integer.toString(stuId);
		// 利用stuId和当前系统时间进行用户数的统计
		// 获取当前时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		// 时间为域，学生id为值，写入redis
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		map.put(stuIdString, date);

		try {
			jedis.hmset("hashmap:statistics", map);
		} finally {
			pool.returnResource(jedis);
		}
		pool.destroy();
	}

	/**
	 * 从reids中获取统计数据
	 * 
	 * @return
	 */
	public HashMap<String, HashMap<Integer, Integer>> getStatisticsData() {

		HashMap<String, HashMap<Integer, Integer>> statisticsMap = new HashMap<String, HashMap<Integer, Integer>>();
		// 获取当前系统时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String currentDate = sDateFormat.format(new java.util.Date());

		DateConvert dc = new DateConvert();
		String dayDate = dc.DayDateConvert(currentDate);
		String weekDate = dc.WeekDateConvert(currentDate);
		String monthDate = dc.MonthDateConvert(currentDate);

		String[] names = { "dayDate", "weekDate", "monthDate" };
		String[] dates = { dayDate, weekDate, monthDate };
		Set<String> keys = jedis.hkeys("hashmap:statistics");
		for (int i = 0, count = dates.length; i < count; i++) {

			try {
				int schoolId = -1;
				HashMap<Integer, Integer> currentStatisticsMap = new HashMap<Integer, Integer>();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String stuId = it.next().toString();
					if (jedis.hget("hashmap:stuId:schoolId", stuId) != null) {
						schoolId = Integer.parseInt(jedis.hget(
								"hashmap:stuId:schoolId", stuId));
					}
					try {
						Date dt1 = sDateFormat.parse(dates[i]);
						String date = jedis.hget("hashmap:statistics", stuId);
						Date dt2 = sDateFormat.parse(date);
						if (dt2.getTime() > dt1.getTime()) {
							if (currentStatisticsMap.get(schoolId) != null) {
								currentStatisticsMap.put(schoolId,
										currentStatisticsMap.get(schoolId) + 1);
							} else {
								currentStatisticsMap.put(schoolId, 1);
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (currentStatisticsMap.get(schoolId) == null) {
					currentStatisticsMap.put(schoolId, 0);
				}
				statisticsMap.put(names[i] + "StatisticsMap",
						currentStatisticsMap);

			} finally {
				pool.returnResource(jedis);
			}

		}
		pool.destroy();
		return statisticsMap;
	}

	/**
	 * 为了能在网页端显示统计信息，对统计数据进行格式转换。
	 * 
	 * @param sourceMap
	 * @return
	 */
	public HashMap<String, HashMap<String, Integer>> mapConvert(
			HashMap<String, HashMap<Integer, Integer>> sourceMap) {
		HashMap<String, HashMap<String, Integer>> destMap = new HashMap<String, HashMap<String, Integer>>();

		List<School> schoolList = new SchoolService().getAll();
		HashMap<Integer, String> schoolMap = new HashMap<Integer, String>();
		for (School s : schoolList) {
			schoolMap.put(s.getId(), s.getName());
		}

		Iterator<Map.Entry<String, HashMap<Integer, Integer>>> it = sourceMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, HashMap<Integer, Integer>> entry = (Map.Entry<String, HashMap<Integer, Integer>>) it
					.next();
			HashMap<Integer, Integer> tempMap = (HashMap<Integer, Integer>) entry
					.getValue();
			Iterator<Map.Entry<Integer, Integer>> tempIt = tempMap.entrySet()
					.iterator();
			HashMap<String, Integer> m = new HashMap<String, Integer>();
			while (tempIt.hasNext()) {
				Map.Entry<Integer, Integer> tempEntry = (Map.Entry<Integer, Integer>) tempIt
						.next();
				int schoolId = tempEntry.getKey();
				m = destMap.get(schoolMap.get(schoolId));
				if (m == null) {
					m = new HashMap<String, Integer>();
				}
				if (m.get(entry.getKey()) != null) {
					break;
				} else {
					m.put(entry.getKey(), tempEntry.getValue());
				}
				destMap.put(schoolMap.get(schoolId), m);
			}
		}
		return destMap;
	}

	public HashMap<String, List<HashMap<String, Integer>>> getStatisticsDetails() {

		ConnectionFactory factory = ConnectionFactory.getInstatnce();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select department.id as dptId from student, major, department "
				+ "where student.majorId = major.Id "
				+ "and major.departmentId = department.id and student.id = ?";

		HashMap<String, List<HashMap<String, Integer>>> result = new HashMap<String, List<HashMap<String, Integer>>>();
		try {
			HashMap<String, Integer> depCounts = new HashMap<String, Integer>();
			Set<String> stuIdSet = jedis.hkeys("hashmap:statistics");
			Iterator<String> it = stuIdSet.iterator();
			conn = factory.getConnection();
			pstmt = conn.prepareStatement(sql);
			while (it.hasNext()) {
				String idStr = it.next();
				pstmt.setInt(1, Integer.parseInt(idStr));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					int dptId = rs.getInt(1);
					if (depCounts.get(Integer.toString(dptId)) != null) {
						depCounts.put(Integer.toString(dptId),
								depCounts.get(Integer.toString(dptId)) + 1);
					} else {
						depCounts.put(Integer.toString(dptId), 1);
					}
				}
			}

			Set<String> schoolIdListSet = jedis.smembers("schoolIdList");
			Iterator<String> schoolIt = schoolIdListSet.iterator();
			while (schoolIt.hasNext()) {
				String sIdStr = schoolIt.next();
				Set<String> dptIdSet = jedis.smembers("school:" + sIdStr
						+ ":dpt");
				Iterator<String> dptIt = dptIdSet.iterator();
				List<HashMap<String, Integer>> tempList = new ArrayList<HashMap<String, Integer>>();
				while (dptIt.hasNext()) {
					String dptIdStr = dptIt.next();
					Iterator<Entry<String, Integer>> dptCountIt = depCounts
							.entrySet().iterator();
					while (dptCountIt.hasNext()) {
						Map.Entry<String, Integer> entry = dptCountIt.next();
						if (entry.getKey().equals(dptIdStr)) {
							HashMap<String, Integer> temp = new HashMap<String, Integer>();
							temp.put(entry.getKey(), entry.getValue());
							tempList.add(temp);
						}
					}
				}
				result.put(sIdStr, tempList);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			factory.freeConnection(conn);
			pool.returnResource(jedis);
		}

		pool.destroy();
		return result;
	}

	public HashMap<String, List<HashMap<String, Integer>>> convertIdtoName(
			HashMap<String, List<HashMap<String, Integer>>> sourceMap) {
		HashMap<String, List<HashMap<String, Integer>>> result = new HashMap<String, List<HashMap<String, Integer>>>();

		Iterator<Map.Entry<String, List<HashMap<String, Integer>>>> sourceMapIt = sourceMap
				.entrySet().iterator();
		while (sourceMapIt.hasNext()) {
			Map.Entry<String, List<HashMap<String, Integer>>> entry = sourceMapIt
					.next();
			try {

				String schoolName = jedis.hget("schoolInfo", entry.getKey());
				List<HashMap<String, Integer>> dptCountList = entry.getValue();

				int size = dptCountList.size();
				List<HashMap<String, Integer>> dptCountTempList = new ArrayList<HashMap<String, Integer>>();
				for (int i = 0; i < size; i++) {
					Iterator<Map.Entry<String, Integer>> dptIt = dptCountList
							.get(i).entrySet().iterator();
					while (dptIt.hasNext()) {
						HashMap<String, Integer> dptCountTempMap = new HashMap<String, Integer>();
						Map.Entry<String, Integer> dptEntry = dptIt.next();
						String dptName = jedis.hget("dptInfo",
								dptEntry.getKey());

						dptCountTempMap.put(dptName, dptEntry.getValue());
						dptCountTempList.add(dptCountTempMap);
					}
				}

				result.put(schoolName, dptCountTempList);

			} finally {
				pool.returnResource(jedis);
			}

		}
		
		pool.destroy();

		return result;
	}

}
