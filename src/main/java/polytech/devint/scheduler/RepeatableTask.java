package polytech.devint.scheduler;

/**
 * Represents a repeatable task
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class RepeatableTask implements Runnable {

  private boolean stop;
  private int loops;

  @Override
  public void run() {
    loops++;
    execute();
  }

  /**
   * Executed each loop
   */
  public abstract void execute();

  /**
   * @return The amount of time the execute() method has been called
   */
  public int getLoops() {
    return loops;
  }

  /**
   * Change the step of the task. It will stop next loop
   */
  public void stop() {
    this.stop = true;
  }

  /**
   * @return true if the task should stop
   */
  public boolean needToStop() {
    return stop;
  }

}
