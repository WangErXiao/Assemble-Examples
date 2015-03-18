package redislock;

/**
 * Created by root on 15-3-18.
 */
public class TestLock {
    public static void main(String[]args) throws InterruptedException {
        DistributeLock distributeLock=new DistributeLockImpl();

        distributeLock.lock("my");
        new MyThread().start();
        Thread.sleep(5000);
        System.out.println("main--------");
        Thread.sleep(500);
        distributeLock.unlock("my");


    }
    private static class MyThread extends Thread{
        @Override
        public void run() {
            DistributeLock distributeLock=new DistributeLockImpl();
            distributeLock.lock("my");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("-------"+Thread.currentThread().getId());
            distributeLock.unlock("my");
        }
    }
}
