package polytech.devint.event;

/**
 * Class used for testing purpose.
 * 
 * @author Günther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class TestObserver {

  boolean onEvent, onCustomEvent;

  @EventHandler
  public void onEvent(Event event) {
    onEvent = true;
  }

  @EventHandler
  public void onCustomEvent(CustomEvent event) {
    onCustomEvent = true;
  }
}
