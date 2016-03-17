package polytech.devint.entity;

import polytech.devint.model.Model;

/**
 * Represents a button
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class Button<M extends Model> extends Entity<M> {
//TODO a button doesn't need a model, maybe an entity doesn't always need a model? Maybe deeply rethink this

  /**
   * An interface used to manage button clicks
   *
   * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
   */
  @FunctionalInterface
  public interface ButtonTask {
    void onClick();
  }

  private final ButtonTask callback;
  private final String name;

  /**
   * Instantiates a new button under a model with a callback
   *
   * @param model    The model
   * @param name     The name of the button
   * @param callback The callback (run() will be executed when the button is pressed
   */
  public Button(M model, String name, ButtonTask callback) {
    super(model);
    this.callback = callback;
    this.name = name;
  }

  @Override
  public void activate() {
    setAlive();
  }

  @Override
  public void update() {
    if (isAlive()) {
      callback.onClick();
    }
  }

  @Override
  public void terminate() {
    setDead();
  }

  /**
   * @return The name of the button
   */
  public String getName() {
    return name;
  }
}
