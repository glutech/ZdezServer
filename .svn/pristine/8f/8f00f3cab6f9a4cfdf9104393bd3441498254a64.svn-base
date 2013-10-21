package cn.com.zdez.dao;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
	
	public JedisPool getConnection() {
		
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
		
		return pool;
	}

}
