package polytech.devint.controller;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.controller.input.SwingInputConfiguration;
import polytech.devint.event.EventHandler;
import polytech.devint.event.basic.*;
import polytech.devint.model.Model;
import polytech.devint.view.DevintView;
import polytech.devint.view.View;
import polytech.devint.view.configuration.DisplayConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific controller for DeViNT developers.
 * All mandatory functionality are present.
 * This controller need to have registered at least one main view (using the <code>registerMainView(View v)</code> method.
 * The palette and font change need to be implemented in every view to work.
 *
 * A default behavior of the escape key is present. It may be overridden (override onEscape method).
 *
 * @param <M> Model that the controller can manage.
 * @author Loris Friedel
 */
public abstract class DevintController<M extends Model, V extends DevintView<M>>
        extends Controller<M, V> {

  private static List<View<?>> mainViews = new ArrayList<>();
  public static final String SWING_CONFIG_KEY = "SWING_DEFAULT";

  public DevintController(M model, InputConfiguration inputConfiguration) {
    super(model, inputConfiguration);
  }

  /**
   * Create a controller from a default input config
   *
   * @param model Model that the controller will control
   */
  public DevintController(M model) {
    this(model, new InputConfiguration().addConfig(SWING_CONFIG_KEY, new SwingInputConfiguration()));
  }

  @EventHandler
  public final void askForHelp(final F1Event event) {
    getViews().forEach(DevintView::playHelp);
  }

  @EventHandler
  public final void askForDetailedHelp(final F2Event event) {
    getViews().forEach(DevintView::playDetailedHelp);
  }

  @EventHandler
  public final void onPaletteChange(final F3Event event) {
    DisplayConfiguration.getDefaultDisplay().nextPalette();
    updateViews();
  }

  @EventHandler
  public final void onFontChange(final F4Event event) {
    DisplayConfiguration.getDefaultDisplay().nextFont();
    updateViews();
  }


  /**
   * Method called when the escape key is pressed.
   * The default behavior of this method is to destroy the current views
   * and initialize all registered main views.
   *
   * @param escapeEvent Event triggered when the escape key is being pressed.
   */
  @EventHandler
  public void onEscape(final EscapeEvent escapeEvent) {
    if (!mainViews.isEmpty()) {
      destroyViews();
      mainViews.forEach(View::init);
    }
  }

  /**
   * Add a main view in the devint controller class.
   * This view will be called when the space key will be pressed.
   *
   * @param view View to be added to the list of main views.
   */
  public static void registerMainView(View<?> view) {
    DevintController.mainViews.add(view);
  }

  /**
   * Remove a main view in the devint controller class.
   *
   * @param view View to be removed to the list of main views.
   */
  public static void unregisterMainView(View<?> view) {
    DevintController.mainViews.remove(view);
  }
}
