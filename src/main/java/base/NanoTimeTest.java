package base;

/**
 * Created by yao on 15/7/10.
 */
public class NanoTimeTest {
    public static void main(String[]args){
        long start=System.currentTimeMillis();
        for (int i=0;i<1000*1000;i++){
            System.nanoTime();
        }
        System.out.println(System.currentTimeMillis()-start);
        start=System.currentTimeMillis();
        for (int i=0;i<1000*1000;i++){
            System.currentTimeMillis();
        }
        System.out.println(System.currentTimeMillis()-start);
    }
}
