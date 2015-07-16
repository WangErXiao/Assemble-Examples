package base;

/**
 * Created by yao on 15/7/16.
 */
public class Swap {

    public static void main(String []args){
        int a=11;
        int b=22;
        a=a^b;
        b=a^b;
        a=a^b;
        System.out.println("a="+a+" b="+b);


        String str1="a";
        String str2="c";
        swap(str1,str2);
        System.out.println("str1="+str1+" str2="+str2);

    }
    //无效
    public static void swap(String a,String b){
        String c=a;
        a=b;
        b=c;
    }


}
