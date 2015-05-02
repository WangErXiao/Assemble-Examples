package concurrent.thread;

import java.util.concurrent.ThreadFactory;

/**
 * Created by robin on 5/2/15.
 */
public class DefaultThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(r);
        if(!thread.isDaemon()){
            thread.setDaemon(true);
        }
        return thread;
    }
}
