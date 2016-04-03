package polytech.devint.view.swing;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import polytech.devint.controller.Controller;
import polytech.devint.event.*;
import polytech.devint.event.Observable;
import polytech.devint.model.Model;
import polytech.devint.view.View;

/**
 * A keyboard adapter to listen for keyboard inputs
 *
 * @param <M> Type of the model that the controller (which uses this key dispatcher) is controlling.
 * @param <V> Type of the view that the controller (which uses this key dispatcher) is using.
 * @author Loris Friedel
 */
public class SwingKeyDispatcher<M extends Model, V extends View<M>> extends Observable implements KeyEventDispatcher {

  private static final Logger LOGGER = LogManager.getLogger(Controller.class);

  private final Controller<M, V> controller;
  private final Set<Integer> waitingRelease;

  // TODO make it specific to a SwingView or at least a DevintView ?
  // TODO make it specific to a KeyboardInputConfiguration

  public SwingKeyDispatcher(Controller<M, V> controller) {
    this.controller = controller;
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
            controller.getInputConfiguration().getKeyPressedEvents(keyCode);
    Optional<List<Class<? extends Event>>> pressOnceEvents =
            controller.getInputConfiguration().getKeyPressedOnceEvents(keyCode);

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
            controller.getInputConfiguration().getKeyReleasedEvents(keyCode);

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
