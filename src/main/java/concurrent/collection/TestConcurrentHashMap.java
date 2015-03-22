package concurrent.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 15-3-22.
 */
public class TestConcurrentHashMap {
    public static void main(String[]args){

        Map<String ,String>map=new ConcurrentHashMap<String, String>();
        //Map<String ,String>map=new HashMap<String, String>();
        map.put("1","1");
        map.put("2","1");
        map.put("3","1");
        map.put("4","1");
        map.put("5","1");
        map.put("6","1");
        map.put("7","1");
        map.put("8","1");
        for (Map.Entry entry:map.entrySet()){
            System.out.println(entry.getKey());
            map.remove(Integer.parseInt((String)entry.getKey())+1+"");
        }
        for (Map.Entry entry:map.entrySet()){
            System.out.println(entry.getKey());
        }
    }



}
