package guava.String;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by root on 15-3-3.
 */
public class Test_Joiner_Spliter {
    public static void main(String[]args){
        String[]xx={"what","fuck","do","u","say"};
        String line= Joiner.on(" ").join(xx);
        System.out.println(line);
        String testString = "foo , what,,,more,";
        System.out.println(Splitter.on(",").trimResults().omitEmptyStrings().split(testString));

        String str = "key1: 1; key2: 2  ; key3: 3";
        Map<String, String> m = Splitter.on(';')
                .trimResults()
                .withKeyValueSeparator(":")
                .split(str);
        System.out.println(m.toString());

        Map<String, String> map = Maps.newHashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);
        map.put("key4", "value3");
        System.out.println(Joiner.on(';')
                .useForNull("NULL")
                .withKeyValueSeparator("=")
                .join(map));
    }
}
