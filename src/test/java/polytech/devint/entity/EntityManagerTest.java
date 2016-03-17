package polytech.devint.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Loris Friedel
 */
public class EntityManagerTest {

  EntityManager manager = new EntityManager();

  @Before
  public void init() {
    manager = new EntityManager();
  }

  @Test
  public void simpleAddEntityTest() {
    assertTrue(manager.entities.isEmpty());
    manager.addEntity(mock(Entity.class));
    assertEquals(1, manager.entities.size());
  }

  @Test
  public void simpleRemoveEntityTest() {
    Entity entity = mock(Entity.class);
    assertTrue(manager.entities.isEmpty());
    manager.addEntity(entity);
    assertEquals(1, manager.entities.size());

    manager.removeEntity(entity);
    assertTrue(manager.entities.isEmpty());
  }

  @Test
  public void updateEntities() {
    Entity aliveEntity = mock(Entity.class);
    Entity deadEntity = mock(Entity.class);
    Entity notSpawnedEntity = mock(Entity.class);
    when(aliveEntity.isDead()).thenReturn(false);
    when(notSpawnedEntity.isDead()).thenReturn(false);
    when(deadEntity.isDead()).thenReturn(true);

    manager.addEntity(aliveEntity);
    manager.addEntity(notSpawnedEntity);
    manager.addEntity(deadEntity);

    assertEquals(3, manager.entities.size());

    manager.update();

    assertEquals(2, manager.entities.size());
  }
}
