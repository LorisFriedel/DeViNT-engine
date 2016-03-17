package polytech.devint.entity;

import polytech.devint.event.entity.EntityCompositeUpdateEvent;
import polytech.devint.model.Model;

/**
 * An entity containing entities
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 * @author Loris Friedel
 */
public abstract class EntityComposite<M extends Model> extends Entity<M> {

  protected final EntityManager entityManager;

  /**
   * Instantiates a new composite entity on a given model
   *
   * @param model The model
   */
  public EntityComposite(M model) {
    this(model, new EntityManager());
  }

  /**
   * Instantiates a new composite entity with a given entity manager on a given model
   *
   * @param entityManager The entity manager
   * @param model         The model
   */
  public EntityComposite(M model, EntityManager entityManager) {
    super(model);
    this.entityManager = entityManager;
  }

  /**
   * Adds a new entity and activates it
   *
   * @param entity the entity we want to add
   */
  public void addEntity(Entity entity) {
    getEntityManager().addEntity(entity);
  }

  /**
   * Removes an entity and terminates it
   *
   * @param entity the entity to be removed
   */
  public void removeEntity(Entity entity) {
    entityManager.removeEntity(entity);
  }

  @Override
  public void update() {
    EntityCompositeUpdateEvent event = new EntityCompositeUpdateEvent(this);
    model.getEventManager().notify(event);

    // If the event is cancelled, we don't update the entity
    if (event.isCancelled()) {
      return;
    }

    // We update all the entities every update
    entityManager.update();
    updateComposite();
  }

  /**
   * Called when a composite entity updates
   */
  public abstract void updateComposite();

  public EntityManager getEntityManager() {
    return entityManager;
  }
}
