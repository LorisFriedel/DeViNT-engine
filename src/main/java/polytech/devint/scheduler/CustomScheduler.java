package polytech.devint.scheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Used to create tasks (repeatable or pontual)
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 * @author Loris Friedel
 */
public class CustomScheduler extends SchedulerReady {

  private static final CustomScheduler DEFAULT_CUSTOM_SCHEDULER;
  private static final int DEFAULT_THREAD_POOL_SIZE = 2;
  private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

  static {
    DEFAULT_CUSTOM_SCHEDULER = new CustomScheduler();
  }

  /**
   * Instantiates a new CustomScheduler with the default thread pool size
   */
  public CustomScheduler() {
    super(DEFAULT_THREAD_POOL_SIZE);
  }

  /**
   * Executes a task
   *
   * @param runnable the runnable for the task
   */
  public void repeat(Runnable runnable) {
    getExecutor().execute(runnable);
  }

  /**
   * Executes a repeatable task
   *
   * @param task    the task to repeat
   * @param delayMs the delay between two task execution in milliseconds
   */
  public void repeat(RepeatableTask task, long delayMs) {
    this.repeat(task, 0, delayMs);
  }

  /**
   * Executes a repeatable task with an initial delay before the first execution.
   *
   * @param task           the task to repeat
   * @param delayMs        the delay between two task execution in milliseconds
   * @param initialDelayMs the initial time to wait before the first execution in milliseconds
   */
  public void repeat(RepeatableTask task, long initialDelayMs, long delayMs) {
    MutableObject<ScheduledFuture<?>> mutableTask = new MutableObject<>();
    mutableTask.setValue(getExecutor().scheduleWithFixedDelay(() -> {
      if (task.needToStop()) {
        mutableTask.getValue().cancel(true);
      } else {
        task.run();
      }
    }, initialDelayMs, delayMs, DEFAULT_TIME_UNIT));
  }

  /**
   * @return the default scheduler
   */
  public static CustomScheduler getDefaultCustomScheduler() {
    return DEFAULT_CUSTOM_SCHEDULER;
  }
}
