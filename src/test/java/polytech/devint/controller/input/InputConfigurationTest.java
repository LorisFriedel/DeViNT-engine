package polytech.devint.controller.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

import polytech.devint.event.basic.EscapeEvent;
import polytech.devint.event.basic.F1Event;
import polytech.devint.event.basic.F6Event;

/**
 * @author Loris Friedel
 */
public class InputConfigurationTest {

  InputConfiguration config;

  @Before
  public void init() {
    config = new InputConfiguration();
  }

  /**
   * Check if the default input configuration is properly set
   */
  @Test
  public void defaultConfiguration() {
    InputConfiguration config = new InputConfiguration();

    // The default configuration should be present
    assertTrue(config.getKeyPressedOnceEvents(KeyEvent.VK_F1).isPresent());
    assertTrue(config.getKeyPressedOnceEvents(KeyEvent.VK_ENTER).isPresent());
    assertTrue(config.getKeyPressedOnceEvents(KeyEvent.VK_SPACE).isPresent());
    assertTrue(config.getKeyPressedOnceEvents(KeyEvent.VK_F6).isPresent());
    assertTrue(config.getKeyPressedOnceEvents(KeyEvent.VK_ESCAPE).isPresent());
  }

  /**
   * Check if the binding removal is working from the string key and the event class
   */
  @Test
  public void removeBinding() {
    assertTrue(config.getKeyPressedOnceEvents(KeyEvent.VK_SPACE).isPresent());

    config.unbind(KeyEvent.VK_SPACE);
    assertFalse(config.getKeyPressedOnceEvents(KeyEvent.VK_SPACE).isPresent());

    config.unbind(F1Event.class);
    assertFalse(config.getKeyPressedOnceEvents(KeyEvent.VK_F1).isPresent());

    config.bindOnPressOnce(KeyEvent.VK_ESCAPE, EscapeEvent.class);
    assertEquals(2, config.getKeyPressedOnceEvents(KeyEvent.VK_ESCAPE).get().size());
  }

  /**
   * Multiple check on the addBinding method
   */
  @Test
  public void addBindingTest() {
    int defaultSize = config.keyPressedOnceControls.size();

    config.bindOnPressOnce(KeyEvent.VK_ESCAPE, F1Event.class);
    assertEquals(defaultSize, config.keyPressedOnceControls.size());
    config.bindOnPressOnce(KeyEvent.VK_ESCAPE, F1Event.class);
    assertEquals(defaultSize, config.keyPressedOnceControls.size());

    config.bindOnPressOnce(KeyEvent.VK_F10, F6Event.class);
    assertEquals(defaultSize + 1, config.keyPressedOnceControls.size());
    config.bindOnPressOnce(KeyEvent.VK_F10, F6Event.class);
    assertEquals(defaultSize + 1, config.keyPressedOnceControls.size());

    config.bindOnPressOnce(KeyEvent.VK_0, EscapeEvent.class);
    assertEquals(defaultSize + 2, config.keyPressedOnceControls.size());
  }
}
