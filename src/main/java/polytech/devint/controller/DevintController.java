package polytech.devint.controller;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.event.EventHandler;
import polytech.devint.event.basic.F1Event;
import polytech.devint.event.basic.F2Event;
import polytech.devint.event.basic.F3Event;
import polytech.devint.event.basic.F4Event;
import polytech.devint.event.entity.ButtonClickEvent;
import polytech.devint.model.Model;
import polytech.devint.util.time.Timeout;
import polytech.devint.view.DevintView;
import polytech.devint.view.configuration.DisplayConfiguration;

/**
 * @param <M>
 * @author Loris Friedel
 */
public abstract class DevintController<M extends Model, V extends DevintView<M>>
        extends Controller<M, V> {

  public DevintController(M model, InputConfiguration inputConfiguration) {
    super(model, inputConfiguration);
  }

  public DevintController(M model) {
    super(model);
  }

  /**
   * Executed when a button is being pressed
   *
   * @param event The corresponding event
   */
  @EventHandler
  public final void onButtonClick(ButtonClickEvent event) {
    event.getEntity().update();
  }

  @EventHandler
  public final void askForHelp(F1Event event) {
    forEachViews(DevintView::playHelp);
  }

  @EventHandler
  public final void askForDetailedHelp(F2Event event) {
    forEachViews(DevintView::playDetailedHelp);
  }

  @EventHandler
  public final void onPaletteChange(F3Event event) {
    DisplayConfiguration.getDefaultDisplay().nextPalette();
    updateViews();
  }

  @EventHandler
  public final void onFontChange(F4Event event) {
    DisplayConfiguration.getDefaultDisplay().nextFont();
    updateViews();
  }
}
