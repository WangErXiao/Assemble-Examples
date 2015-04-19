package jetty.io.selector;

import jetty.io.Connection;
import jetty.io.EndPoint;
import jetty.utils.component.AbstractLifeCycle;
import jetty.utils.component.Dumpable;
import jetty.utils.thread.Scheduler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

/**
 * Created by yaozb on 15-4-19.
 *
 * 管理ManagedSelector。
 * 负责提交各种channel任务。比如负责监听的Acceptor, 读写操作accept, 连接操作connect
 * Manager会根据固定的规则从自己管理的ManagedSelectors中选择一个，来提交任务。
 */
public abstract class SelectorManager extends AbstractLifeCycle implements Dumpable {
    public static final  int DEFAULT_CONNECT_TIMEOUT=15000;

    private final Executor executor;
    private final Scheduler scheduler;
    private final ManagedSelector[]_selectors;
    private long _connectTimeout=DEFAULT_CONNECT_TIMEOUT;
    private long _selectorIndex;

    protected SelectorManager(Executor executor, Scheduler scheduler)
    {
        this(executor, scheduler, (Runtime.getRuntime().availableProcessors() + 1) / 2);
    }
    protected SelectorManager(Executor executor, Scheduler scheduler,int selectors) {
        if(selectors<=0)
            throw  new IllegalArgumentException("no selectors");
        this.executor = executor;
        this.scheduler = scheduler;
        _selectors=new ManagedSelector[selectors];
    }
    public Executor getExecutor()
    {
        return executor;
    }
    public Scheduler getScheduler()
    {
        return scheduler;
    }
    public long getConnectTimeout()
    {
        return _connectTimeout;
    }
    public void setConnectTimeout(long milliseconds)
    {
        _connectTimeout = milliseconds;
    }
    /**
     * execute task
     * @param task
     */
    protected void execute(Runnable task){
        executor.execute(task);
    }
    public int getSelectorCount(){
        return _selectors.length;
    }
    private ManagedSelector chooseSelector(SocketChannel channel)
    {
        ManagedSelector candidate1 = null;
        if (channel != null)
        {
            try
            {
                SocketAddress remote = channel.getRemoteAddress();
                if (remote instanceof InetSocketAddress)
                {
                    byte[] addr = ((InetSocketAddress)remote).getAddress().getAddress();
                    if (addr != null)
                    {
                        int s = addr[addr.length - 1] & 0xFF;
                        candidate1 = _selectors[s % getSelectorCount()];
                    }
                }
            }
            catch (IOException x)
            {
                x.printStackTrace();
            }
        }
        long s = _selectorIndex++;
        int index = (int)(s % getSelectorCount());
        ManagedSelector candidate2 = _selectors[index];
        if (candidate1 == null || candidate1.size() >= candidate2.size() * 2)
            return candidate2;
        return candidate1;
    }
    //以下四个方法很重要 分别是负责 提交连接\读写\acceptor操作的，全部通过ManagedSelector submit方法来提交任务；
    public void connect(SocketChannel channel, Object attachment)
    {
        ManagedSelector set = chooseSelector(channel);
        set.submit(set.new Connect(channel, attachment));
    }
    public void accept(SocketChannel channel)
    {
        accept(channel, null);
    }
    public void accept(SocketChannel channel, Object attachment)
    {
        final ManagedSelector selector = chooseSelector(channel);
        selector.submit(selector.new Accept(channel, attachment));
    }
    public void acceptor(ServerSocketChannel server)
    {
        final ManagedSelector selector = chooseSelector(null);
        selector.submit(selector.new Acceptor(server));
    }
    protected void accepted(SocketChannel channel) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doStart() throws Exception
    {
        super.doStart();
        for (int i = 0; i < _selectors.length; i++)
        {
            ManagedSelector selector = newSelector(i);
            _selectors[i] = selector;
            selector.start();
            execute(selector);
        }
    }
    protected ManagedSelector newSelector(int id)
    {
        return new ManagedSelector(this, id);
    }

    @Override
    protected void doStop() throws Exception
    {
        for (ManagedSelector selector : _selectors)
            selector.stop();
        super.doStop();
    }

    protected void endPointOpened(EndPoint endpoint)
    {
        endpoint.onOpen();
    }


    protected void endPointClosed(EndPoint endpoint)
    {
        endpoint.onClose();
    }


    public void connectionOpened(Connection connection)
    {
        try
        {
            connection.onOpen();
        }
        catch (Throwable x)
        {
            x.printStackTrace();
        }
    }
    public void connectionClosed(Connection connection)
    {
        try
        {
            connection.onClose();
        }
        catch (Throwable x)
        {
            x.printStackTrace();
        }
    }
    protected boolean finishConnect(SocketChannel channel) throws IOException
    {
        return channel.finishConnect();
    }
    protected void connectionFailed(SocketChannel channel, Throwable ex, Object attachment)
    {
    }
    protected abstract EndPoint newEndPoint(SocketChannel channel, ManagedSelector selector, SelectionKey selectionKey) throws IOException;
    public abstract Connection newConnection(SocketChannel channel, EndPoint endpoint, Object attachment) throws IOException;

}
