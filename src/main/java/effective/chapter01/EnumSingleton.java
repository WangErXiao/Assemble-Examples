package effective.chapter01;

/**
 * Created by root on 15-3-9.
 */
public enum  EnumSingleton {

    INSTANCE;

    private String field="yao";

    public void sayHello(){
        System.out.println("hello "+INSTANCE.field+"+code:"+INSTANCE.hashCode());
    }

    public static void main(String[]args){
        EnumSingleton.INSTANCE.sayHello();
    }
}
