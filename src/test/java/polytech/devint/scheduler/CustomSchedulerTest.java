package polytech.devint.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Testing different thread tasks
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class CustomSchedulerTest {

  /**
   * Testing if a normal task executes
   *
   * @throws InterruptedException
   */
  @Test
  public void testNormalTask() throws InterruptedException {
    CustomScheduler customScheduler = new CustomScheduler();
    MutableObject<Boolean> executed = new MutableObject<>(false);
    customScheduler.repeat(() -> executed.setValue(true));
    Thread.sleep(50);
    assertTrue(executed.getValue());
  }

  /**
   * Testing a repeatable task (should be executed 10 times)
   *
   * @throws InterruptedException
   */
  @Test
  public void testRepeatableTaskNoDelay() throws InterruptedException {
    CustomScheduler customScheduler = new CustomScheduler();
    MutableObject<Integer> executed = executeRepeatableTask(customScheduler, 0);
    // We give it plenty if time to execute a lot of loops
    Thread.sleep(150);
    assertEquals(10, (int) executed.getValue());
  }


  /**
   * Testing a repeatable task (should be executed 10 times) with a delay
   *
   * @throws InterruptedException
   */
  @Test
  public void testRepeatableTaskWithDelay() throws InterruptedException {
    CustomScheduler customScheduler = new CustomScheduler();
    MutableObject<Integer> executed = executeRepeatableTask(customScheduler, 50);
    // Should have looped 0 time after 20ms
    Thread.sleep(20);
    assertEquals(0, (int) executed.getValue());
    // We give it plenty if time to execute a lot of loops
    Thread.sleep(300);
    assertEquals(10, (int) executed.getValue());
  }

  /**
   * Executes a repeatable task with a delay
   *
   * @param customScheduler The customScheduler to use
   * @param delay The delay
   * @return The thread loop counter
   */
  private MutableObject<Integer> executeRepeatableTask(CustomScheduler customScheduler, int delay) {
    MutableObject<Integer> executed = new MutableObject<>(0);
    customScheduler.repeat(new RepeatableTask() {

      @Override
      public void execute() {
        executed.setValue(executed.getValue() + 1);
        if (executed.getValue() == 10) {
          this.stop();
          return;
        }
      }
    }, delay, 5);
    return executed;
  }

}