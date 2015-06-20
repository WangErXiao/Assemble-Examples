package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yao on 15/6/4.
 */
public class ArrayExample {
    public static void main(String[]args){
        List<String> list=new ArrayList<String>(){{
            add("yao");
            add("wang");
            add("li");
        }};
        String[] array={"yao","wang","li"};
        Arrays.asList(list).stream().forEach(s -> System.out.println(s));
        System.out.println(Arrays.deepToString(list.toArray()));
    }
}
