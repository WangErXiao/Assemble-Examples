package jetty.utils.thread;


import jetty.utils.Loader;
import jetty.utils.thread.strategy.ExecuteProduceRun;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;

/**
 * Created by yaozb on 15-4-19.
 */
public interface ExecutionStrategy {
    public void dispatch();
    public void execute();
    public interface Producer{
        Runnable produce();
    }
    public static class Factory
    {

        public static ExecutionStrategy instanceFor(Producer producer, Executor executor)
        {
            // TODO remove this mechanism before release
            String strategy = System.getProperty(producer.getClass().getName()+".ExecutionStrategy");
            if (strategy!=null)
            {
                try
                {
                    Class<? extends ExecutionStrategy> c = Loader.loadClass(producer.getClass(), strategy);
                    Constructor<? extends ExecutionStrategy> m = c.getConstructor(Producer.class,Executor.class);
                    return  m.newInstance(producer,executor);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            return new ExecuteProduceRun(producer,executor);
        }
    }


}
