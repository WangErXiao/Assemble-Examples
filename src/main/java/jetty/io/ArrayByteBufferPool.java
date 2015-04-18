package jetty.io;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by yaozb on 15-4-18.
 */
public class ArrayByteBufferPool implements ByteBufferPool {
    private final int _min;
    private final Bucket[]_direct;
    private final Bucket[]_indirect;
    private final int _inc;
    public ArrayByteBufferPool()
    {
        this(0,1024,64*1024);
    }

    public ArrayByteBufferPool(int minSize, int increment, int maxSize) {
        //最小size不能大于增量
        if (minSize>=increment)
            throw new IllegalArgumentException("minSize >= increment");
        //最大size 必须是增量的整数倍，并且增量不能大于最大的size
        if ((maxSize%increment)!=0 || increment>=maxSize)
            throw new IllegalArgumentException("increment must be a divisor of maxSize");
        _min=minSize;
        _inc=increment;
        //初始化 maxSize/increment个桶,包含直接内存的与heap的
        _direct=new Bucket[maxSize/increment];
        _indirect=new Bucket[maxSize/increment];

        int size=0;
        for (int i=0;i<_direct.length;i++)
        {
            size+=_inc;
            _direct[i]=new Bucket(size);
            _indirect[i]=new Bucket(size);
        }
    }
    @Override
    public ByteBuffer acquire(int size, boolean direct) {
        //根据size找到对应size的桶
        Bucket bucket = bucketFor(size,direct);
        //如果queue中存在ByteBuffer,则直接返回
        ByteBuffer buffer = bucket==null?null:bucket._queue.poll();
        //初次使用 桶中的queue没有 ByteBuffer
        if (buffer == null){
            //（bucket==null）如果是由于size 超过pool的最大size,造成没有相应的桶
            int capacity = bucket==null?size:bucket._size;
            //分配相应size的ByteBuffer;
            buffer = direct ? BufferUtil.allocateDirect(capacity) : BufferUtil.allocate(capacity);
        }
        return buffer;
    }
    @Override
    public void release(ByteBuffer buffer) {
        if (buffer!=null){
            Bucket bucket = bucketFor(buffer.capacity(),buffer.isDirect());
            if (bucket!=null){
                BufferUtil.clear(buffer);
                //关键：这步是把相应size的ByteBuffer放到相应的size的桶中，byteBuffer入队,用来重用
                bucket._queue.offer(buffer);
            }
        }
    }
    //清空byteBuffer  pool
    public void clear(){
        for (int i=0;i<_direct.length;i++){
            _direct[i]._queue.clear();
            _indirect[i]._queue.clear();
        }
    }
    //根据size寻找 桶
    private Bucket bucketFor(int size,boolean direct) {
        if (size<=_min)
            return null;
        int b=(size-1)/_inc;
        if (b>=_direct.length)
            return null;
        Bucket bucket = direct?_direct[b]:_indirect[b];
        return bucket;
    }
    public static class Bucket{
        public final int _size;
        public final Queue<ByteBuffer> _queue= new ConcurrentLinkedQueue<>();
        Bucket(int size) {
            _size=size;
        }
        @Override
        public String toString()
        {
            return String.format("Bucket@%x{%d,%d}",hashCode(),_size,_queue.size());
        }
    }
    // 测试用的，可以忽略
    Bucket[] bucketsFor(boolean direct)
    {
        return direct ? _direct : _indirect;
    }
}
