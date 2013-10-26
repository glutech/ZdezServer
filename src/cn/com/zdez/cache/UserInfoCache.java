package cn.com.zdez.cache;

import cn.com.zdez.dao.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class UserInfoCache {
	
	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();
	
	public void cacheSchoolAdminInfo() {
		
	}

}
