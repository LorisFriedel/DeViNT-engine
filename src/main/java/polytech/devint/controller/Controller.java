package polytech.devint.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.entity.Game;
import polytech.devint.event.Event;
import polytech.devint.event.EventManager;
import polytech.devint.model.Model;
import polytech.devint.scheduler.RepeatableTask;
import polytech.devint.scheduler.Scheduler;
import polytech.devint.view.View;

/**
 * @author Loris Friedel
 */
public abstract class Controller<M extends Model, V extends View<M>> {

  static final Logger LOGGER = LogManager.getLogger(Controller.class);
  private static final int INTERACTION_INTERVAL = 35;
  protected final EventManager eventManager;
  final InputConfiguration inputConfiguration;
  protected final List<V> views;
  protected final List<Key> pressedKeys;
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
    this.eventManager = new EventManager();
    this.eventManager.registerObserver(this);
    this.views = new ArrayList<>();
    this.pressedKeys = new ArrayList<>();
    Scheduler.getDefaultScheduler().execute(new RepeatableTask() {

      @Override
      public void execute() {
        pressedKeys.forEach((key) -> pressKey(key));
      }
    }, 0, INTERACTION_INTERVAL, TimeUnit.MILLISECONDS);
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
    views.forEach(v -> {
      if (v.isActive()) {
        v.update();
      }
    });
  }

  /**
   * Executes a lambda on each view
   */
  public void forEachViews(Consumer<V> consumer) {
    views.forEach(consumer);
  }

  /**
   * @return The input configuration
   */
  public InputConfiguration getInputConfiguration() {
    return inputConfiguration;
  }

  /**
   * Adds a key being pressed from its code
   *
   * @param code
   */
  public void pressKey(int code) {
    Key key = getKey(code);
    if (key == null) {
      key = new Key(code);
      pressedKeys.add(key);
    }
    pressKey(key);
  }

  public void pressKey(Key key) {
    Optional<List<Class<? extends Event>>> events = getInputConfiguration().getEvents(key.code);
    if (events.isPresent()) {
      List<Class<? extends Event>> eventList = events.get();
      eventList.forEach(e -> {
        try {
          getEventManager().notify(e.getConstructor().newInstance());
        } catch (Exception e1) {
          LOGGER.error("Invalid event constructor", e);
        }
      });
    }
  }

  private Controller<M, V>.Key getKey(int code) {
    for (Key k : pressedKeys) {
      if (k.code == code) {
        return k;
      }
    }
    return null;
  }

  /**
   * Removes a key being pressed from its code
   *
   * @param code
   */
  public void unpressKey(int code) {
    Key key = getKey(code);
    pressedKeys.remove(key);
    if (key != null && canPress(key)) {
      pressKey(key);
    }
  }

  private boolean canPress(Key key) {
    return System.currentTimeMillis() - key.code > INTERACTION_INTERVAL;
  }

  /**
   * Resets the controller inputs
   */
  public void resetKeys() {
    pressedKeys.clear();
  }

  private class Key {
    final int code;

    public Key(int code) {
      super();
      this.code = code;
    }
  }
}
