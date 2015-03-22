package concurrent.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by root on 15-3-22.
 */
public class NewCachedThreadPool {
    public static void main(String[]args) throws ExecutionException, InterruptedException {
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Future<String>future0=executorService.submit(new MyTask());
        final Future<String>future1=executorService.submit(new MyTask());
        final Future<String>future2=executorService.submit(new MyTask());
        final Future<String>future3=executorService.submit(new MyTask());
        List<Future<String>> futures=new ArrayList<Future<String>>(){{
            int i=0;
            while (i<1000){
                add(future1);
                i++;
            }
/*            add(future0);
            add(future1);
            add(future2);
            add(future3);*/
        }};
        while (true){
            Future<String>tmp=null;
            for (Future<String>future:futures){
                if(future.isDone()){
                    tmp=future;
                    System.out.println(future.get());
                }
            }
            if(tmp!=null){
                futures.remove(tmp);
            }
            if(futures.size()==0){
                break;
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
        executorService.shutdown();
    }
    private static class MyTask implements Callable<String>{
        @Override
        public String call() throws Exception {
            int xx=(int)(Math.random()*10+5);
            TimeUnit.SECONDS.sleep(xx);
            return xx+"";
        }
    }
}
