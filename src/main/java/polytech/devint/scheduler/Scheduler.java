package polytech.devint.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Used to create tasks (repeatable or pontual)
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class Scheduler {

  private final ScheduledExecutorService executor;

  private static final Scheduler scheduler;
  private static final int THREAD_POOL_SIZE = 4;

  static {
    scheduler = new Scheduler();
  }

  /**
   * Instantiates a new Scheduler (THREAD_POOL_SIZE=4 threads in the pool)
   */
  public Scheduler() {
    executor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
  }

  /**
   * Executes a task
   *
   * @param runnable the runnable for the task
   */
  public void execute(Runnable runnable) {
    executor.execute(runnable);
  }

  /**
   * Executes a repeatable task
   *
   * @param task  The task
   * @param delay The delay between two loops
   * @param unit  The unit of the delay
   */
  public void execute(RepeatableTask task, int delay, TimeUnit unit) {
    this.execute(task, 0, delay, unit);
  }

  /**
   * Executes a repeatable task
   *
   * @param task         The task
   * @param delay        The delay between two loops
   * @param initialDelay The initial delay
   * @param unit         The unit of the delay
   */
  public void execute(RepeatableTask task, int initialDelay, int delay, TimeUnit unit) {
    MutableObject<ScheduledFuture<?>> mutableTask = new MutableObject<>();
    mutableTask.setValue(executor.scheduleWithFixedDelay(() -> {
      if (task.needToStop()) {
        mutableTask.getValue().cancel(true);
      } else {
        task.run();
      }
    }, initialDelay, delay, unit));
  }

  /**
   * @return The default scheduler
   */
  public static Scheduler getDefaultScheduler() {
    return scheduler;
  }
}
