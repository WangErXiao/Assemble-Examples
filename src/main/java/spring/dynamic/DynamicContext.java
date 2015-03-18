package spring.dynamic;

import hessian.share.HelloService;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by root on 15-3-17.
 */
public class DynamicContext {

    private ApplicationContext applicationContext=null;//new ClassPathXmlApplicationContext("/dynamic/spring.xml");

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public DynamicContext(String[]configLocations){
        init(configLocations);
    }
    public boolean init(String[]configLocations){
        try {
            XmlWebApplicationContext context = new XmlWebApplicationContext();
            //context.setParent(applicationContext);
            //BeanFactory beanFactory=new DefaultListableBeanFactory();
            context.refresh();
            XmlBeanDefinitionReader beanDefinitionReader =
                    new XmlBeanDefinitionReader((BeanDefinitionRegistry) context.getBeanFactory());
            beanDefinitionReader.setResourceLoader(context);
            beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(context));
            for (String config : configLocations) {
                beanDefinitionReader.loadBeanDefinitions(context.getResource(config));
            }
            applicationContext=context;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static void main(String[]args){
        DynamicContext dynamicContext=new DynamicContext(new String[]{"/dynamic/spring-hessian-client.xml"});
        HelloService helloService= (HelloService)dynamicContext.getApplicationContext().getBean("helloService");
        System.out.println(helloService.sayHello("xxx"));
    }
}
