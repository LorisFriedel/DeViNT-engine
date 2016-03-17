package polytech.devint.entity;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import polytech.devint.event.entity.ButtonClickEvent;
import polytech.devint.model.Model;
import polytech.devint.scheduler.MutableObject;

/**
 * Testing menus
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class MenuTest {

  /**
   * Testing if a button is properly being clicked from an extern event call
   */
  @Test
  public void testClickOneButton() {
    Model model = new Model() {};
    Menu menu = new Menu(model);
    MutableObject<Integer> button1Value = new MutableObject<>(0);
    MutableObject<Integer> button2Value = new MutableObject<>(0);
    // We increment their corresponding mutableObject to count the amount of clicks
    Button button1 =
        menu.addButton("toto", () -> button1Value.setValue(button1Value.getValue() + 1));
    Button button2 =
        menu.addButton("toto", () -> button2Value.setValue(button2Value.getValue() + 1));
    assertArrayEquals(new Button[] {button1, button2}, menu.getButtons().toArray(new Button[0]));
    // We simuate a click from outside
    model.getEventManager().notify(new ButtonClickEvent(button1));
    // Only the button 1 should have been pressed
    assertEquals(1, (int) button1Value.getValue());
    assertEquals(0, (int) button2Value.getValue());
  }

  /**
   * Testing if a button cannot be pressed when removed
   */
  @Test
  public void testRemoveButton() {
    Model model = new Model() {};
    Menu menu = new Menu(model);
    MutableObject<Integer> button1Value = new MutableObject<>(0);
    Button button1 = menu.addButton("t", () -> button1Value.setValue(button1Value.getValue() + 1));
    menu.removeButton(button1);
    // We simuate a click from outside
    model.getEventManager().notify(new ButtonClickEvent(button1));
    // We should have pressed 0 button
    assertEquals(0, (int) button1Value.getValue());
  }

}
