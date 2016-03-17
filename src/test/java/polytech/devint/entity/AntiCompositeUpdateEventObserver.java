package polytech.devint.entity;

import polytech.devint.event.EventHandler;
import polytech.devint.event.entity.EntityCompositeUpdateEvent;

/**
 * Used to cancel composite update events
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class AntiCompositeUpdateEventObserver {

  @EventHandler
  public void onCompositeUpdate(EntityCompositeUpdateEvent event) {
    // We cancel the event
    event.setCancelled(true);
  }
}
