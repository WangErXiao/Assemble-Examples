package classloader;

import java.net.URL;

/**
 * Created by root on 15-3-20.
 */
public class PrintJar {

    public static void main(String[]args){
        URL[]urls=sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url:urls){
            System.out.println(url.toExternalForm());
        }
        System.out.println(System.getProperty("sun.boot.class.path"));
    }
}
