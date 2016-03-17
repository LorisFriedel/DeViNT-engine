package polytech.devint.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import polytech.devint.model.Model;
import polytech.devint.scheduler.MutableObject;

/**
 * Testing buttons
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class ButtonTest {

  /**
   * Testing if the update method of a button is properly being called
   */
  @Test
  public void updateTest() {
    Model model = new Model() {};
    MutableObject<Integer> loops = new MutableObject<>(0);
    Button button = new Button(model, "test 1", () -> loops.setValue(loops.getValue() + 1));
    assertEquals("test 1", button.getName());
    button.activate();
    button.update();
    button.terminate();
    button.update();
    // Should be called only once
    assertEquals(1, (int) loops.getValue());
  }

}
