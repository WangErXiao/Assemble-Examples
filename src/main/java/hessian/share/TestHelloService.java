package hessian.share;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by root on 15-3-11.
 */
public class TestHelloService {
    public static void main(String[]args){
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/hessian/spring-hessian-client.xml");
        HelloService helloService=(HelloService)context.getBean("helloService");
        long start=System.currentTimeMillis();
        System.out.println(helloService.sayHello("yao"));
        System.out.println("time consumer:"+(System.currentTimeMillis()-start));
    }
}
