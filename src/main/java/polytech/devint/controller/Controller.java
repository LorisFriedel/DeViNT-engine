package polytech.devint.controller;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.event.Event;
import polytech.devint.event.EventManager;
import polytech.devint.model.Model;
import polytech.devint.view.View;

/**
 * @author Loris Friedel
 */
public abstract class Controller<M extends Model, V extends View<M>> {

  private static final Logger LOGGER = LogManager.getLogger(Controller.class);

  private final EventManager eventManager;
  private final InputConfiguration inputConfiguration;
  protected final Set<Integer> waitingRelease;
  protected final List<V> views;
  protected M model;

  /**
   * Create a controller from a unique model and input configuration (see MVC pattern)
   *
   * @param model              model that the controller will control
   * @param inputConfiguration input configuration the controller is ruled by
   */
  public Controller(M model, InputConfiguration inputConfiguration) {
    this.model = model;
    this.inputConfiguration = inputConfiguration;
    this.waitingRelease = new HashSet<>();
    this.eventManager = new EventManager();
    this.eventManager.registerObserver(this);
    this.views = new ArrayList<>();
  }

  /**
   * Create a controller from a default input config
   *
   * @param model model that the controller will control
   */
  public Controller(M model) {
    this(model, new InputConfiguration());
  }

  /**
   * Get the model that the controller is controlling for now
   *
   * @return the model that the controller is controlling for now
   */
  public M getModel() {
    return model;
  }

  /**
   * Get the event manager that links the observer (this) to its observables (the views)
   *
   * @return the event manager that links the observer (this) to its observables (the views)
   */
  public EventManager getEventManager() {
    return eventManager;
  }

  /**
   * Send a notification to the current controller.
   * This method does the exact same thing as the <code>notifyObserver(Event event)</code> method.
   *
   * @param event event to send to itself
   */
  public void notifySelf(Event event) {
    notifyObserver(event);
  }

  /**
   * Send a notification to the current controller (this).
   *
   * @param event event to send to itself
   */
  public void notifyObserver(Event event) {
    getEventManager().notify(event);
  }

  /**
   * Change the model linked to the current controller.
   * This method should be used CAREFULLY.
   *
   * @param newModel new model that will replace the previous one
   */
  public void replaceModel(M newModel) {
    this.model = newModel;
  }

  /**
   * Add a view to the list of controlled views by the current controller The view will be listened
   * by the controller
   *
   * @param view view to add to the list of views of the controller
   */
  public void addView(V view) {
    views.add(view);
    view.setController(this);
  }

  /**
   * Remove the given view from the list of views controlled by the current controller The view will
   * stop being listened by the controller
   *
   * @param view view to remove
   */
  public void removeView(V view) {
    views.remove(view);
    view.setController(null);
  }

  /**
   * Update all active views
   */
  public void updateViews() {
    getViews().forEach(v -> {
      if (v.isActive()) {
        v.update();
      }
    });
  }

  /**
   * @return all view currently linked to this controller
   */
  public List<V> getViews() {
    return views;
  }

  /**
   * @return The input configuration
   */
  public InputConfiguration getInputConfiguration() {
    return inputConfiguration;
  }


  public void pressKey(Integer keyCode) {
    List<Class<? extends Event>> events = new ArrayList<>();

    Optional<List<Class<? extends Event>>> pressEvents = getInputConfiguration().getKeyPressedEvents(keyCode);
    Optional<List<Class<? extends Event>>> pressOnceEvents = getInputConfiguration().getKeyPressedOnceEvents(keyCode);

    if (!waitingRelease.contains(keyCode) && pressOnceEvents.isPresent()) {
      waitingRelease.add(keyCode);
      events.addAll(pressOnceEvents.get());
    }

    if (pressEvents.isPresent()) {
      events.addAll(pressEvents.get());
    }

    notifyAll(events);
  }

  public void releaseKey(Integer keyCode) {
    waitingRelease.remove(keyCode);

    Optional<List<Class<? extends Event>>> releaseEvents = getInputConfiguration().getKeyReleasedEvents(keyCode);

    if (releaseEvents.isPresent()) {
      notifyAll(releaseEvents.get());
    }
  }

  private void notifyAll(List<Class<? extends Event>> events) {
    events.forEach(event -> {
      try {
        notifySelf(event.getConstructor().newInstance());
      } catch (Exception e) {
        LOGGER.error("Error while invoking " + event + ":", e);
      }
    });
  }
}
