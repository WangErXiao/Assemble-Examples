package reflect.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * Created by root on 15-4-1.
 */
public class BookServiceProxyFactory {
    private static BookServiceBean serviceBean=new BookServiceBean();
    private BookServiceProxyFactory( ){}
    public static BookServiceBean getInstance(
            MyCglibProxy proxy
    ){
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(BookServiceBean.class);
        en.setCallback(proxy);
        return (BookServiceBean)en.create();
    }
}
