package polytech.devint.model;

import polytech.devint.event.Event;
import polytech.devint.event.EventManager;

/**
 * Represents an abstract model (see MVC pattern)
 *
 * @author Loris Friedel
 */
public abstract class Model {

  private final EventManager eventManager;

  /**
   * Instantiates a new model with one preexisting event manager
   *
   * @param eventManager a preexisting event manager
   */
  public Model(EventManager eventManager) {
    this.eventManager = eventManager;
  }

  /**
   * Instantiates a new model
   */
  public Model() {
    this(new EventManager());
  }

  /**
   * @return the model's event manager
   */
  public EventManager getEventManager() {
    return eventManager;
  }

  /**
   * Notify all views that observes this model with the given event
   *
   * @param event event that will be sent to all views that observes this model
   */
  public void notifyViews(Event event) {
    getEventManager().notify(event);
  }
}
