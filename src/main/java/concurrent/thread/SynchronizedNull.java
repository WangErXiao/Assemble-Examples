package concurrent.thread;

/**
 * Created by yao on 16/2/23.
 */
public class SynchronizedNull {


    //synchronized null会报错 java.lang.NullPointerException
    private Object lock=null;

    //private Object lock=new Object();

    public static  void main(String[]args){

        SynchronizedNull synchronizedNull=new SynchronizedNull();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizedNull.sayHello();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizedNull.sayHello();
            }
        }).start();

    }

    public void sayHello(){
        synchronized (lock){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("say Hello , thread Id"+Thread.currentThread().getId());
        }
    }
}
