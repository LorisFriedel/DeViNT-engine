package polytech.devint.util.time;

/**
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class Timeout {

  private long lastProcessed;
  private final long timeToWaitMs;

  /**
   * Creates a new Timeout Util object
   *
   * @param timeToWaitMs The delta between each process
   */
  public Timeout(long timeToWaitMs) {
    this.timeToWaitMs = timeToWaitMs;
  }

  /**
   * @return True if the last proceed() call was executed after the timeToWaitMs delay
   */
  private boolean canProceed() {
    return System.currentTimeMillis() - lastProcessed > timeToWaitMs;
  }

  /**
   * Proceed a time update
   */
  private void proceed() {
    lastProcessed = System.currentTimeMillis();
  }

  /**
   * Executes a function if canProceed() is true
   *
   * @param method
   */
  public void tryTo(Runnable method) {
    if (canProceed()) {
      proceed();
      method.run();
    }
  }
}
