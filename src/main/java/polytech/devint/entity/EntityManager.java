package polytech.devint.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * An entity manager, that contains a list of entity We can add, update and remove entity from the
 * manager Each update, all dead entities are terminated then removed from the list
 *
 * @author Loris Friedel
 */
public class EntityManager {

  List<Entity> entities; // entities to manage

  /**
   * Instantiate an empty entity manager
   */
  public EntityManager() {
    this.entities = new CopyOnWriteArrayList<>();
  }

  /**
   * Add an entity that will be managed by the current entity manager
   *
   * @param entity entity to add to the manager
   */
  public void addEntity(Entity entity) {
    entities.add(entity);
    if (entity.isNotSpawned()) {
      entity.setAlive();
      entity.activate();
    }
  }

  /**
   * Remove an entity from the entity list of the manager
   *
   * @param entity entity to remove
   */
  public void removeEntity(Entity entity) {
    entities.remove(entity);
    if (!entity.isDead()) {
      entity.setDead();
      entity.terminate();
    }
  }

  /**
   * Update all entities. If the entity is dead, it doesn't update it but terminate it and remove it
   * from the entities list
   */
  public void update() {
    List<Entity> copy = new CopyOnWriteArrayList<>();
    int size = entities.size();
    getEntities().forEach(toUpdate -> {
      if (toUpdate.isDead()) {
        toUpdate.terminate();
      } else {
        copy.add(toUpdate);
        if (toUpdate.isAlive()) {
          toUpdate.update();
        }
      }
    });
    copy.addAll(entities.subList(size, entities.size()));
    entities = copy;
  }

  /**
   * Executes an action on all the entities
   * @param action
   */
  public void foreach(Consumer<? super Entity> action) {
    getEntities().forEach(action);
  }

  /**
   * 
   * @return A copy of the list containing the entities
   */
  public List<Entity> getEntities() {
    return new CopyOnWriteArrayList<>(entities);
  }
}
