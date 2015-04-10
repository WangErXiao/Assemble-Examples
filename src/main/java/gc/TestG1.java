package gc;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by yaozb on 15-4-10.
 */
public class TestG1 {
    /**
     * 1 vm args: -XX:+PrintGCDetails -XX:+UseG1GC  -Xloggc:/home/yao/tmp/gclogs
     * 2 vm args: -XX:+PrintGCDetails -Xloggc:/home/yao/tmp/gclogs
     *
     *
      * @param args
     */
    public static void main(String[]args) throws InterruptedException {
        while (true){
            method();
            System.out.println("-----------------------");
            Thread.sleep(100);
        }
    }
    private static void method(){
        String[]str=new String[1024*10];
    }
}
