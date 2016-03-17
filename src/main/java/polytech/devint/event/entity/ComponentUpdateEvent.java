package polytech.devint.event.entity;

import polytech.devint.entity.Component;

/**
 * Event corresponding to a component being updated
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class ComponentUpdateEvent extends EntityEvent<Component> {

  /**
   * Instantiates a new component update event
   *
   * @param entity The entity being updated
   */
  public ComponentUpdateEvent(Component entity) {
    super(entity);
  }
}
