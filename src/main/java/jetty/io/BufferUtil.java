package jetty.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Created by yaozb on 15-4-16.
 */
public class BufferUtil {

    /**
     * 分配 flush模式的ByteBuffer，limit和position都为0,在写入数据时，必须先翻为fill模式
     * @param capacity
     * @return
     */
    public static ByteBuffer allocate(int capacity)
    {
        ByteBuffer buf = ByteBuffer.allocate(capacity);
        buf.limit(0);
        return buf;
    }
    public static ByteBuffer allocateDirect(int capacity)
    {
        ByteBuffer buf = ByteBuffer.allocateDirect(capacity);
        buf.limit(0);
        return buf;
    }

    /**
     * 清空buffer ,只需把positoin 和limit 同时置为0
     * @param buffer
     */
    public static void clear(ByteBuffer buffer)
    {
        if (buffer != null)
        {
            buffer.position(0);
            buffer.limit(0);
        }
    }

    /**
     * 清空buffer ,置为fill模式
     * @param buffer
     */
    public static void clearToFill(ByteBuffer buffer)
    {
        if (buffer != null)
        {
            buffer.position(0);
            buffer.limit(buffer.capacity());
        }
    }

    /**
     * 翻转为fill模式
     * @param buffer
     * @return
     */
    public static int flipToFill(ByteBuffer buffer)
    {
        int position = buffer.position();
        int limit = buffer.limit();
        //说明正好flush完，可以完全转换未fill模式
        if (position == limit)
        {
            buffer.position(0);
            buffer.limit(buffer.capacity());
            return 0;
        }
        //当前limit equal capacity,另申请空间
        int capacity = buffer.capacity();
        if (limit == capacity)
        {
            buffer.compact();
            return 0;
        }
        //一般情况，剩余的容量，可写的空间
        buffer.position(limit);
        buffer.limit(capacity);
        return position;
    }

    /**
     * 转为flush模式，即读模式，把当前写到的位置至为limit,动态传入读开始位置position,
     * 如果position未0 ,该方法的作用和 ByteBuffer.flip()的作用等价
     * @param buffer
     * @param position
     */
    public static void flipToFlush(ByteBuffer buffer, int position)
    {
        buffer.limit(buffer.position());
        buffer.position(position);
    }

    /**
     * 把buffer转换为数组。
     * @param buffer
     * @return
     */
    public static byte[] toArray(ByteBuffer buffer)
    {
        //主要针对heap buffer
        if (buffer.hasArray())
        {
            byte[] array = buffer.array();
            int from=buffer.arrayOffset() + buffer.position();
            return Arrays.copyOfRange(array, from, from + buffer.remaining());
        }
        //针对 direct buffer
        else
        {
            byte[] to = new byte[buffer.remaining()];
            buffer.slice().get(to);
            return to;
        }
    }

    /**
     * 是否为空 ，remaining() 主要是  limit - position
     * @param buf
     * @return
     */
    public static boolean isEmpty(ByteBuffer buf)
    {
        return buf == null || buf.remaining() == 0;
    }
    public static boolean hasContent(ByteBuffer buf)
    {
        return buf != null && buf.remaining() > 0;
    }
    public static boolean isFull(ByteBuffer buf)
    {
        return buf != null && buf.limit() == buf.capacity();
    }
    public static int length(ByteBuffer buffer)
    {
        return buffer == null ? 0 : buffer.remaining();
    }
    public static int space(ByteBuffer buffer)
    {
        if (buffer == null)
            return 0;
        return buffer.capacity() - buffer.limit();
    }
    public static boolean compact(ByteBuffer buffer)
    {
        if (buffer.position()==0)
            return false;
        boolean full = buffer.limit() == buffer.capacity();
        buffer.compact().flip();
        return full && buffer.limit() < buffer.capacity();
    }

    /**
     * 把from中未读的，写到 to 中
     * @param from Buffer  读模式  flush
     * @param to   Buffer  写模式  fill
     * @return number of bytes moved
     */
    public static int put(ByteBuffer from, ByteBuffer to)
    {
        int put;
        int remaining = from.remaining();
        if (remaining > 0)
        {   //如果空间足够，直接写入
            if (remaining <= to.remaining())
            {
                to.put(from);
                put = remaining;
                //把from 读完
                from.position(from.limit());
            }
            //heap buffer
            else if (from.hasArray())
            {
                put = to.remaining();
                //只读部分数据
                to.put(from.array(), from.arrayOffset() + from.position(), put);
                from.position(from.position() + put);
            }
            //direct buffer
            else
            {
                //只读部分数据
                put = to.remaining();
                ByteBuffer slice = from.slice();
                slice.limit(put);
                to.put(slice);
                from.position(from.position() + put);
            }
        }
        else
            put = 0;

        return put;
    }

    /** 添加 byte[] 到buffer中
     * @param to
     * @param b
     * @param off
     * @param len
     * @throws java.nio.BufferOverflowException
     */
    public static void append(ByteBuffer to, byte[] b, int off, int len) throws BufferOverflowException
    {   //置为写模式
        int pos = flipToFill(to);
        try
        {
            to.put(b, off, len);
        }
        finally
        {
            //置为读模式
            flipToFlush(to, pos);
        }
    }

    /**
     * 从文件中读数据到buffer中
      * @param file
     * @param buffer
     * @throws IOException
     */
    public static void readFrom(File file, ByteBuffer buffer) throws IOException
    {
        try(RandomAccessFile raf = new RandomAccessFile(file,"r"))
        {
            FileChannel channel = raf.getChannel();
            long needed=raf.length();

            while (needed>0 && buffer.hasRemaining())
                needed=needed-channel.read(buffer);
        }
    }

    public static void readFrom(InputStream is, int needed, ByteBuffer buffer) throws IOException
    {
        ByteBuffer tmp = allocate(8192);

        while (needed > 0 && buffer.hasRemaining())
        {
            int l = is.read(tmp.array(), 0, 8192);
            if (l < 0)
                break;
            tmp.position(0);
            tmp.limit(l);
            buffer.put(tmp);
        }
    }
}
