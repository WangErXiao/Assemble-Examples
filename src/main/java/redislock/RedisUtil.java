package redislock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by root on 15-3-18.
 */
public class RedisUtil {
    /**
     * 初始化redis pool用
     */
    private static Object mutex=new Object();
    private static volatile JedisPool jedisPool;
    public static JedisPool getJedisPool() {
        if(jedisPool==null){
            throw new RuntimeException("redis pool not initialized");
        }
        return jedisPool;
    }

    public static void initPool(RedisConfig config){
        if(jedisPool!=null){
            return ;
        }
        synchronized (mutex){
            if(jedisPool!=null){
                return ;
            }
            jedisPool=new JedisPool(config(),
                    config.getHost(),config.getPort(),config.getTimeout(),config.getPassword());
        }
    }
    public static void releaseJedis(Jedis jedis) {
        if(jedisPool == null) {
            throw new RuntimeException("jedis pool is not initialization!");
        }
        jedisPool.returnResource(jedis);
    }
    private static JedisPoolConfig config() {
        JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
        config.setMaxIdle(100);
        config.setMinIdle(0);
        config.setMinEvictableIdleTimeMillis(600000);
        config.setSoftMinEvictableIdleTimeMillis(-1);
        config.setTimeBetweenEvictionRunsMillis(600000);
        return config;
    }
}
