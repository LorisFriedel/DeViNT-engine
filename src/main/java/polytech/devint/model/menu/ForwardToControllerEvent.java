package polytech.devint.model.menu;

import polytech.devint.event.Event;

/**
 * @author Loris Friedel
 */
public class ForwardToControllerEvent implements Event {

  private Event eventToForward;

  public ForwardToControllerEvent(Event eventToForward) {
    this.eventToForward = eventToForward;
  }

  public Event getEventToForward() {
    return eventToForward;
  }
}
