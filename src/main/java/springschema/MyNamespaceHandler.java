package springschema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by root on 15-3-23.
 */
public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("people",new PeopleBeanDefinitionParser());
    }
}
