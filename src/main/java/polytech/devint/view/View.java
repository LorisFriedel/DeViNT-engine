package polytech.devint.view;

import polytech.devint.controller.Controller;
import polytech.devint.event.Event;
import polytech.devint.event.Observable;
import polytech.devint.model.Model;

/**
 * Represents a view (see MVC pattern)
 *
 * @param <M> the model type linked to this view
 * @author Antoine Aubé (aube.antoine@gmail.com)
 * @author Loris Friedel
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class View<M extends Model> extends Observable {

  protected Controller<M, ?> controller;
  private boolean isActive;

  /**
   * Instantiates an empty view with no controller The controller and model of the view will be
   * associated when the view is added to the controller
   */
  public View() {
    super();
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
   * Makes the view being an observer of the associated model.
   */
  private void register() {
    getLinkedModel().addObserver(this);
  }

  /**
   * Makes the view not being an observer of the associated model anymore.
   */
  private void unregister() {
    getLinkedModel().deleteObserver(this);
  }

  /**
   * @return The model associated to the current view.
   */
  public M getLinkedModel() {
    return getController().getModel();
  }

  /**
   * Set the current controller of the view for the controller-view interaction
   *
   * @param controller new controller that will be connected to this view
   */
  public void setController(Controller<M, ?> controller) {
    if(getController() != null) {
      deleteObserver(getController());
    }
    this.controller = controller;
    addObserver(controller);
  }

  /**
   * @return The controller associated to the current view.
   */
  public Controller<M, ?> getController() {
    return controller;
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
   * WARNING, read carefully.
   * Notify the controller that is currently attached to this view.
   * This method does the exact same thing as the <code>notifyObserver(Event event)</code> method.
   * The given event is sent to every observer of this view, CONTROLLER OR NOT.
   *
   * @param event Event that will be sent to the controller (and other observer) of this view
   */
  public void notifyController(Event event) {
    notifyObservers(event);
  }
}
