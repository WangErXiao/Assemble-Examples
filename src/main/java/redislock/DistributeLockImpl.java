package redislock;

import cache.Expiry;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * Created by root on 15-3-18.
 */
public class DistributeLockImpl implements DistributeLock {
    private static long timeout=10*1000;
    private static long expireTime=100*1000;
    static {
        RedisUtil.initPool(new RedisConfig(){{
            setHost("127.0.0.1");
            setPassword("123456");
            setPort(6379);
            setTimeout(100000);
        }});
    }
    @Override
    public void lock(String key) {
        long startTime=System.currentTimeMillis();
        Jedis jedis=RedisUtil.getJedisPool().getResource();
        try {
            if (jedis.setnx(key, System.currentTimeMillis() + timeout + "") > 0) {
                return;
            } else {
                while (true) {
                    String curExpireTimeOne = jedis.get(key);
                    if (curExpireTimeOne == null || curExpireTimeOne.trim().equals("")
                            || System.currentTimeMillis() > Long.valueOf(curExpireTimeOne)) {
                        String curExpireTimeTwo = jedis.getSet(key, System.currentTimeMillis() + timeout + "");
                        if (curExpireTimeTwo == null || curExpireTimeTwo.trim().equals("")
                                || System.currentTimeMillis() > Long.valueOf(curExpireTimeTwo)) {
                            if ((curExpireTimeOne == null && curExpireTimeTwo == null) ||
                                    (curExpireTimeOne.trim().equals(curExpireTimeOne.trim()))) {
                                return;
                            }
                        }
                    } else {
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    if((System.currentTimeMillis()-startTime)>expireTime){
                        throw  new RuntimeException("get lock time expire-----");
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("get lock fail");
        }finally {
            RedisUtil.releaseJedis(jedis);
        }
    }

    @Override
    public void unlock(String key) {
        Jedis jedis=RedisUtil.getJedisPool().getResource();
        try {
            String curLockTime = jedis.get(key);
            if (curLockTime != null && Long.valueOf(curLockTime)>System.currentTimeMillis()) {
                jedis.del(key);
            }
            RedisUtil.releaseJedis(jedis);
        }catch (Exception e){
            throw  new RuntimeException("unlock fail");
        }
    }
}
