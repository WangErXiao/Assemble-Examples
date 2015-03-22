package concurrent.threadpool;

import java.util.concurrent.*;

/**
 * Created by root on 15-3-22.
 */
public class NewSingleThreadExecutor {
    public static void main(String[]args) throws ExecutionException, InterruptedException {

        ExecutorService executorService= Executors.newSingleThreadExecutor();
        Future<String>future= executorService.submit(new Task());
        while (true){
            if(future.isDone()){
                System.out.println(future.get());
                break;
            }else {
                System.out.println("loop-----------");
                TimeUnit.MILLISECONDS.sleep(400);
            }

        }
        executorService.shutdown();
    }
    private static class Task implements Callable<String>{
        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(3);
            return "finish---------";
        }
    }
}
