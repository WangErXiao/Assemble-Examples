package reflect;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yao on 15/7/9.
 */
public class TypeTest {
    public static void main(String[]args) throws NoSuchMethodException {
        A a=new Aimpl("xx");
        Type type1=a.getClass().getGenericSuperclass();
        System.out.println( type1 instanceof ParameterizedType  );
        Type[] params = ((ParameterizedType) type1).getActualTypeArguments();
        if(params!=null&&params.length>0){
            System.out.println(params[0].getTypeName());

        }
    }

    public static abstract class A <T>{
       abstract void  say(T name);
    }

    public static class  Aimpl extends A<String>  {
        private String xx;
        public Aimpl(String xx){
            this.xx=xx;
        }
        public void say(String name) {
            System.out.println("hello "+name);
        }
    }
}
