package polytech.devint.view.swing;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import polytech.devint.controller.Controller;
import polytech.devint.event.Event;
import polytech.devint.model.Model;
import polytech.devint.view.View;

/**
 * A keyboard adapter to listen for keyboard inputs
 *
 * @param <M>
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class SwingKeyDispatcher<M extends Model, V extends View<M>> implements KeyEventDispatcher {

  private final Controller<M, V> controller;

  /**
   * Instantiates a new key dispatcher under a controller
   *
   * @param controller The controller
   */
  public SwingKeyDispatcher(Controller<M, V> controller) {
    this.controller = controller;
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getID() == KeyEvent.KEY_PRESSED) {
      controller.pressKey(event.getKeyCode());
    } else if (event.getID() == KeyEvent.KEY_RELEASED) {
      controller.releaseKey(event.getKeyCode());
    }
    return false;
  }
}
