package polytech.devint.controller;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.event.EventHandler;
import polytech.devint.event.basic.F1Event;
import polytech.devint.event.basic.F2Event;
import polytech.devint.event.basic.F3Event;
import polytech.devint.event.basic.F4Event;
import polytech.devint.model.Model;
import polytech.devint.util.time.Timeout;
import polytech.devint.view.DevintView;
import polytech.devint.view.configuration.DisplayConfiguration;

/**
 * @param <M> Model that the controller can manage
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

  @EventHandler
  public final void askForHelp(F1Event event) {
    getViews().forEach(DevintView::playHelp);
  }

  @EventHandler
  public final void askForDetailedHelp(F2Event event) {
    getViews().forEach(DevintView::playDetailedHelp);
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

  // TODO onEscape -> back to the main menu
}
