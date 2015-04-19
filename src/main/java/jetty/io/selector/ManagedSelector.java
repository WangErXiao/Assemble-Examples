package jetty.io.selector;


import jetty.io.Connection;
import jetty.io.EndPoint;
import jetty.utils.ConcurrentArrayQueue;
import jetty.utils.SpinLock;
import jetty.utils.annotation.ManagedAttribute;
import jetty.utils.component.AbstractLifeCycle;
import jetty.utils.thread.ExecutionStrategy;
import jetty.utils.thread.Scheduler;

import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yaozb on 15-4-19.
 *
 * ManagedSelector封装了JDK NIO中的Selector。
 * ManagedSelector中有一个Selector和任务Queue,该任务队列放置的都是Runnable类型的任务。
 *
 *
 * 里面包含了一个执行策略类（ExecutionStrategy），jetty对它的注释是：ExecutionStrategy
 * 执行由Producer生产出的runnable任务。任务执行的策略根据实现的不同，或许在调用线程中直接执行，
 * 或者另起一个新的线程来执行。
 * ManagedSelector内部实现了一个生产者SelectorProducer，该生产者循环的执行以下代码
 while (true)
 {

    //处理select出来的keys
    Runnable task = processSelected();
    if (task != null)
    return task;

    Runnable action = runActions();
    if (action != null)
    return action;

    update();

    if (!select())
    return null;
 }
 *
 *
 *
 *
 *
 *
 *
 *

 */
public class ManagedSelector extends AbstractLifeCycle implements Runnable  {

    private final SpinLock _lock = new SpinLock();
    private boolean _selecting = false;
    //这里命名很有意思，叫action,主要是为了区分producer生产的读写task区分的。
    //这里的action全部是通过ManagedSelector.submit方法提交上的来的action,
    //一般该方法由SelectorManager来调用，主要有几种类型的action:
    //Acceptor,用来注册监听类型的Key OP_ACCEPT
    //Accept，主要是把SocketChannel注册到selector上，用来读写操作；
    //Connect,用来注册Key OP_CONNECT,用来判断连接是否就绪；

    private final Queue<Runnable> _actions = new ConcurrentArrayQueue<>();
    private final SelectorManager _selectorManager;
    private final int _id;
    private final ExecutionStrategy _strategy;
    private Selector _selector;

    public ManagedSelector(SelectorManager selectorManager, int id)
    {
        _selectorManager = selectorManager;
        _id = id;
        _strategy = ExecutionStrategy.Factory.instanceFor(new SelectorProducer(), selectorManager.getExecutor());
        setStopTimeout(5000);
    }
    @ManagedAttribute(value="The stop timeout in milliseconds")
    public long getStopTimeout()
    {
        return _stopTimeout;
    }

    public void setStopTimeout(long stopTimeout)
    {
        this._stopTimeout = stopTimeout;
    }


    private void closeNoExceptions(Closeable closeable)
    {
        try
        {
            if (closeable != null)
                closeable.close();
        }
        catch (Throwable x)
        {
            x.printStackTrace();
        }
    }
    private Runnable processConnect(SelectionKey key, final Connect connect)
    {
        SocketChannel channel = (SocketChannel)key.channel();
        try
        {
            key.attach(connect.attachment);
            boolean connected = _selectorManager.finishConnect(channel);
            if (connected)
            {
                if (connect.timeout.cancel())
                {
                    key.interestOps(0);
                    return new CreateEndPoint(channel, key)
                    {
                        @Override
                        protected void failed(Throwable failure)
                        {
                            super.failed(failure);
                            connect.failed(failure);
                        }
                    };
                }
                else
                {
                    throw new SocketTimeoutException("Concurrent Connect Timeout");
                }
            }
            else
            {
                throw new ConnectException();
            }
        }
        catch (Throwable x)
        {
            connect.failed(x);
            return null;
        }
    }

    private EndPoint createEndPoint(SocketChannel channel, SelectionKey selectionKey) throws IOException
    {
        EndPoint endPoint = _selectorManager.newEndPoint(channel, this, selectionKey);
        _selectorManager.endPointOpened(endPoint);
        Connection connection = _selectorManager.newConnection(channel, endPoint, selectionKey.attachment());
        endPoint.setConnection(connection);
        selectionKey.attach(endPoint);
        _selectorManager.connectionOpened(connection);
        return endPoint;
    }
    private void processAccept(SelectionKey key)
    {
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        SocketChannel channel = null;
        try
        {
            while ((channel = server.accept()) != null)
            {
                _selectorManager.accepted(channel);
            }
        }
        catch (Throwable x)
        {
            closeNoExceptions(channel);
        }
    }
    /**
     * 这个方法由SelectorManager中的executor放到线程池里执行，
     * 在SelectorManager初始化ManagedSelector后就提交到executor执行。
     */
    @Override
    public void run() {
        _strategy.execute();
    }

    public void submit(Runnable change)
    {
        try (SpinLock.Lock lock = _lock.lock())
        {
            _actions.offer(change);
            if (_selecting)
            {
                Selector selector = _selector;
                if (selector != null)
                    selector.wakeup();
                // To avoid the extra select wakeup.
                _selecting = false;
            }
        }
    }

    @Override
    protected void doStart() throws Exception
    {
        super.doStart();
        _selector = newSelector();
    }

    protected Selector newSelector() throws IOException
    {
        return Selector.open();
    }
    @Override
    protected void doStop() throws Exception
    {
        CloseEndPoints close_endps = new CloseEndPoints();
        submit(close_endps);
        close_endps.await(getStopTimeout());
        super.doStop();
        CloseSelector close_selector = new CloseSelector();
        submit(close_selector);
        close_selector.await(getStopTimeout());
    }
    public int size()
    {
        Selector s = _selector;
        if (s == null)
            return 0;
        return s.keys().size();
    }
    public void destroyEndPoint(final EndPoint endPoint)
    {
        final Connection connection = endPoint.getConnection();
        submit(new Product()
        {
            @Override
            public void run()
            {
                if (connection != null)
                    _selectorManager.connectionClosed(connection);
                _selectorManager.endPointClosed(endPoint);
            }
        });
    }





    /*----------------------------------------------------*/

    public interface SelectableEndPoint extends EndPoint
    {
        /**
         * Callback method invoked when a read or write events has been
         * detected by the {@link ManagedSelector} for this endpoint.
         *
         * @return a job that may block or null
         */
        Runnable onSelected();
        /**
         * Callback method invoked when all the keys selected by the
         * {@link ManagedSelector} for this endpoint have been processed.
         */
        void updateKey();
    }
    private interface Product extends Runnable
    {
    }

    private class SelectorProducer implements ExecutionStrategy.Producer
    {
        private Set<SelectionKey> _keys = Collections.emptySet();
        private Iterator<SelectionKey> _cursor = Collections.emptyIterator();

        //重点,会被循环的调用，在ManagedSeletor的run()方法中会被循环的调用
        @Override
        public Runnable produce()
        {
            while (true)
            {
                //处理选中事件，如果有任务生成直接返回。
                Runnable task = processSelected();
                if (task != null)
                    return task;
                //
                Runnable action = runActions();
                if (action != null)
                    return action;

                update();
                //重新select,获取感兴趣的事件。
                if (!select())
                    return null;
            }
        }
        private Runnable processSelected()
        {
            while (_cursor.hasNext())
            {
                SelectionKey key = _cursor.next();
                if (key.isValid())
                {
                    Object attachment = key.attachment();
                    try
                    {
                        if (attachment instanceof SelectableEndPoint)
                        {
                            // Try to produce a task
                            //会产生读或写任务，或者读写任务。

                            SelectableEndPoint selectable = (SelectableEndPoint)attachment;
                            Runnable task = selectable.onSelected();
                            if (task != null)
                                return task;
                        }
                        else if (key.isConnectable())
                        {
                            Runnable task = processConnect(key, (Connect)attachment);
                            if (task != null)
                                return task;
                        }
                        else if (key.isAcceptable())
                        {
                            processAccept(key);
                        }
                        else
                        {
                            throw new IllegalStateException("key=" + key + ", att=" + attachment + ", iOps=" + key.interestOps() + ", rOps=" + key.readyOps());
                        }
                    }
                    catch (CancelledKeyException x)
                    {
                        if (attachment instanceof org.eclipse.jetty.io.EndPoint)
                            closeNoExceptions((EndPoint)attachment);
                    }
                    catch (Throwable x)
                    {
                        if (attachment instanceof org.eclipse.jetty.io.EndPoint)
                            closeNoExceptions((EndPoint)attachment);
                    }
                }
                else
                {
                    Object attachment = key.attachment();
                    if (attachment instanceof EndPoint)
                        closeNoExceptions((EndPoint)attachment);
                }
            }
            return null;
        }

        /**
         *
         * @return
         */
        private Runnable runActions()
        {
            //循环处理任务队列中的任务，直到任务完全被处理完。
            while (true)
            {
                Runnable action;
                try (SpinLock.Lock lock = _lock.lock())
                {
                    action = _actions.poll();
                    if (action == null)
                    {
                        // No more actions, so we need to select
                        _selecting = true;
                        return null;
                    }
                }
                //***重点*** 如果是生产者生成的任务，直接返回，交给 ExecutionStrategy来执行
                if (action instanceof Product)
                    return action;
                // Running the change may queue another action.
                // 执行任务，或许会产生新的任务入队。
                runChange(action);
            }
        }

        private void runChange(Runnable change)
        {
            try
            {
                change.run();
            }
            catch (Throwable x)
            {
                x.printStackTrace();
            }
        }
        private void update()
        {
            for (SelectionKey key : _keys)
                updateKey(key);
            _keys.clear();
        }
        private void updateKey(SelectionKey key)
        {
            Object attachment = key.attachment();
            if (attachment instanceof SelectableEndPoint)
                ((SelectableEndPoint)attachment).updateKey();
        }
        //重新进行选择。。。。。。
        private boolean select()
        {
            try
            {
                Selector selector = _selector;
                if (selector != null && selector.isOpen())
                {
                    int selected = selector.select();
                    try (SpinLock.Lock lock = _lock.lock())
                    {
                        // finished selecting
                        _selecting = false;
                    }
                    _keys = selector.selectedKeys();
                    _cursor = _keys.iterator();

                    return true;
                }
            }
            catch (Throwable x)
            {
                closeNoExceptions(_selector);
            }
            return false;
        }


    }

    class Connect implements Runnable
    {
        private final AtomicBoolean failed = new AtomicBoolean();
        private final SocketChannel channel;
        private final Object attachment;
        private final Scheduler.Task timeout;

        Connect(SocketChannel channel, Object attachment)
        {
            this.channel = channel;
            this.attachment = attachment;
            this.timeout = ManagedSelector.this._selectorManager.getScheduler().schedule(new ConnectTimeout(this), ManagedSelector.this._selectorManager.getConnectTimeout(), TimeUnit.MILLISECONDS);
        }

        @Override
        public void run()
        {
            try
            {
                channel.register(_selector, SelectionKey.OP_CONNECT, this);
            }
            catch (Throwable x)
            {
                failed(x);
            }
        }

        private void failed(Throwable failure)
        {
            if (failed.compareAndSet(false, true))
            {
                timeout.cancel();
                closeNoExceptions(channel);
                ManagedSelector.this._selectorManager.connectionFailed(channel, failure, attachment);
            }
        }
    }
    private class CreateEndPoint implements Product
    {
        private final SocketChannel channel;
        private final SelectionKey key;

        public CreateEndPoint(SocketChannel channel, SelectionKey key)
        {
            this.channel = channel;
            this.key = key;
        }

        @Override
        public void run()
        {
            try
            {
                createEndPoint(channel, key);
            }
            catch (Throwable x)
            {
                failed(x);
            }
        }

        protected void failed(Throwable failure)
        {
            closeNoExceptions(channel);
        }
    }

    private class ConnectTimeout implements Runnable
    {
        private final Connect connect;
        private ConnectTimeout(Connect connect)
        {
            this.connect = connect;
        }

        @Override
        public void run()
        {
            SocketChannel channel = connect.channel;
            if (channel.isConnectionPending())
            {
                connect.failed(new SocketTimeoutException("Connect Timeout"));
            }
        }
    }

    class Acceptor implements Runnable
    {
        private final ServerSocketChannel _channel;

        public Acceptor(ServerSocketChannel channel)
        {
            this._channel = channel;
        }

        @Override
        public void run()
        {
            try
            {
                SelectionKey key = _channel.register(_selector, SelectionKey.OP_ACCEPT, null);
            }
            catch (Throwable x)
            {
                closeNoExceptions(_channel);
            }
        }
    }

    class Accept implements Runnable
    {
        private final SocketChannel channel;
        private final Object attachment;

        Accept(SocketChannel channel, Object attachment)
        {
            this.channel = channel;
            this.attachment = attachment;
        }

        @Override
        public void run()
        {
            try
            {
                final SelectionKey key = channel.register(_selector, 0, attachment);
                submit(new CreateEndPoint(channel, key));
            }
            catch (Throwable x)
            {
                closeNoExceptions(channel);
            }
        }
    }
    private class DumpKeys implements Runnable {
        private final CountDownLatch latch = new CountDownLatch(1);
        private final List<Object> _dumps;

        private DumpKeys(List<Object> dumps) {
            this._dumps = dumps;
        }

        @Override
        public void run() {
            Selector selector = _selector;
            if (selector != null && selector.isOpen()) {
                Set<SelectionKey> keys = selector.keys();
                _dumps.add(selector + " keys=" + keys.size());
                for (SelectionKey key : keys) {
                    try {
                        _dumps.add(String.format("SelectionKey@%x{i=%d}->%s", key.hashCode(), key.interestOps(), key.attachment()));
                    } catch (Throwable x) {
                    }
                }
            }
            latch.countDown();
        }
    }

    private class CloseEndPoints implements Runnable
    {
        private final CountDownLatch _latch = new CountDownLatch(1);
        private CountDownLatch _allClosed;

        @Override
        public void run()
        {
            List<EndPoint> end_points = new ArrayList<>();
            for (SelectionKey key : _selector.keys())
            {
                if (key.isValid())
                {
                    Object attachment = key.attachment();
                    if (attachment instanceof EndPoint)
                        end_points.add((EndPoint)attachment);
                }
            }
            int size = end_points.size();

            _allClosed = new CountDownLatch(size);
            _latch.countDown();
            for (EndPoint endp : end_points)
                submit(new EndPointCloser(endp, _allClosed));
        }

        public boolean await(long timeout)
        {
            try
            {
                return _latch.await(timeout, TimeUnit.MILLISECONDS) &&
                        _allClosed.await(timeout, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException x)
            {
                return false;
            }
        }
    }

    private class EndPointCloser implements Product
    {
        private final EndPoint _endPoint;
        private final CountDownLatch _latch;

        private EndPointCloser(EndPoint endPoint, CountDownLatch latch)
        {
            _endPoint = endPoint;
            _latch = latch;
        }

        @Override
        public void run()
        {
            closeNoExceptions(_endPoint.getConnection());
            _latch.countDown();
        }
    }

    private class CloseSelector implements Runnable
    {
        private CountDownLatch _latch = new CountDownLatch(1);

        @Override
        public void run()
        {
            Selector selector = _selector;
            _selector = null;
            closeNoExceptions(selector);
            _latch.countDown();
        }

        public boolean await(long timeout)
        {
            try
            {
                return _latch.await(timeout, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException x)
            {
                return false;
            }
        }
    }
}
