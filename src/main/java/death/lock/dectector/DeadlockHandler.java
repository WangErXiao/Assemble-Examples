package death.lock.dectector;

import java.lang.management.ThreadInfo;

/**
 * Created by root on 15-3-20.
 */
public interface DeadlockHandler {
    void handleDeadlock(final ThreadInfo[] deadlockedThreads);
}
