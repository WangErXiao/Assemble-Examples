package jvm;

import java.util.Vector;

/**
 * Created by yao on 15/12/20.
 *
 *  -Xmx20m -Xms5m -XX:+HeapDumpOnOutOfMemoryError
 */
public class DumpOOM {
    public static void main(String[]args){
        Vector v=new Vector();
        for (int i=0;i<25;i++){
            v.add(new byte[1*1024*1024]);
        }
    }
}
