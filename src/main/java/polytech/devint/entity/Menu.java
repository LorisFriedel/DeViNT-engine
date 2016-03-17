package polytech.devint.entity;

import java.util.LinkedList;
import java.util.List;

import polytech.devint.entity.Button.ButtonTask;
import polytech.devint.event.EventHandler;
import polytech.devint.event.entity.ButtonClickEvent;
import polytech.devint.model.Model;

/**
 * A menu (can add/remove buttons)
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class Menu<M extends Model> extends Component<M> {

  // TODO this class menu uses only the entityManager present in the entityComposite class
  // TODO a model is not needed, so maybe rethink this whole thing ?

  List<Button<M>> buttonList;

  /**
   * Instantiates a new menu under a model with a given entity manager
   *
   * @param model         The model
   * @param entityManager The entity manager
   */
  public Menu(M model, EntityManager entityManager) {
    super(model, entityManager);
    this.buttonList = new LinkedList<>();
  }

  /**
   * Instantiates a new meny under a model with a new entity manager
   *
   * @param model The model
   */
  public Menu(M model) {
    super(model);
    this.buttonList = new LinkedList<>();
  }

  /**
   * Executed when a button is clicked
   *
   * @param event The corresponding event
   */
  @EventHandler
  public void onButtonClick(ButtonClickEvent event) {
    event.getEntity().update();
  }

  /**
   * Creates and adds a button from a lambda
   *
   * @param callback the task called when the button is pressed
   * @param name     the name of the button
   * @return the new button created
   */
  public Button<M> addButton(String name, ButtonTask callback) {
    Button<M> button = new Button<>(model, name, callback);
    addButton(button);
    return button;
  }

  /**
   * Adds a button and activates it if not already activated
   *
   * @param button The button to add
   */
  private void addButton(Button<M> button) {
    buttonList.add(button);
    entityManager.addEntity(button);
    if (!button.isAlive()) {
      button.activate();
    }
  }

  /**
   * Removes and terminates a button if the button is alive
   *
   * @param button The button
   */
  public void removeButton(Button<M> button) {
    if (button.isAlive()) {
      button.terminate();
    }
    entityManager.removeEntity(button);
    buttonList.remove(button);
  }

  @Override
  public void onRemove() {
    // Do nothing
  }

  @Override
  public void updateComponent() {
    // Do nothing

  }

  @Override
  public void activate() {
    // Do nothing
  }

  /**
   * @return A list of the different buttons
   */
  public List<Button<M>> getButtons() {
    return buttonList;
  }

}
