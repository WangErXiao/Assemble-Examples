package reflect.asm;

/**
 * Created by yaozb on 15-4-9.
 */
public class AopInterceptor {
    public static void beforeInvoke(){
        System.out.println("before");
    }

    public static void afterInvoke(){
        System.out.println("after");
    }
}
