package polytech.devint.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Testing the event manager
 * 
 * @author Günther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class EventManagerTest {

  /**
   * Testing if the current method handlers are being called
   */
  @Test
  public void testEvents() {
    EventManager eventManager = new EventManager();
    TestObserver tester = new TestObserver();
    eventManager.registerObserver(tester);
    CustomEvent customEvent = new CustomEvent();
    // Fields should be false by default
    assertFalse(tester.onEvent);
    assertFalse(tester.onCustomEvent);
    eventManager.notify(customEvent);
    // Only the method onCustomEvent should be called
    assertFalse(tester.onEvent);
    assertTrue(tester.onCustomEvent);
  }

  /**
   * 
   * Testing if the observer has been properly registered
   */
  @Test
  public void testRegister() {
    EventManager eventManager = new EventManager();
    TestObserver tester = new TestObserver();
    eventManager.registerObserver(tester);
    assertTrue(eventManager.hasObserver(tester));
    assertTrue(eventManager.observers.containsKey(tester));
  }

  /**
   * 
   * Testing if the observer has been properly removed
   */
  @Test
  public void testRemove() {
    EventManager eventManager = new EventManager();
    TestObserver tester = new TestObserver();
    eventManager.registerObserver(tester);
    eventManager.removeObserver(tester);
    assertFalse(eventManager.hasObserver(tester));
    assertFalse(eventManager.observers.containsKey(tester));
  }

  /**
   * 
   * Testing if the observer has been properly removed after calling removeAllObservers()
   */
  @Test
  public void testRemoveAll() {
    EventManager eventManager = new EventManager();
    TestObserver tester = new TestObserver();
    eventManager.registerObserver(tester);
    eventManager.removeAllObservers();
    assertFalse(eventManager.hasObserver(tester));
    assertFalse(eventManager.observers.containsKey(tester));
  }

  /**
   * Testing if the methods have been properly added and removed on/to the event manager
   */
  @Test
  public void testHasEvent() {
    EventManager eventManager = new EventManager();
    TestObserver tester = new TestObserver();
    eventManager.registerObserver(tester);
    assertEquals(1, eventManager.observers.size());
    assertEquals(2, eventManager.observers.get(tester).size());
  }
}
