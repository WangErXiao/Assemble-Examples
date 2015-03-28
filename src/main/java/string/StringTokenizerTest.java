package string;

import java.util.StringTokenizer;

/**
 * Created by root on 15-3-28.
 */
public class StringTokenizerTest {
    public static void main(String[]args){
        String xx="yao.zhi.bin";
        StringTokenizer stringTokenizer=new StringTokenizer(xx,".");
        System.out.println(stringTokenizer.countTokens());
        String str;
        for (;stringTokenizer.hasMoreTokens();){
            str=stringTokenizer.nextToken();
            System.out.println(str);
        }
    }
}
