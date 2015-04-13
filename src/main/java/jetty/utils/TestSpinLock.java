package jetty.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaozb on 15-4-13.
 * 测试代码
 */
public class TestSpinLock implements Runnable {
    private SpinLock spinLock=new SpinLock();
    public int counter=0;
    private void incr(){
        try(SpinLock.Lock lock=spinLock.lock()) {
            counter++;
        }
    }
    public static void main(String[]args) throws InterruptedException {
        TestSpinLock testSpinLock=new TestSpinLock();
        ExecutorService executorService= Executors.newCachedThreadPool();
        executorService.submit(testSpinLock);
        executorService.submit(testSpinLock);
        executorService.submit(testSpinLock);
        executorService.shutdown();
        executorService.awaitTermination(1000, TimeUnit.SECONDS);
        System.out.println(testSpinLock.counter);
    }
    @Override
    public void run() {
        for(int i=0;i<1000000;i++){
            incr();
        }
    }
}
