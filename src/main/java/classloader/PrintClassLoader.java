package classloader;

/**
 * Created by root on 15-3-20.
 */
public class PrintClassLoader {
    public static void main(String[]args){
        ClassLoader loader=PrintClassLoader.class.getClassLoader();
        while (loader!=null){
            System.out.println(loader);
            loader=loader.getParent();
        }

        System.out.println(loader);

    }
}
