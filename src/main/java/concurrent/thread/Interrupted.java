package concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by yao on 15/12/23.
 */
public class Interrupted {

    public static void main(String[]args) throws InterruptedException {

        Thread sleepThread=new Thread(new SleepRunner());
        Thread busyThread=new Thread(new BusyRunner());
        sleepThread.setDaemon(true);
        sleepThread.setName("sleep");
        busyThread.setDaemon(true);
        busyThread.setName("busy");
        System.out.println("start---==================");
        sleepThread.start();
        busyThread.start();

        //TimeUnit.MILLISECONDS.sleep(100);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("interrupt---==================");
        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());


        TimeUnit.SECONDS.sleep(2000);

    }


    static class SleepRunner implements Runnable{

        @Override
        public void run() {

            while (true){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class BusyRunner implements Runnable{

        @Override
        public void run() {

            while (true){
                //System.out.println("running----");
                //Thread.interrupted();
            }
        }
    }


}
