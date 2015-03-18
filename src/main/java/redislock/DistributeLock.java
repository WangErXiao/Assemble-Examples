package redislock;

/**
 * Created by root on 15-3-18.
 */
public interface DistributeLock {
    public void lock(String key);
    public void unlock(String key);
}
