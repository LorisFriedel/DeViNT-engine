package polytech.devint.entity;

import polytech.devint.event.entity.WorldUpdateEvent;
import polytech.devint.model.Model;

/**
 * Represents a world
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class World<M extends Model> extends EntityComposite<M> {

  /**
   * Instantiates a new world under a model
   *
   * @param model The model
   */
  public World(M model) {
    super(model);
  }

  /**
   * Instantiates a new world under a model with a certain entity manager
   *
   * @param model         The model
   * @param entityManager The entity manager
   */
  public World(M model, EntityManager entityManager) {
    super(model, entityManager);
  }

  @Override
  public void updateComposite() {
    WorldUpdateEvent event = new WorldUpdateEvent(this);
    model.getEventManager().notify(event);
    if (!event.isCancelled()) {
      updateWorld();
    }
  }

  /**
   * Executed when a world updates
   */
  public void updateWorld() {
    // Do nothing
  }

  @Override
  public void activate() {
    // We do nothing, all its entities are activated by default
  }

  @Override
  public void terminate() {
    // We terminate all its entities
    this.entityManager.entities.forEach(e -> {
      if (e.isAlive()) {
        e.terminate();
      }
    });
  }

}
