package classloader;

/**
 * Created by root on 15-3-22.
 */
public class Super {
    static {
        System.out.println("Super static block");
    }
    public Super(){
        System.out.println("Super init block");
    }
}
