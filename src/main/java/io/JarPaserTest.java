package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by root on 15-3-28.
 */
public class JarPaserTest {
    public static void main(String[]args) throws IOException {
        String jarUrl="/home/yao/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar";
        System.out.println(jarUrl.endsWith("jar"));
        InputStream inputStream=new FileInputStream(jarUrl);
        if(inputStream ==null){
            return;
        }
        JarInputStream jarInputStream=new JarInputStream(inputStream);
        JarEntry entry=null;
        for (entry=jarInputStream.getNextJarEntry();entry!=null;entry=jarInputStream.getNextJarEntry()){
            if(entry.getName().endsWith(".class")){
                System.out.println(entry.getName());
            }
            entry=null;
        }
    }
}
