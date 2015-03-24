package btrace;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 15-3-24.
 */
public class LoadProxyClass {
    public static void main(String[]args) throws InterruptedException {
        while (true){
            Enhancer enhancer=new Enhancer();
            enhancer.setSuperclass(Target.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invoke(o,objects);
                }
            });
            TimeUnit.SECONDS.sleep(2);
        }
    }
    static class Target{
    }
}
