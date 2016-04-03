package polytech.devint.view.swing;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import polytech.devint.controller.Controller;
import polytech.devint.controller.DevintController;
import polytech.devint.controller.input.SwingInputConfiguration;
import polytech.devint.event.*;
import polytech.devint.event.Observable;
import polytech.devint.model.Model;

/**
 * A keyboard adapter to listen for keyboard inputs
 *
 * @param <M> Type of the model that the controller (which uses this key dispatcher) is controlling.
 * @author Loris Friedel
 */
public class SwingKeyDispatcher<M extends Model> extends Observable implements KeyEventDispatcher {

  private static final Logger LOGGER = LogManager.getLogger(Controller.class);

  private final SwingInputConfiguration configuration;
  private final Set<Integer> waitingRelease;

  public SwingKeyDispatcher(Controller<M, ?> controller) {
    this.configuration = controller.getInputConfiguration().getSwingConfig();
    this.waitingRelease = new HashSet<>();
    addObserver(controller);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getID() == KeyEvent.KEY_PRESSED) {
      pressKey(event.getKeyCode());
    } else if (event.getID() == KeyEvent.KEY_RELEASED) {
      releaseKey(event.getKeyCode());
    }
    return false;
  }

  private void pressKey(Integer keyCode) {
    List<Class<? extends Event>> events = new ArrayList<>();

    Optional<List<Class<? extends Event>>> pressEvents =
            configuration.getKeyPressedEvents(keyCode);
    Optional<List<Class<? extends Event>>> pressOnceEvents =
            configuration.getKeyPressedOnceEvents(keyCode);

    if (!waitingRelease.contains(keyCode) && pressOnceEvents.isPresent()) {
      waitingRelease.add(keyCode);
      events.addAll(pressOnceEvents.get());
    }

    if (pressEvents.isPresent()) {
      events.addAll(pressEvents.get());
    }

    notifyObservers(events);
  }

  private void releaseKey(Integer keyCode) {
    waitingRelease.remove(keyCode);

    Optional<List<Class<? extends Event>>> releaseEvents =
            configuration.getKeyReleasedEvents(keyCode);

    if (releaseEvents.isPresent()) {
      notifyObservers(releaseEvents.get());
    }
  }

  private void notifyObservers(List<Class<? extends Event>> events) {
    events.forEach(event -> {
      try {
        notifyObservers(event.getConstructor().newInstance());
      } catch (Exception e) {
        LOGGER.error("Error while invoking " + event + ":", e);
      }
    });
  }
}
