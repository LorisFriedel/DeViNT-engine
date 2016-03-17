package polytech.devint.event;

/**
 * Represents an event which can be cancelled
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class CancellableEvent implements Event {

  private boolean cancelled;

  /**
   * @return True if the event has been cancelled
   */
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Sets if whether or not the event should be cancelled
   *
   * @param flag true = event will be cancelled
   */
  public void setCancelled(boolean flag) {
    this.cancelled = flag;
  }
}
