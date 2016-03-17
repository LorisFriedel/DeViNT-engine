package polytech.devint.event.entity;

import polytech.devint.entity.Component;

/**
 * Event corresponding to a component being removed
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class ComponentRemoveEvent extends EntityEvent<Component> {

  /**
   * Instantiates a new component remove event
   *
   * @param entity The entity being removed
   */
  public ComponentRemoveEvent(Component entity) {
    super(entity);
  }
}
