package concurrent.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by robin on 5/2/15.
 */
public class TestThreadDeathWatcher {

    @Test
    public void testThreadDeath() throws InterruptedException {

        CountDownLatch latch=new CountDownLatch(2);

        ThreadDeathWatcher.watch(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }){{
            start();
        }}, new Runnable() {
            @Override
            public void run() {
                System.out.println("4 seconds thread");
                latch.countDown();
            }

        });

        ThreadDeathWatcher.watch(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }){{start();}}, new Runnable() {
            @Override
            public void run() {
                System.out.println("10 seconds thread");
                latch.countDown();
            }
        });

        latch.await();
    }
}
