package collections;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by root on 15-3-27.
 */
public class PropertiesTest {
    public static void main(String[]args) throws IOException {
        String path=Thread.currentThread().getClass().getResource("/").toString().substring(5)+"xx.ini";
        Properties properties=new Properties();
        properties.load(new FileInputStream(path));
        System.out.println(Thread.currentThread().getClass().getResource("/"));
        System.out.println(properties.get("name"));
        properties.setProperty("xx","ooo");
        properties.store(new FileOutputStream(path),"xx");
    }
}
