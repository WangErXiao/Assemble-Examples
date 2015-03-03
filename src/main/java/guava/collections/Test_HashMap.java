package guava.collections;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 15-3-3.
 */
public class Test_HashMap {
    public static void main(final String[]args){
        Map<String,Map<Long,List<String>>> myMap= Maps.newHashMap();
        myMap.put("xx1",new HashMap<Long, List<String>>(){
            {
                put(1l,new ArrayList<String>(){{
                     add("yao");
                     add("zhi");
                     add("bin");
                }});
            }
        });
        System.out.println(myMap.get("xx1").get(1l).get(0));
    }
}
