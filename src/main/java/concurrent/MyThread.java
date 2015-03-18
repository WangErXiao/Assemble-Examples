package concurrent;

/**
 * Created by root on 15-3-18.
 */
public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("run-------------==============");
    }

    public static void main(String[]args) throws InterruptedException {

        MyThread myThread=new MyThread();
        myThread.start();
        Thread.currentThread().sleep(4000);
        myThread.start();

    }
}
