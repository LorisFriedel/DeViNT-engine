package polytech.devint.model;

import polytech.devint.event.Event;
import polytech.devint.event.Observable;

/**
 * Represents an abstract model (see MVC pattern)
 *
 * @author Loris Friedel
 */
public abstract class Model extends Observable {

  /**
   * Instantiates a new model
   */
  public Model() {
    super();
  }

  /**
   * WARNING, read carefully.
   * Notify all views that observes this model with the given event
   * This method does the exact same thing as the <code>notifyObserver(Event event)</code> method.
   * The given event is sent to every observer of this model, VIEW OR NOT.
   *
   * @param event event that will be sent to all views that observes this model
   */
  public void notifyViews(Event event) {
    notifyObservers(event);
  }
}
