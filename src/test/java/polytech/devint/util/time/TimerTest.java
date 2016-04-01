package polytech.devint.util.time;

import org.junit.Before;
import org.junit.Test;
import polytech.devint.util.time.Timer;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Loris Friedel
 */
public class TimerTest {

  private static final int ERROR_MARGIN = 6; // depends on the configure, waiting for a better solution

  class TimerDefault extends Timer {
    static final long DEFAULT_DURATION = 1000L;

    public TimerDefault(long duration, long interval) {
      super(duration, interval);
    }

    public TimerDefault(long duration) {
      super(duration);
    }

    @Override
    protected void onTick() {

    }

    public TimerDefault() {
      super(DEFAULT_DURATION);
    }
  }

  Timer timer;

  @Before
  public void setUp() {
    timer = new TimerDefault();
  }

  @Test
  public void noElapsedTime() {
    timer.start();
    timer.pause();
    assertEquals(0, timer.getElapsedTime());
  }

  @Test
  public void elapsedTimeTest() {
    Arrays.asList(10, 100, 1, 33, 7, 111, 200, 2, 99)
            .forEach(this::elapsedTimeWithIntervalTest);
  }

  /**
   * Test if the timer works well by checking the start and pause method.
   *
   * @param interval interval of update of the timer
   */
  public void elapsedTimeWithIntervalTest(int interval) {
    long duration = 2000;   // in ms
    long pauseAfter = 111;  // in ms

    timer = new TimerDefault(duration, interval);

    timer.start();
    waitMs(pauseAfter);
    timer.pause();
    System.out.print(""); // magic trick

    assertTrue(timer.getElapsedTime() >= pauseAfter - 4*interval
            && timer.getElapsedTime() <= pauseAfter + 4*interval);
  }

  @Test
  public void pauseResumeTest() {
    timer.start();
    timer.pause();
    assertEquals(0, timer.getElapsedTime(), timer.interval);
    Arrays.asList(2, 43, 111, 20, 7, 10, 200, 151, 9, 11)
            .forEach(this::pauseResumeTest);
  }

  private void pauseResumeTest(long waitBetween) {
    long alreadyElapsed = timer.getElapsedTime();
    timer.resume();
    waitMs(waitBetween);
    timer.pause();
    assertEquals(alreadyElapsed + waitBetween, timer.getElapsedTime(), ERROR_MARGIN*Timer.DEFAULT_INTERVAL_MS);
  }

  @Test
  public void cancelTest() {
    timer.start();
    waitMs(28);
    timer.cancel();

    assertFalse(timer.isRunning());
    assertEquals(0, timer.getElapsedTime());
    waitMs(20); // wait a little more to be sure it is stopped
    assertEquals(0, timer.getElapsedTime());
  }

  @Test
  public void restartTest() {
    timer.start();
    waitMs(28);
    timer.restart();
    waitMs(55);

    assertTrue(timer.isRunning());
    timer.pause();

    assertFalse(timer.isRunning());
    assertEquals(55, timer.getElapsedTime(), ERROR_MARGIN*Timer.DEFAULT_INTERVAL_MS);
  }

  @Test
  public void legalCall() {
    timer = new TimerDefault(100);
    assertTrue(timer.isReady());
    timer.start();
    assertTrue(timer.isRunning());
    timer.pause();
    assertTrue(timer.isPaused());
    timer.resume();
    assertTrue(timer.isRunning());
    timer.cancel();
    assertTrue(timer.hasBeenCanceled());
    timer.start();
    assertTrue(timer.isRunning());
    timer.pause();
    assertTrue(timer.isPaused());
    timer.restart();
    assertTrue(timer.isRunning());
    timer.pause();
    assertTrue(timer.isPaused());
    timer.start();
    assertTrue(timer.isRunning());
    timer.restart();
    assertTrue(timer.isRunning());
    waitMs(120);

    assertTrue(timer.hasFinished());

    timer.reset();
    assertTrue(timer.isReady());
  }

  @Test
  public void illegalCall() {
    timer = new TimerDefault(100);
    assertTrue(timer.isReady());
    timer.cancel();
    assertTrue(timer.isReady());
    timer.pause();
    assertTrue(timer.isReady());
    timer.resume();
    assertTrue(timer.isReady());

    timer.start();
    assertTrue(timer.isRunning());

    timer.start();
    assertTrue(timer.isRunning());

    timer.reset();
    assertTrue(timer.isRunning());

    waitMs(120);
    assertTrue(timer.hasFinished());

    timer.reset();
    assertTrue(timer.isReady());
    timer.cancel();
    assertTrue(timer.isReady());
    timer.pause();
    assertTrue(timer.isReady());
    timer.reset();
    assertTrue(timer.isReady());
  }

  @Test
  public void remainingTimeTest() {
    long toWait = 123;

    timer.start();
    waitMs(toWait);
    timer.pause();

    assertEquals(TimerDefault.DEFAULT_DURATION - toWait, timer.getRemainingTime(), ERROR_MARGIN*Timer.DEFAULT_INTERVAL_MS);
  }

  /**
   * Blocking method: make the program wait a given amount of time
   *
   * @param ms time in milliseconds to stop the run of the program (thread at least)
   */
  void waitMs(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      // do nothing
    }
  }
}
