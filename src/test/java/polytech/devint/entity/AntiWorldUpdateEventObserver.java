package polytech.devint.entity;

import polytech.devint.event.EventHandler;
import polytech.devint.event.entity.WorldUpdateEvent;

/**
 * Used to cancel world update events
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class AntiWorldUpdateEventObserver {

  @EventHandler
  public void onWorldUpdate(WorldUpdateEvent event) {
    // We cancel the event
    event.setCancelled(true);
  }
}
