package polytech.devint.view.swing;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import polytech.devint.controller.Controller;
import polytech.devint.model.Model;
import polytech.devint.view.View;

/**
 * A keyboard adapter to listen for keyboard inputs
 *
 * @param <M>
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class SwingKeyDispatcher<M extends Model, V extends View<M>>
        implements KeyEventDispatcher {

  private final Controller<M, V> controller;

  private static final Logger LOGGER = LogManager.getLogger(SwingKeyDispatcher.class);

  /**
   * Instantiates a new key dispatcher under a controller
   *
   * @param controller The controller
   */
  public SwingKeyDispatcher(Controller<M, V> controller) {
    this.controller = controller;
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent arg0) {
    // If a key has been pressed
    if (arg0.getID() == KeyEvent.KEY_PRESSED) {
      controller.pressKey(arg0.getKeyCode());
    } else if (arg0.getID() == KeyEvent.KEY_RELEASED) {
      controller.unpressKey(arg0.getKeyCode());
    }
    return false;
  }

}
