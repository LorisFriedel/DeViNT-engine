package polytech.devint.entity;

import polytech.devint.event.EventHandler;
import polytech.devint.event.entity.ComponentRemoveEvent;

/**
 * Used to cancel components remove events
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class AntiComponentRemoveEventObserver {

  @EventHandler
  public void onComponentRemove(ComponentRemoveEvent event) {
    // We cancel the event
    event.setCancelled(true);
  }
}
