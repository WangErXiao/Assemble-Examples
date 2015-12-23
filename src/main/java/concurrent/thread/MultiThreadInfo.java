package concurrent.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Created by yao on 15/12/23.
 */
public class MultiThreadInfo {
    public static void main(String[]args){

        ThreadMXBean threadMXBean= ManagementFactory.getThreadMXBean();

        ThreadInfo[]threadInfos=threadMXBean.dumpAllThreads(false,false);

        for (ThreadInfo threadInfo:threadInfos){
            System.out.println("id:"+threadInfo.getThreadId()+" name:"+threadInfo.getThreadName());
        }


    }
}
