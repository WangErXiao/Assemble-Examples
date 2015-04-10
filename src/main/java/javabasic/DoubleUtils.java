package javabasic;


import org.apache.commons.lang3.StringUtils;


/**
 * Created by yaozb on 15-4-9.
 */
public class DoubleUtils {
    public static double parseDouble(String str){
        if(StringUtils.isBlank(str)){
            return 0;
        }
        str=str.trim();
        boolean isNe=false;
        if(str.startsWith("-")){
            isNe=true;
        }
        if(str.startsWith("-")||str.startsWith("+")){
            str=str.substring(1,str.length());
        }
        //format
        if(str.indexOf(".",str.indexOf(".")+1)>-1){
            throw  new IllegalArgumentException("参数错误");
        }
        char[]chars=str.toCharArray();
        for (char c:chars){
            if(!(c=='.'||(c>='0'&&c<='9'))){
                throw  new IllegalArgumentException("参数错误");
            }
        }
        String[]strs;
        double rel=0;
        if(str.contains(".")){
            strs=str.split("\\.");
        }else{
            strs=new String[]{str};
        }
        rel+=parseInt(strs[0]);
        if(strs.length==2){
            rel+=(parseInt(strs[1])/(Math.pow(10.0,strs[1].length())));
        }
        return isNe==false?rel:-rel;
    }
    private static int parseInt(String intStr){
        char[]chars=intStr.toCharArray();
        int length=chars.length;
        int rel=0;
        for(int i=(length-1);i>-1;i--){
                rel+=(chars[length-i-1]-'0')*(Math.pow(10,i));
        }
        return rel;
    }
    public static void main(String[]args){
       // System.out.println(parseDouble("12.3.43"));
        System.out.println(parseDouble("+222113.3"));
        System.out.println(parseDouble("22-2113.3"));
    }
}
