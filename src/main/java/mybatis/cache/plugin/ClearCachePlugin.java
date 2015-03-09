package mybatis.cache.plugin;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 15-3-5.
 */
public class ClearCachePlugin implements Interceptor {

    private Properties properties;

    private Map<String,Set<String>> cacheKeysMap=new ConcurrentHashMap<String ,Set<String>>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties=properties;



    }
}
