package polytech.devint.entity;

import polytech.devint.event.entity.ComponentRemoveEvent;
import polytech.devint.event.entity.ComponentUpdateEvent;
import polytech.devint.model.Model;

/**
 * Represents a component, a composite entity & observer
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class Component<M extends Model> extends EntityComposite<M> {

  /**
   * Instantiates a new component
   *
   * @param model         The model containing the component
   * @param entityManager The entity manager
   */
  public Component(M model, EntityManager entityManager) {
    super(model, entityManager);
    this.model.getEventManager().registerObserver(this);
  }

  /**
   * Instantiates a new component with a new entity manager
   *
   * @param model The model containing the component
   */
  public Component(M model) {
    this(model, new EntityManager());
  }

  @Override
  public void updateComposite() {
    ComponentUpdateEvent event = new ComponentUpdateEvent(this);
    model.getEventManager().notify(event);
    // If the event hasn't been cancelled
    if (!event.isCancelled()) {
      updateComponent();
    }
  }

  @Override
  public void terminate() {
    ComponentRemoveEvent event = new ComponentRemoveEvent(this);
    model.getEventManager().notify(event);
    // If the event hasn't been cancelled
    if (!event.isCancelled()) {
      this.model.getEventManager().removeObserver(this);
      onRemove();
    }
  }

  /**
   * Called when a component is removed
   */
  public abstract void onRemove();

  /**
   * Called when a component is updating
   */
  public abstract void updateComponent();
}
