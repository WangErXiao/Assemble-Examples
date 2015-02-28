package cache.impl;

import cache.Cache;
import cache.Duration;
import cache.Expiry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 15-2-28.
 */
public class DefaultCache<K,V> implements Cache<K,V> {

    private final  Map<K,V>dataMap=new ConcurrentHashMap<K, V>();

    private final Map<K,Duration> durationMap=new ConcurrentHashMap<K, Duration>();

    @Override
    public V get(K key) {
        if(key==null){
            throw new NullPointerException("key can't be null");
        }
        return dataMap.get(key);
    }

    @Override
    public void put(K key, V value) {
        if(key==null){
            throw new NullPointerException("key can't be null");
        }
        dataMap.put(key,value);
    }

    @Override
    public void put(K key, V value, long expiry) {
        put(key,value);
        durationMap.put(key,new Duration(System.currentTimeMillis(), expiry));
    }

    @Override
    public void remove(K key) {
        if(key==null){
            throw new NullPointerException("key can't be null");
        }
        dataMap.remove(key);
        durationMap.remove(key);
    }
    @Override
    public void clear() {
        dataMap.clear();
        durationMap.clear();
    }
    @Override
    public Map<K, Duration> getDurations() {
        return durationMap;
    }
}
