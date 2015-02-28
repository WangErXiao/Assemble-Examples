package cache;

/**
 * Created by root on 15-2-28.
 */
public class CacheException extends RuntimeException {

    public CacheException(){
        super();
    }
    public CacheException(String s){
        super(s);
    }
    public CacheException(Throwable e){
        super(e);
    }
    public CacheException(String s, InterruptedException e) {
        super(s,e);
    }
}
