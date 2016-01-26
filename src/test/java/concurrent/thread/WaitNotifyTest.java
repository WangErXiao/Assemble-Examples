package concurrent.thread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by yao on 16/1/26.
 */
public class WaitNotifyTest {

    @Test
    public void testLock() throws Exception {
        WaitNotify waitNotify=new WaitNotify();
        Thread one=new Thread(new Runnable() {
            @Override
            public void run() {
                waitNotify.waitM();
                System.out.println("one enter");
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("one out ");
                }
            }
        });
        Thread two=new Thread(new Runnable() {
            @Override
            public void run() {
                waitNotify.waitM();
                System.out.println("two enter");
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("two out ");
                }
            }
        });
        one.start();
        two.start();
        waitNotify.notifyM();
        waitNotify.notifyM();
        one.join();
        two.join();
    }
}