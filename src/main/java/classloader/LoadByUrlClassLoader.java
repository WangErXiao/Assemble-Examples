package classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by root on 15-3-27.
 */

public class LoadByUrlClassLoader {
    public static void main(String[]args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL[]urls=new URL[]{
              new URL("file:///home/yao/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar")
        };
        ClassLoader loader=new URLClassLoader(urls);
        Class clazz= loader.loadClass("org.apache.commons.lang.CharSetUtils");
        System.out.println(clazz.newInstance());
    }
}
