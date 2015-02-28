package cache;

/**
 * Created by root on 15-2-28.
 */
public class Duration {
    private long start;  // 开始时间
    private long expiry; // 过期时长（毫秒）

    public Duration(long start, long expiry) {
        this.start = start;
        this.expiry = expiry;
    }

    public long getStart() {
        return start;
    }

    public long getExpiry() {
        return expiry;
    }

}
