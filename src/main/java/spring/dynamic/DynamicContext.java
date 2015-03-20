package spring.dynamic;

import hessian.share.HelloService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by root on 15-3-17.
 */
public class DynamicContext {

    // private final Log logger = LogFactory.getLog(getClass());
    private ApplicationContext applicationContext;

    private String dynamicPath;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public <T> T getBean(String bean, Class<T> clazz) {
        return (T) applicationContext.getBean(bean);
    }

    public Object getBean(String bean) {
        return applicationContext.getBean(bean);
    }

    public <T> Object getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public ConfigurableApplicationContext getConfigurableContext() {
        return (ConfigurableApplicationContext) applicationContext;
    }


    public boolean loadBean(String[] configLocations) {
        try {
            XmlWebApplicationContext ac = (XmlWebApplicationContext) this.getApplicationContext();

            XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
                    (BeanDefinitionRegistry) ac.getBeanFactory());
            beanDefinitionReader.setResourceLoader(ac);
            beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(ac));
            for (int i = 0; i < configLocations.length; i++) {
                beanDefinitionReader.loadBeanDefinitions(ac.getResources(configLocations[i]));
            }
            // ac.refresh();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getDynamicPath() {
        return dynamicPath;
    }

    public void setDynamicPath(String dynamicPath){
        if (null == dynamicPath || "".equals(dynamicPath)) {
            throw new RuntimeException("dynamicPath注入值未设置");
        }
        if (!dynamicPath.endsWith("/")) {
            dynamicPath += "/";
        }
        this.dynamicPath = dynamicPath;
    }
}
