package polytech.devint.controller.menu;

import polytech.devint.controller.DevintController;
import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.controller.input.SwingInputConfiguration;
import polytech.devint.event.EventHandler;
import polytech.devint.event.basic.DownEvent;
import polytech.devint.event.basic.EnterEvent;
import polytech.devint.event.basic.UpEvent;
import polytech.devint.model.menu.SwingMenuModel;
import polytech.devint.view.View;
import polytech.devint.view.swing.menu.Button;
import polytech.devint.view.swing.menu.SwingMenuView;
import polytech.devint.view.swing.menu.event.ButtonTriggeredEvent;

/**
 * @author Loris Friedel
 */
public class SwingMenuController<M extends SwingMenuModel> extends DevintController<M, SwingMenuView<M>> {

  public SwingMenuController(M model, InputConfiguration inputConfiguration) {
    super(model, inputConfiguration);
  }

  public SwingMenuController(M model) {
    super(model);
  }

  @EventHandler
  public final void onUp(UpEvent event) {
    getViews().stream()
            .filter(View::isActive)
            .forEach(v -> v.changeButtonSelection(SwingMenuView.Direction.UP));
  }

  @EventHandler
  public final void onDown(DownEvent event) {
    getViews().stream()
            .filter(View::isActive)
            .forEach(v -> v.changeButtonSelection(SwingMenuView.Direction.DOWN));
  }

  @EventHandler
  public final void onEnter(EnterEvent event) {
    getViews().stream()
            .filter(v -> v.isActive() && v.getCurrentSelectedButton() != null)
            .forEach(v -> triggerButton(v.getCurrentSelectedButton()));
  }

  @EventHandler
  public final void onButtonTrigger(ButtonTriggeredEvent event) {
    triggerButton(event.getButton());
  }

  private void triggerButton(Button button) {
    if (button!= null) {
      new Thread(button.getAction()).start();
    }
  }
}
