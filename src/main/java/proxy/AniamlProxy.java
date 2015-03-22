package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by root on 15-3-22.
 */
public class AniamlProxy implements InvocationHandler {
    private Object target;
    public AniamlProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object r =   method.invoke(target,args);
        System.out.println("after");
        return r;
    }
}
