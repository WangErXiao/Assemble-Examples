package concurrent.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 15-3-22.
 */
public class CountDownLatchTest {
    public static void main(String[]args) throws InterruptedException {
        ExecutorService executorService= Executors.newCachedThreadPool();
        CountDownLatch latch=new CountDownLatch(2);
        executorService.submit(new LatchTask(latch));
        executorService.submit(new LatchTask(latch));

        latch.await();
        System.out.println("finish------");
        executorService.shutdown();


    }
    public static class LatchTask implements Runnable{
        private CountDownLatch latch;
        public LatchTask(CountDownLatch latch) {
            this.latch=latch;
        }
        @Override
        public void run() {
            System.out.println("starting --------");
            try {
                TimeUnit.SECONDS.sleep((int)(Math.random()*10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println("end--------------");
        }
    }
}
