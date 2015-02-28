package cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 15-2-28.
 */
public class CacheFactory {
    // 定义一个 CacheManager Map，用于存放目标类与 CacheManager 的对应关系（一个目标类对应一个 CacheManager），目标类一般为 Service 类
    private static final Map<Class<?>, CacheManager> cacheManagerMap = new HashMap<Class<?>, CacheManager>();


    public static Iterable<CacheManager> getCacheManagers() {
        return cacheManagerMap.values();
    }



}
