package jetty.io;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by yaozb on 15-4-18.
 */
public class ByteBufferPoolTest {
    public static void main(String[]args){
        ByteBufferPool.Lease lease =
                new ByteBufferPool.Lease(new ArrayByteBufferPool(),
                                        new ArrayList<>(),
                                        new ArrayList<>());

        ByteBuffer buffer1=lease.acquire(100, false);
        lease.append(buffer1, true);
        buffer1.put("robin yao".getBytes());
        BufferUtil.flipToFlush(buffer1, 0);
        System.out.println(new String(buffer1.array()));
        lease.recycle();
        ByteBuffer buffer2=lease.acquire(100, false);
        System.out.println(System.identityHashCode(buffer1));
        System.out.println(System.identityHashCode(buffer2));
        //是否是同一个
        System.out.println(buffer1==buffer2);
    }
    public Void getX1(){
        System.out.println("Void");
        return null;
    }
    public void getX2(){
        System.out.println("Void");
    }
}
