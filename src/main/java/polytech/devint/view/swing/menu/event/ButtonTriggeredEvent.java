package polytech.devint.view.swing.menu.event;

import polytech.devint.event.Event;
import polytech.devint.view.swing.menu.Button;

/**
 * @author Loris Friedel
 */
public class ButtonTriggeredEvent implements Event {

  private Button button;

  public ButtonTriggeredEvent(Button button) {
    this.button = button;
  }

  public Button getButton() {
    return button;
  }
}
