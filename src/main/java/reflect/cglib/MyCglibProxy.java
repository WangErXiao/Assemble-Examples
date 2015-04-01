package reflect.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by root on 15-4-1.
 */
public class MyCglibProxy implements MethodInterceptor{
    public Enhancer enhancer=new Enhancer();
    private String name;
    public MyCglibProxy(String name){
        this.name=name;
    }

    public Object getDaoBean(Class cls){
        enhancer.setSuperclass(cls);
        enhancer.setCallback(this);
        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("invoke method:"+method.getName());
        if(!"wangerxiao".equals(name)){
            System.out.println("no access authority");
            return null;
        }
        Object result=methodProxy.invokeSuper(o,objects);
        return result;
    }
}
