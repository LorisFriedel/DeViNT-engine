package polytech.devint.event.entity;

import polytech.devint.entity.Entity;
import polytech.devint.event.CancellableEvent;

/**
 * Event corresponding to a certain entity update
 *
 * @param <E> The type of the entity
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class EntityEvent<E extends Entity> extends CancellableEvent {

  private final E entity;

  /**
   * Instantiates a new entity update event
   *
   * @param entity
   */
  public EntityEvent(E entity) {
    this.entity = entity;
  }

  /**
   * @return The entity
   */
  public E getEntity() {
    return entity;
  }

}
