package cn.com.zdez.cache;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.com.zdez.dao.RedisConnection;
import cn.com.zdez.po.Department;
import cn.com.zdez.po.School;
import cn.com.zdez.service.DepartmentService;
import cn.com.zdez.service.SchoolService;

public class SchoolInfoCache {

	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();

	/**
	 * 缓存学校信息，包括学校id和学校名称
	 */
	public void SchoolCache() {

		List<School> schoolList = new SchoolService().getAll();
		String key = "schoolInfo";
		for (int i = 0; i < schoolList.size(); i++) {
			jedis.hset(key, Integer.toString(schoolList.get(i).getId()),
					schoolList.get(i).getName());
		}

	}

	/**
	 * 缓存学院信息，包括学院id和学院名称。同时缓存学院与学校的对应关系
	 */
	public void DepartmentInfoCache() {

		List<Department> departmentList = new DepartmentService().getAll();
		String key = "dptInfo";
		String key2 = "schoolIdList";
		int size = departmentList.size();
		for (int i = 0; i < size; i++) {
			String key1 = "school:";
			String dptIdStr = Integer.toString(departmentList.get(i).getId());

			jedis.hset(key, dptIdStr, departmentList.get(i).getName());

			String schoolIdStr = Integer.toString(departmentList.get(i)
					.getSchoolId());

			key1 = key1 + departmentList.get(i).getSchoolId() + ":dpt";

			jedis.sadd(key2, schoolIdStr);
			jedis.sadd(key1, dptIdStr);
		}

	}

}
