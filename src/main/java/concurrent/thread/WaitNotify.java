package concurrent.thread;

/**
 * Created by yao on 16/1/26.
 *
 * 通过isNotified 标示位防止notify信号丢失
 *
 * lock中通过while 循环防止伪唤醒（spurious wake)
 * 如果isNotified为false 即使代码往下走 也会再次被阻塞
 *
 */
public class WaitNotify {

    private Object obj=new Object();

    private boolean isNotified=false;

    public void waitM(){
        synchronized (obj) {
            while (!isNotified) {
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isNotified = false;
        }
    }
    public void notifyM(){
        synchronized (obj) {
            isNotified = true;
            obj.notify();
        }
    }

}
