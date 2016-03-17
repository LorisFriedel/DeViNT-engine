package polytech.devint.entity;

import polytech.devint.event.EventHandler;
import polytech.devint.event.entity.ComponentUpdateEvent;

/**
 * Used to cancel components update events
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class AntiComponentUpdateEventObserver {

  @EventHandler
  public void onComponentUpdate(ComponentUpdateEvent event) {
    // We cancel the event
    event.setCancelled(true);
  }
}
