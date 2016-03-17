package polytech.devint.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import polytech.devint.model.Model;

/**
 * @author Loris Friedel
 */
public class EntityTest {

  @Test
  public void uniqueIdentification() {
    Model model = mock(Model.class);
    EntityDefault entityDefaultFirst = new EntityDefault(model);
    EntityDefault entityDefaultSecond = new EntityDefault(model);
    EntityDefault entityDefaultThird = new EntityDefault(model);

    assertNotEquals(entityDefaultFirst.getId(), entityDefaultSecond.getId());
    assertNotEquals(entityDefaultSecond.getId(), entityDefaultThird.getId());
    assertNotEquals(entityDefaultFirst.getId(), entityDefaultThird.getId());
  }

  /**
   * Test all the activate, update and terminate method in a "basic" cycle of an entity activate ->
   * update (x times) -> terminate
   */
  @Test
  public void basicCycle() {
    Model model = mock(Model.class);
    EntityDefault entityDefault = new EntityDefault(model);

    assertTrue(entityDefault.isNotSpawned());
    assertFalse(entityDefault.isAlive());
    assertFalse(entityDefault.isDead());
    assertEquals(0, entityDefault.cptTest);

    entityDefault.activate();

    assertTrue(entityDefault.isAlive());
    assertFalse(entityDefault.isDead());
    assertFalse(entityDefault.isNotSpawned());

    entityDefault.update();

    assertTrue(entityDefault.isAlive());
    assertEquals(1, entityDefault.cptTest);

    entityDefault.update();

    assertTrue(entityDefault.isAlive());
    assertEquals(2, entityDefault.cptTest);

    entityDefault.terminate();

    assertFalse(entityDefault.isAlive());
    assertTrue(entityDefault.isDead());
    assertFalse(entityDefault.isNotSpawned());
  }
}
