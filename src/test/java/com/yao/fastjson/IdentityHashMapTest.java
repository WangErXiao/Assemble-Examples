package com.yao.fastjson;

import junit.framework.TestCase;

/**
 * Created by yao on 15/7/11.
 */
public class IdentityHashMapTest extends TestCase {

    public void testGet() throws Exception {

        String a=new String("yao100");
        String b=new String("yao100");

        System.out.println(a.equals(b));
        System.out.println(a==b);
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));

        IdentityHashMap<Object,Object>map=new IdentityHashMap<>();
        Object x=new Object();
        map.put(x,x);
        System.out.println(map.get(x));
        System.out.println(map.get(new Object()));
        map.put(Class.forName("java.awt.Point"), "xxx");
        System.out.println(map.get(Class.forName("java.awt.Point")));
    }
}