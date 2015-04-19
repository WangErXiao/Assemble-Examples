package jetty.utils.thread.strategy;

import jetty.utils.SpinLock;
import jetty.utils.thread.ExecutionStrategy;

import java.util.concurrent.Executor;

/**
 * Created by yaozb on 15-4-19.
 */
public class ExecuteProduceRun implements ExecutionStrategy ,Runnable{
    private final SpinLock _lock = new SpinLock();
    private final Runnable _runExecute = new RunExecute();
    private final Producer _producer;
    private final Executor _executor;
    private boolean _idle=true;
    private boolean _execute;
    private boolean _producing;
    private boolean _pending;
    public ExecuteProduceRun(Producer producer, Executor executor)
    {
        this._producer = producer;
        this._executor = executor;
    }
    @Override
    public void execute() {
        boolean produce=false;
        try (SpinLock.Lock locked = _lock.lock())
        {
            // If we are idle and a thread is not producing
            if (_idle)
            {
                if (_producing)
                    throw new IllegalStateException();
                // Then this thread will do the producing
                produce=_producing=true;
                // and we are no longer idle
                _idle=false;
            }
            else
            {
                // Otherwise, lets tell the producing thread
                // that it should call produce again before going idle
                _execute=true;
            }
        }
        if (produce)
            produceAndRun();
    }

    @Override
    public void dispatch(){
        boolean dispatch=false;
        try (SpinLock.Lock locked = _lock.lock())
        {
            if (_idle)
                dispatch=true;
            else
                _execute=true;
        }
        if (dispatch)
            _executor.execute(_runExecute);
    }

    @Override
    public void run() {
        boolean produce=false;
        try (SpinLock.Lock locked = _lock.lock())
        {
            _pending=false;
            if (!_idle && !_producing)
            {
                produce=_producing=true;
            }
        }

        if (produce)
            produceAndRun();
    }

    private void produceAndRun() {
        while (true)
        {
            //_producer.produce()方法会被循环的调用
            Runnable task = _producer.produce();
            boolean dispatch=false;
            try (SpinLock.Lock locked = _lock.lock())
            {
                _producing=false;
                if (task == null)
                {
                    if (_execute)
                    {
                        _idle=false;
                        _producing=true;
                        _execute=false;
                        continue;
                    }
                    _idle=true;
                    break;
                }
                // We have a task, which we will run ourselves,
                // so if we don't have another thread pending
                if (!_pending)
                {
                    // dispatch one
                    dispatch=_pending=true;
                }
                _execute=false;
            }
            // If we became pending
            if (dispatch)
            {
                // Spawn a new thread to continue production by running the produce loop.
                _executor.execute(this);
            }
            // Run the task.
            task.run();
            // Once we have run the task, we can try producing again.
            //保证只有一个线程生产任务
            try (SpinLock.Lock locked = _lock.lock())
            {
                // Is another thread already producing or we are now idle?
                if (_producing || _idle)
                    break;
                _producing=true;
            }
        }
    }
    private class RunExecute implements Runnable
    {
        @Override
        public void run()
        {
            execute();
        }
    }
}
