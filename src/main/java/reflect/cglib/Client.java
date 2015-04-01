package reflect.cglib;

/**
 * Created by root on 15-4-1.
 */
public class Client {
    public static void main(String[]args){
       // BookServiceBean serviceBean=BookServiceFactory.getInstance();
         BookServiceBean serviceBean=BookServiceProxyFactory.getInstance(new MyCglibProxy("wangerxiao"));
         doMethod(serviceBean);


    }
    public static void doMethod(BookServiceBean service){
        service.create();
        service.update();
        service.query();
        service.delete();
    }
}
