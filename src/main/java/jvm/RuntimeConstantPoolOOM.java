package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yao on 16/1/14.
 *
 * VM  :  -XX:PermSize=10M  -XX:MaxPermSize=10M
 *  要在1.8之前的版本
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[]args){

        List<String>list=new ArrayList<>();

        int i=0;

        while (true){

            list.add(String.valueOf(i++).intern());

        }

    }
}
