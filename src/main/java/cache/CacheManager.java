package cache;


/**
 * Created by root on 15-2-28.
 */
public interface CacheManager {

    Iterable<Cache> getCaches();

    <K,V> Cache<K,V> createCache(String cacheName);

    <K,V> Cache<K,V> getCache(String cacheName);

    void destroyCache(String cacheName);

}
