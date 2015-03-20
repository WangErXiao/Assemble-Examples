package redislock;

import redis.clients.jedis.Jedis;

/**
 * Created by root on 15-3-18.
 */
public class TestRedis {
    public static void main(String[]args){
        RedisUtil.initPool(new RedisConfig(){{
            setHost("127.0.0.1");
            setPassword("123456");
            setPort(6379);
            setTimeout(100000);
        }});
        Jedis jedis=RedisUtil.getJedisPool().getResource();
        System.out.println(jedis.get("xxx"));
        RedisUtil.releaseJedis(jedis);
    }
}
