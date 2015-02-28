package cache;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 15-2-28.
 */
public class CacheThread  extends Thread{

    @Override
    public void run() {
        try {
            while (true) {
                // 遍历所有的 Cache Manager
                Iterable<CacheManager> cacheManagers = CacheFactory.getCacheManagers();
                for (CacheManager cacheManager : cacheManagers) {
                    // 遍历所有的 Cache
                    Iterable<Cache> caches = cacheManager.getCaches();
                    for (Cache cache : caches) {
                        // 遍历所有的 Duration Map
                        Map<Object, Duration> durationMap = cache.getDurations();
                        for (Object entrySet : durationMap.entrySet()) {
                            // 获取 Duration Map 中的 key 与 value
                            Map.Entry<Object, Duration> entry = (Map.Entry<Object, Duration>) entrySet;
                            Object cacheKey = entry.getKey();
                            Duration duration = entry.getValue();
                            // 获取 Duration 中的相关数据
                            long start = duration.getStart();   // 开始时间
                            long expiry = duration.getExpiry(); // 过期时长
                            // 获取当前时间
                            long current = System.currentTimeMillis();
                            // 判断是否已过期
                            if (current - start >= expiry) {
                                // 若已过期，则首先移除 Cache（也会同时移除 Duration Map 中对应的条目）
                                cache.remove(cacheKey);
                            }
                        }
                    }
                }
                // 使线程休眠 5 秒钟
                sleep(5000);
            }
        } catch (InterruptedException e) {
            throw new CacheException("错误：运行 CacheThead 出现异常！", e);
        }
    }
}
