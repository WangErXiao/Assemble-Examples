package guava.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by root on 15-3-3.
 */
public class Test_ImmutableList {
    public static void main(String[]args){

        ImmutableList<String> of=ImmutableList.of("hello","world","ni","hao");

        for(String x:of){
            System.out.println(x);
        }
        ImmutableMap<String,String> map=ImmutableMap.of("key1", "value1",
                "key2", "value2",
                "key3", "value3",
                "key4", "value4",
                "key5", "value5"
        );
        for (Map.Entry<String,String> entry:map.entrySet()){
            System.out.println("key:" + entry.getKey()+"  value:"+entry.getValue());
        }
    }
}
