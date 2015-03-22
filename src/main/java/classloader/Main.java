package classloader;

/**
 * Created by root on 15-3-22.
 */
public class Main extends Super {
    static {
        System.out.println("main static block");
    }
    public Main(){
        System.out.println("main ini block");
    }
}
