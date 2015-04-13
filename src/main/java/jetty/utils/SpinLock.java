package jetty.utils;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * Spin Lock
 * This is a lock designed to protect VERY short sections of
 * critical code.  Threads attempting to take the lock will spin
 * forever until the lock is available, thus it is important that
 * the code protected by this lock is extremely simple and non
 * blocking. The reason for this lock is that it prevents a thread
 * from giving up a CPU core when contending for the lock.</p>
 *
 * try(SpinLock.Lock lock = spinlock.lock())
 * {
 *   // something very quick and non blocking
 * }
 *
 * 该自旋锁设计目的主要是为了保护非常短小的关键代码段。线程在获取到该自旋锁之前会一直自旋，
 * 因此被该自旋锁保护的代码，必须非常简单，没有阻塞。因为在各个线程竞争获取该锁的时候，
 * 会一直自旋，不会放弃cpu
 * 该锁主要通过原子类的CAS，来获取锁
 */

public class SpinLock {
    private final AtomicReference<Thread> _lock = new AtomicReference<>(null);
    private final Lock _unlock = new Lock();
    public Lock lock()
    {
        Thread thread = Thread.currentThread();
        while(true)
        {
            //通过CAS操作来获取锁
            if (!_lock.compareAndSet(null,thread))
            {
                if (_lock.get()==thread)
                    throw new IllegalStateException("SpinLock is not reentrant");
                continue;
            }
            return _unlock;
        }
    }
    public boolean isLocked()
    {
        return _lock.get()!=null;
    }
    public boolean isLockedThread()
    {
        return _lock.get()==Thread.currentThread();
    }
    public class Lock implements AutoCloseable
    {
        @Override
        public void close()
        {
            _lock.set(null);
        }
    }
}
