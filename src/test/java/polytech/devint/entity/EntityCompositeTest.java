package polytech.devint.entity;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import polytech.devint.model.Model;

/**
 * Testing updates on the composite entity
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class EntityCompositeTest {

  /**
   * Testing if the update is properly cancelled
   */
  @Test
  public void testUpdateCancelled() {
    // We need a new model
    Model model = new Model() {};
    // We create an observer cancelling EntityCompositeEvent events
    AntiCompositeUpdateEventObserver observer = new AntiCompositeUpdateEventObserver();
    model.getEventManager().registerObserver(observer);
    EntityComposite composite = spy(new EmptyEntityComposite(model));
    composite.update();
    verify(composite, times(0)).updateComposite();
  }

  /**
   * Testing if the update is properly called
   */
  @Test
  public void testUpdateEntities() {
    // We need a new model
    Model model = new Model() {};
    EntityComposite composite = spy(new EmptyEntityComposite(model));
    Entity entity = mock(Entity.class);
    when(entity.isAlive()).thenReturn(true).thenReturn(false);
    composite.addEntity(entity);
    composite.update();
    composite.removeEntity(entity);
    composite.update();
    verify(composite, times(2)).updateComposite();
    verify(entity, times(1)).update();
  }

  /**
   * Testing if an entity is properly being added
   */
  @Test
  public void testAddEntity() {
    // We need a new model
    Model model = new Model() {};
    EntityComposite composite = spy(new EmptyEntityComposite(model));
    Entity entity = spy(new EmptyEntityComposite(model));
    composite.addEntity(entity);
    assertTrue(entity.isAlive());
    verify(entity, times(1)).activate();
  }

  /**
   * Testing if the update is properly called
   */
  @Test
  public void testRemoveEntity() {
    // We need a new model
    Model model = new Model() {};
    EntityComposite composite = spy(new EmptyEntityComposite(model));
    Entity entity = spy(new EmptyEntityComposite(model));
    composite.addEntity(entity);
    composite.removeEntity(entity);
    verify(entity, times(1)).terminate();
    assertTrue(entity.isDead());
  }

}


