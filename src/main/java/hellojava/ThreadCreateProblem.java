package hellojava;

/**
 在执行此段代码时，可能会出现java.lang.OutOfMemoryError: unable to create new native thread，
 请问如出现此异常，可能是什么原因造成的？
 如将上面的i<20000改为i<40000，再执行时（慎重执行此操作），
 同样可能会出现java.lang.OutOfMemoryError: unable to create new native thread，
 并且此时在shell中执行ls等操作时，可能会出现Resource temporarily unavailable，可能是什么原因造成的？
 * vm args: -Xss1m
 */
public class ThreadCreateProblem {
    public static void main(String[]args) {
        for (int i = 0; i < 10000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(100000);
                    } catch (Exception e) {

                    }
                }
            }).start();
        }
    }
}
