package com.yao.fastjson;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yao on 15/7/10.
 */
public class TypeUtils {

    static {
        try {
            System.out.println(Thread.currentThread().getClass().getResource("/prop.ini").getPath());
            System.getProperties().load(new FileInputStream(
                    new File(Thread.currentThread().getClass().getResource("/prop.ini").getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String prop=System.getProperty("com.yao");

        System.out.println(prop);
    }


    public static final Byte castToByte(Object value){
        if(value==null){
            return null;
        }

        if(value instanceof Number){
            return ((Number) value).byteValue();
        }


        if (value instanceof  String){
            String strVal=(String)value;
            if(strVal.length()==0){
                return null;
            }
            if("null".equals(strVal)||"NULL".equals(strVal)){
               return null;
            }
            return Byte.parseByte(strVal);
        }
        throw new RuntimeException("can not cast to byte");
    }



    public static final <T> T cast(Object obj,Class<T> clazz){

        if (obj==null){
            return null;
        }
        if(clazz==null){
            throw  new IllegalArgumentException("clazz is null");
        }


        if (clazz.isArray()) {
            if (obj instanceof Collection) {

                Collection collection = (Collection) obj;
                int index = 0;
                Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                for (Object item : collection) {
                    Object value = cast(item, clazz.getComponentType());
                    Array.set(array, index, value);
                    index++;
                }

                return (T) array;
            }

            if (clazz == byte[].class) {
                return (T) castToBytes(obj);
            }
        }

        if (clazz.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }



        return null;


    }



    public static final byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        if (value instanceof String) {
            return Base64.decodeFast((String) value);
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static void main(String []args){
        System.out.println("test ------");

        String xx="xxx";

        System.out.println(xx instanceof String);

        System.out.println(castToByte("124"));

        System.out.println(xx.getClass().isAssignableFrom(Object.class));
        System.out.println(Object.class.isAssignableFrom(xx.getClass()));
        System.out.println(xx.getClass().isAssignableFrom(String.class));
        System.out.println(Integer.class.isAssignableFrom(Number.class));
        System.out.println(Number.class.isAssignableFrom(Integer.class));


        //List<String> list=new ArrayList<>();
        //list.add("xx");
        String[]list={"xx"};
        System.out.println(list.getClass().getComponentType());

    }

}
