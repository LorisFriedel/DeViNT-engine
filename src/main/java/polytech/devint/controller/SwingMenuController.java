package polytech.devint.controller;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.event.EventHandler;
import polytech.devint.event.basic.DownEvent;
import polytech.devint.event.basic.EnterEvent;
import polytech.devint.event.basic.UpEvent;
import polytech.devint.model.Model;
import polytech.devint.view.swing.SwingMenuView;

/**
 * @param <M>
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class SwingMenuController<M extends Model> extends DevintController<M, SwingMenuView<M>> {

  public SwingMenuController(M model, InputConfiguration inputConfiguration) {
    super(model, inputConfiguration);
  }

  public SwingMenuController(M model) {
    super(model);
  }

  @EventHandler
  public final void onUp(UpEvent event) {
    defaultKeyTimeout.tryTo(() -> views.forEach(v -> {
      if (v.isActive()) {
        v.changeButtonSelection(SwingMenuView.ButtonChangeDirection.UP);
      }
    }));
  }

  @EventHandler
  public final void onDown(DownEvent event) {
    defaultKeyTimeout.tryTo(() -> views.forEach(v -> {
      if (v.isActive()) {
        v.changeButtonSelection(SwingMenuView.ButtonChangeDirection.DOWN);
      }
    }));
  }

  @EventHandler
  public final void onEnter(EnterEvent event) {
    defaultKeyTimeout.tryTo(() -> views.forEach(v -> {
      if (v.isActive()) {
        v.pressButton();
      }
    }));

  }
}
