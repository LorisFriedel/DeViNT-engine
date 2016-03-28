package polytech.devint.view;

import polytech.devint.controller.Controller;
import polytech.devint.event.Event;
import polytech.devint.model.Model;

/**
 * Represents a view (see MVC pattern)
 *
 * @param <M> the model type linked to this view
 * @author Antoine Aubé (aube.antoine@gmail.com)
 * @author Loris Friedel
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class View<M extends Model> {

  protected Controller<M, ?> controller;
  private boolean isActive;

  /**
   * This attribute is not updated if there is a change of model
   * in the controller that is linked to this view   *
   */
  @Deprecated
  protected M model;


  /**
   * Instantiates an empty view with no controller The controller and model of the view will be
   * associated when the view is added to the controller
   */
  public View() {

  }

  /**
   * Initializes the view
   */
  public void init() {
    register();
    setupContent();
    isActive = true;
  }

  /**
   * Destroys the view
   */
  public void destroy() {
    unregister();
    destroyContent();
    isActive = false;
  }

  /**
   * Sets any content required by this view
   */
  public abstract void setupContent();

  /**
   * Removes any content we would not required after this view's removal
   */
  public abstract void destroyContent();

  /**
   * Makes the view being an observer of the associated model
   */
  private void register() {
    getLinkedModel().getEventManager().registerObserver(this);
  }

  /**
   * Makes the view not being an observer of the associated model anymore
   */
  private void unregister() {
    getLinkedModel().getEventManager().removeObserver(this);
  }

  /**
   * @return the controller associated to the current view
   */
  public Controller<M, ?> getController() {
    return controller;
  }

  /**
   * @return the model associated to the current view
   */
  public M getLinkedModel() {
    return controller.getModel();
  }

  /**
   * Set the current controller of the view for the controller-view interaction
   *
   * @param controller new controller that will be connected to this view
   */
  public void setController(Controller<M, ?> controller) {
    this.controller = controller;
    this.model = controller.getModel();
  }

  /**
   * Executed when a views needs to be updated
   */
  public abstract void update();

  /**
   * @return True if the view is active, false if not
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * Notify the controller that is currently attached to this view
   *
   * @param event event that will be sent to the controller of this view
   */
  public void notifyController(Event event) {
    controller.getEventManager().notify(event);
  }
}
