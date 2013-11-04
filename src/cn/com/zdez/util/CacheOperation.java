package cn.com.zdez.util;

import java.util.Iterator;
import java.util.Set;

import cn.com.zdez.dao.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CacheOperation {
	
	private JedisPool pool = new RedisConnection().getConnection();
	private Jedis jedis = pool.getResource();
	
	public void addIsTopToExistedNews() {
		try {
			
			Set<String> newsIdList = jedis.smembers("newsMsg:idList");
			Iterator<String> it = newsIdList.iterator();
			while (it.hasNext()) {
				jedis.hset("newsMsg:" + it.next(), "isTop", "0");
			}
			
		} finally {
			pool.returnResource(jedis);
		}
		
		pool.destroy();
	}

}
