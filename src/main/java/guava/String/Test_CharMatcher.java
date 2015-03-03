package guava.String;

import com.google.common.base.CharMatcher;

/**
 * Created by root on 15-3-3.
 */
public class Test_CharMatcher {
    public static void main(String[]args){
        String result= CharMatcher.DIGIT.retainFrom("yao123321zhibin4567");
        System.out.println(result);
        String result1= CharMatcher.DIGIT.removeFrom("yao123321zhibin4567");
        System.out.println(result1);
    }
}
