package io.nio;

import java.nio.FloatBuffer;

/**
 * Created by yao on 15/12/14.
 */
public class BufferTest {

    public static void main(String[]args){
        FloatBuffer buffer=FloatBuffer.allocate(10);
        for (int i=0;i<buffer.capacity();i++){
            float f=(float) Math.sin((((float) i) / 10) * (2 * Math.PI));
            buffer.put(f);
        }
        buffer.flip();
        while (buffer.hasRemaining()){
            float f=buffer.get();
            System.out.println(f);
        }
    }
}
