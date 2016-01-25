package concurrent.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 15-3-28.
 */
public class ScheduledExecutorServiceTest {
    public static void main(String []args){
 /*
        ExecutorService executorService= Executors.newSingleThreadScheduledExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("-------------");
            }
        });*/
        ScheduledExecutorService  executorService= Executors.newSingleThreadScheduledExecutor();
        // 不会把任务执行的时间算进去,到点就起线程运行任务
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("--------------- start 1-----");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--------------- end 1-----");
            }
        },4,4, TimeUnit.SECONDS);

        // 会把任务执行时间算进去，一个任务执行完后＋延迟时间  才去执行另外一个任务 单线程即可搞定

        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("--------------- start 2-----");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--------------- end 2-----");
            }
        },4,4, TimeUnit.SECONDS);










    }






}
