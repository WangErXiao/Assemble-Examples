package collections;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 15-3-27.
 */
public class HashMapTest {
    public static void main(String[]args){
        Map<String,String> map=new HashMap<String, String>();
        map.put("xx","xxx");
        map.put("xx1","xxx1");
        map.put("xx2","xxx2");
        map.put("xx3","xxx3");
        map.put("xx4","xxx4");
        for (int i =0;i<5;i++){
           for (Map.Entry entry:map.entrySet()){
               System.out.println("entry key:"+entry.getKey()+" value:"+entry.getValue());
           }
            System.out.println("-----------------------------");
        }
        for (int i =0;i<5;i++){
            for (String str:map.keySet()){
                System.out.println("entry key:"+str+" value:"+map.get(str));
            }
            System.out.println("-----------------------------");
        }

    }
}
