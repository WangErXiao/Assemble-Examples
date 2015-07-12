package com.yao.dubbo;

import junit.framework.TestCase;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by yao on 15/7/12.
 */
public class ClassGeneratorTest extends TestCase {

    @Test
    public void testNewInstance() throws Exception {

        ClassGenerator classGenerator=ClassGenerator.newInstance();

        classGenerator.setClassName("Person");

        classGenerator.addField("name", Modifier.PRIVATE, String.class);

        classGenerator.addField("age", Modifier.PRIVATE, Integer.class);

        classGenerator.addConstructor(Modifier.PUBLIC, new Class[]{String.class, Integer.class},
                "this.name=arg0;this.age=arg1;");

        classGenerator.addMethod("sayHello",Modifier.PUBLIC,String.class,new Class[]{},"return name+age + \"  hello world\";");

        Class Person=classGenerator.toClass();

        Constructor constructor= Person.getConstructor(String.class, Integer.class);

        Object person=constructor.newInstance("yao", 23);

        Method sayHello=Person.getMethod("sayHello");

        System.out.println(sayHello.invoke(person));

    }
}