package jetty.io;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Jetty ByteBufferPool
 * Created by yaozb on 15-4-18.
 */
public interface ByteBufferPool {

    /**
     * 获取指定大小的ByteBuffer.
     * 返回的ByteBuffer capacity 或许大于size,
     * 但是limit一定等于size
     * @param size
     * @param direct  是否用direct内存
     * @return
     */
    public ByteBuffer acquire(int size,boolean direct);

    /**
     * 返还ByteBuffer,使之重用
     * @param buffer
     */
    public void release(ByteBuffer buffer);

    public static class Lease{
        private final ByteBufferPool byteBufferPool;
        private final List<ByteBuffer> buffers;
        private final List<Boolean>recycles;
        public Lease(ByteBufferPool byteBufferPool, List<ByteBuffer> buffers, List<Boolean> recycles) {
            this.byteBufferPool = byteBufferPool;
            this.buffers = buffers;
            this.recycles = recycles;
        }
        public ByteBuffer acquire(int capacity,boolean direct){
           ByteBuffer buffer=byteBufferPool.acquire(capacity,direct);
           BufferUtil.clearToFill(buffer);
           return  buffer;
        }
        //预先王ByteBuffer list中插入一个ByteBuffer
        public void prepend(ByteBuffer  buffer,boolean recycle){
            insert(0, buffer, recycle);
        }
        public void insert(int index, ByteBuffer buffer, boolean recycle) {
            buffers.add(index,buffer);
            recycles.add(index,recycle);
        }
        public void append(ByteBuffer buffer, boolean recycle) {
            buffers.add(buffer);
            recycles.add(recycle);
        }

        public List<ByteBuffer> getByteBuffers() {
            return buffers;
        }
        //buffer list 所有剩余长度
        public long getTotalLength() {
            long length = 0;
            for (int i = 0; i < buffers.size(); ++i)
                length += buffers.get(i).remaining();
            return length;
        }
        public int getSize() {
            return buffers.size();
        }
        //释放所有可以循环利用的ByteBuffer
        public void recycle() {
            for (int i = 0; i < buffers.size(); ++i)
            {
                ByteBuffer buffer = buffers.get(i);
                if (recycles.get(i))
                    byteBufferPool.release(buffer);
            }
            buffers.clear();
            recycles.clear();
        }
    }
}
