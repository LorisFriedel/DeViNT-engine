package polytech.devint.model.menu;

import polytech.devint.event.Event;
import polytech.devint.view.swing.menu.Button;

public class ButtonTriggeredEvent implements Event {

  final Button button;

  public ButtonTriggeredEvent(Button button) {
    this.button = button;
  }

  public Button getButton() {
    return button;
  }
}
