package polytech.devint.entity;

import polytech.devint.model.Model;
import polytech.devint.util.Identifiable;

/**
 * Represent an abstract entity It has a state, can be activated, updated and terminated It also has
 * a unique long identifier (set on instantiation)
 *
 * @author Loris Friedel
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class Entity<M extends Model> extends Identifiable<Long> {

  public enum State {
    NOT_SPAWNED, ALIVE, DEAD
  }

  // Class attributes
  private static long nextId = 0;

  // Instance attributes
  private State state;
  protected final M model;

  /**
   * Instantiates the entity on a given model
   *
   * @param model The model
   */
  public Entity(M model) {
    super(nextId++);
    this.state = State.NOT_SPAWNED;
    this.model = model;
  }

  /**
   * Logic to process when the entity is activated
   */
  public abstract void activate();


  /**
   * Logic to process when the entity is updated
   */
  public abstract void update();


  /**
   * Logic to process when the entity is killed
   */
  public abstract void terminate();

  /**
   * Set the entity to the ALIVE state
   */
  public void setAlive() {
    this.state = State.ALIVE;
  }

  /**
   * Set the entity to the DEAD state
   */
  public void setDead() {
    this.state = State.DEAD;
  }

  /**
   * Set the entity to the NOT_SPAWNED state
   */
  public void setNotSpawned() {
    this.state = State.NOT_SPAWNED;
  }

  /**
   * Check if the entity is alive
   *
   * @return true if the entity is in the state ALIVE
   */
  public boolean isAlive() {
    return state.equals(State.ALIVE);
  }

  /**
   * Check if the entity is dead
   *
   * @return true if the entity is in the state DEAD
   */
  public boolean isDead() {
    return state.equals(State.DEAD);
  }

  /**
   * Check if the entity is not spawned
   *
   * @return true if the entity is in the state NOT_SPAWNED
   */
  public boolean isNotSpawned() {
    return state.equals(State.NOT_SPAWNED);
  }
}
