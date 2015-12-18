package concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yao on 15/12/18.
 * gava 限流工具
 */
public class RateLimterTest {
    RateLimiter rateLimiter=RateLimiter.create(1);
    private AtomicInteger count=new AtomicInteger(0);

    public void run(){
        rateLimiter.acquire();
      /*  try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("finish :"+count.incrementAndGet());
        System.out.println("rate :"+rateLimiter.getRate());
    }
    public static void main(String[]args) throws InterruptedException {
        RateLimterTest test=new RateLimterTest();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Random random=new Random();
                int i= random.nextInt(10);
                System.out.println("random:"+i);
                for (int j=0;j<i;j++){
                    test.run();
                }
            }
        };
        long begin=System.currentTimeMillis();
        Thread one=new Thread(runnable);
        Thread two=new Thread(runnable);
        Thread three=new Thread(runnable);
        Thread four=new Thread(runnable);
        one.start();
        two.start();
        three.start();
        four.start();

        one.join();
        two.join();
        three.join();
        four.join();
        System.out.println("result:"+(System.currentTimeMillis() - begin));
    }

}
