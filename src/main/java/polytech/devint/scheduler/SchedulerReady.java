package polytech.devint.scheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Makes children ready to use a scheduler
 *
 * @author Loris Friedel
 */
public abstract class SchedulerReady {

  private final ScheduledThreadPoolExecutor executor;

  private static final ScheduledThreadPoolExecutor DEFAULT_EXECUTOR;
  private static final int DEFAULT_THREAD_POOL_SIZE = 1;

  static {
    DEFAULT_EXECUTOR = new ScheduledThreadPoolExecutor(1);
  }

  public SchedulerReady(int corePoolSize) {
    executor = new ScheduledThreadPoolExecutor(corePoolSize);
  }

  public SchedulerReady() {
    this(DEFAULT_THREAD_POOL_SIZE);
  }

  public ScheduledThreadPoolExecutor getExecutor() {
    return executor;
  }

  public static ScheduledThreadPoolExecutor getDefaultExecutor() {
    return DEFAULT_EXECUTOR;
  }

  public static ScheduledFuture<?> schedule(Runnable command, long delayMs) {
    return getDefaultExecutor().schedule(command, delayMs, TimeUnit.MILLISECONDS);
  }
}
