package jetty.utils;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaozb on 15-4-18.
 */
public class FutureCallbackTest {
    @Test
    public void testFuture() throws ExecutionException, InterruptedException {
        FutureCallback callback=new FutureCallback();
        Thread thread=new Thread(new MyTask(callback));
        thread.start();
        callback.get();
        System.out.println("finish--------");
    }
    public static class MyTask implements Runnable{
        private Callback callback;
        MyTask(Callback callback){
            this.callback=callback;
        }
        @Override
        public void run() {
            System.out.println("start process task----------");
            try {
                TimeUnit.SECONDS.sleep(1);
                //throw  new Exception("异常了");
                System.out.println("finish process task----------");
                callback.succeeded();
            } catch (Exception e) {
                callback.failed(e);
            }
        }
    }
}
