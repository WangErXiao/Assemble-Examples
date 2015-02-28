package cache;

import java.util.Map;

/**
 * Created by root on 15-2-28.
 */
public interface Cache<K,V> {

    V get(K key);

    void put(K key,V value);

    void put(K key, V value,long expiry);

    void remove(K key);

    void clear();

    Map<K,Duration> getDurations();

}
