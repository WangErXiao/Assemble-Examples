package base;

/**
 * Created by yao on 15/7/11.
 */
public class IdentityHashCodeTest {
    public static void main(String[]args){
        Object obj=new Object();
        System.out.println(System.identityHashCode(obj));
        System.out.println(obj.hashCode());
    }
}
