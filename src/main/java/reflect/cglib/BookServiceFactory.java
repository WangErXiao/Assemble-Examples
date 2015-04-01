package reflect.cglib;

/**
 * Created by root on 15-4-1.
 */
public class BookServiceFactory {
    private static BookServiceBean serviceBean=new BookServiceBean();
    private BookServiceFactory(){}
    public static BookServiceBean getInstance(){
        return serviceBean;
    }
}
