package jvm;

/**
 * Created by yao on 16/1/14.
 *
 *
 * VM Args: -Xss1m
 *
 */
public class JavaStackOver {

    private int count;

    public  void incr(){
        count++;
        incr();
    }

    public static void main(String[]args){
        JavaStackOver javaStackOver=new JavaStackOver();
        try {

            javaStackOver.incr();
        }catch (Throwable e){
            System.out.println("stack length:"+javaStackOver.count);
            throw e;
        }
    }
}
