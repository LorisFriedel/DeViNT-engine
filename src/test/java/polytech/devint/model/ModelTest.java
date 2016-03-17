package polytech.devint.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import polytech.devint.event.EventManager;


public class ModelTest {

  @Test
  public void testGetEventManager() {
    EventManager eventManager = new EventManager();
    Model model = new Model(eventManager) {};

    assertEquals(eventManager, model.getEventManager());
  }
}
