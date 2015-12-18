package collections;

import java.util.concurrent.*;

/**
 * Created by yao on 15/8/5.
 */
public class DelayQueueTest {

    public static void main(String[]args) throws InterruptedException {
        DelayQueue<MyElement> delayQueue=new DelayQueue();
        ExecutorService service= Executors.newFixedThreadPool(4);

        service.submit(new Runnable() {
            @Override
            public void run() {
                delayQueue.offer(new MyElement(9));
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                delayQueue.offer(new MyElement(10));
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(delayQueue.take().getDelay(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        service.awaitTermination(1000,TimeUnit.SECONDS);
        service.shutdown();

    }



    private static class  MyElement implements Delayed{


        private long delay;

        public MyElement(long delay) {
            this.delay = delay;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return delay;
        }
        @Override
        public int compareTo(Delayed o) {
            return delay-o.getDelay(null)>=0?-1:1;
        }
    }

}
