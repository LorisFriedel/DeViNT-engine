package polytech.devint.event.entity;

import polytech.devint.entity.EntityComposite;

/**
 * Event corresponding to a composite entity update
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class EntityCompositeUpdateEvent extends EntityEvent<EntityComposite> {

  /**
   * Instantiates a new entity composite event
   *
   * @param entity The entity updating
   */
  public EntityCompositeUpdateEvent(EntityComposite entity) {
    super(entity);
  }
}
