package jetty.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by yaozb on 15-4-19.
 */
public class Loader {

    public static URL getResource(Class<?> loadClass,String name){
        URL url=null;
        ClassLoader context_loader=Thread.currentThread().getContextClassLoader();
        if(context_loader!=null)
            url=context_loader.getResource(name);
        if(url==null&&loadClass!=null){
            ClassLoader load_loader=loadClass.getClassLoader();
            if(load_loader!=null&&load_loader!=context_loader){
                url=load_loader.getResource(name);
            }
        }
        if(url==null)
            url=ClassLoader.getSystemResource(name);
        return url;
    }
    public static Class loadClass(Class loadClass,String name) throws ClassNotFoundException {
        ClassNotFoundException ex=null;
        Class<?>c=null;
        ClassLoader context_loader=Thread.currentThread().getContextClassLoader();
        if(context_loader!=null){
            try {
                c=context_loader.loadClass(name);
            }catch (ClassNotFoundException e){
                ex=e;
            }
        }
        if(c==null && loadClass!=null){
            ClassLoader load_loader=loadClass.getClassLoader();
            if(load_loader!=null&&load_loader!=context_loader){
                try{
                    c=load_loader.loadClass(name);
                }catch (ClassNotFoundException e){
                    if(ex==null)
                        ex=e;
                }
            }
        }
        if(c==null){
           try{
               c=Class.forName(name);
           } catch (ClassNotFoundException e){
               if(ex!=null)
                   throw ex;
               throw e;
           }
        }
        return  c;
    }
    public static String getClassPath(ClassLoader loader){
        StringBuilder classpath=new StringBuilder();
        while(loader!=null&&(loader instanceof URLClassLoader)){
            URL[]urls=((URLClassLoader)loader).getURLs();
            if(urls!=null){
                for(URL url:urls){
                    File file= new File(url.getFile());
                    if(file!=null&&file.exists()){
                        if(classpath.length()>0){
                            classpath.append(File.pathSeparatorChar);
                        }
                        classpath.append(file.getAbsoluteFile());
                    }
                }
            }
            loader=loader.getParent();
        }
        return classpath.toString();
    }
}
