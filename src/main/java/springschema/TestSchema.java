package springschema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by root on 15-3-23.
 */
public class TestSchema {
    public static void main(String[]args){
        ApplicationContext ctx=new ClassPathXmlApplicationContext("classpath*:spring-schema/spring-people.xml");
        People p=(People)ctx.getBean("wangerxiao");
        System.out.println(p.getName());
    }
}
