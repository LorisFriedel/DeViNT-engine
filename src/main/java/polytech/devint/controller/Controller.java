package polytech.devint.controller;

import java.util.*;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.event.Observable;
import polytech.devint.model.Model;
import polytech.devint.view.View;

/**
 * @author Loris Friedel
 */
public abstract class Controller<M extends Model, V extends View<M>> extends Observable {

  private final InputConfiguration inputConfiguration;
  private final List<V> views;
  private final M model;

  /**
   * Create a controller from a unique model and input configuration (see MVC pattern)
   *
   * @param model              model that the controller will control
   * @param inputConfiguration input configuration the controller is ruled by
   */
  public Controller(M model, InputConfiguration inputConfiguration) {
    this.model = model;
    this.inputConfiguration = inputConfiguration;
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
   * @return the model that the controller is controlling for now
   */
  public M getModel() {
    return model;
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
   * Remove the given view from the list of views controlled by the current controller
   * The removed view will stop being listened by the controller.
   *
   * @param view View to be removed.
   */
  public void removeView(V view) {
    views.remove(view);
    view.setController(null);
  }

  /**
   * Update all active views.
   */
  public void updateViews() {
    getViews().forEach(v -> {
      if (v.isActive()) {
        v.update();
      }
    });
  }

  /**
   * @return All view currently linked to this controller.
   */
  public List<V> getViews() {
    return views;
  }

  /**
   * @return The input configuration of this controller.
   */
  public InputConfiguration getInputConfiguration() {
    return inputConfiguration;
  }
}
