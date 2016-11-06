package concurrent.thread;

/**
 * Created by robin on 11/6/16.
 *
 * 参考原文：http://mp.weixin.qq.com/s?__biz=MzIzNjI1ODc2OA==&mid=403257534&idx=1&sn=2015e011c50c0a9107a48aa60a4adb78&scene=21#wechat_redirect
 * 查找cpu消耗过高的线程办法
 *
 * 1 jps 拿到对应java进程的pid
 *
 * 2 top -Hp <pid> 拿到cpu消耗过高的thread pid
 *
 * 3 进制转换   十 to 十六  echo "obase=16;<pid>" | bc
 *
 * 4 jstack <java进程 pid>  | grep <线程 pid 十六进制>
 *
 *
 *
 */
public class FindCpuHighThread {


    public static void main(String args[]) {
        for (int i = 0; i < 10; i++) {
            new Thread("sleep thread") {
                public void run() {
                    try {
                        Thread.sleep(100000);
                    } catch (Exception e) {
                    }
                }
            }.start();
        }
        Thread t = new Thread("calculator thread") {
            public void run() {
                int i = 0;
                while (true) {
                    i = (i++) / 100;
                }
            }
        };
        t.start();
    }
}
