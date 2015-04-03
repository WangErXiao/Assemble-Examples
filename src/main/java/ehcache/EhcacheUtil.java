package ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

/**
 * Created by root on 15-4-3.
 */
public class EhcacheUtil {
    private static  final String path="/ehcache/ehcache.xml";
    private URL url;
    private CacheManager manager;
    private static  EhcacheUtil ehCache;
    private EhcacheUtil(){
        url=getClass().getResource(path);
        manager= CacheManager.create(url);
    }
    public static EhcacheUtil getInstance(){
        if(ehCache==null){
            synchronized (EhcacheUtil.class){
                if(ehCache==null){
                    ehCache=new EhcacheUtil();
                }
            }
        }
        return ehCache;
    }
    public void put(String name,String k,Object v){
        Cache cache =manager.getCache(name);
        if(cache==null){
            manager.addCache(name);
            cache=manager.getCache(name);
        }
        Element element=new Element(k,v);
        cache.put(element);
    }
    public Object get(String name,String key){
        Cache cache=manager.getCache(name);
        Element element=cache.get(key);
        return element==null?null:element.getObjectValue();
    }
    public Cache get(String name){
        return manager.getCache(name);
    }
    public void remove(String name,String key){
        Cache cache=manager.getCache(name);
        cache.remove(key);
    }
}
