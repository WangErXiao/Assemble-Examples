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
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("---------------");
            }
        },1000,1000, TimeUnit.MILLISECONDS);
    }
}
