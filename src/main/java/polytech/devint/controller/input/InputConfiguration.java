package polytech.devint.controller.input;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import polytech.devint.event.Event;
import polytech.devint.event.basic.DownEvent;
import polytech.devint.event.basic.EnterEvent;
import polytech.devint.event.basic.EscapeEvent;
import polytech.devint.event.basic.F1Event;
import polytech.devint.event.basic.F2Event;
import polytech.devint.event.basic.F3Event;
import polytech.devint.event.basic.F4Event;
import polytech.devint.event.basic.F5Event;
import polytech.devint.event.basic.F6Event;
import polytech.devint.event.basic.LeftEvent;
import polytech.devint.event.basic.RightEvent;
import polytech.devint.event.basic.SpaceEvent;
import polytech.devint.event.basic.UpEvent;

/**
 * Represent a configuration of inputs in the current environment.
 * It define the key binding.
 *
 * @author Loris Friedel
 */
public class InputConfiguration {

  final Map<Integer, List<Class<? extends Event>>> keyPressedControls;
  final Map<Integer, List<Class<? extends Event>>> keyPressedOnceControls;
  final Map<Integer, List<Class<? extends Event>>> keyReleasedControls;

  /**
   * Create an empty input configuration
   */
  public InputConfiguration() {
    this.keyPressedControls = new HashMap<>();
    this.keyPressedOnceControls = new HashMap<>();
    this.keyReleasedControls = new HashMap<>();
    loadDefault();
  }

  /**
   * Load the default input configuration
   */
  private void loadDefault() {
    bindOnPressOnce(KeyEvent.VK_F1, F1Event.class);
    bindOnPressOnce(KeyEvent.VK_F2, F2Event.class);
    bindOnPressOnce(KeyEvent.VK_F3, F3Event.class);
    bindOnPressOnce(KeyEvent.VK_F4, F4Event.class);
    bindOnPressOnce(KeyEvent.VK_F5, F5Event.class);
    bindOnPressOnce(KeyEvent.VK_F6, F6Event.class);

    // EN
    bindOnPressOnce(KeyEvent.VK_SPACE, SpaceEvent.class);
    bindOnPressOnce(KeyEvent.VK_ESCAPE, EscapeEvent.class);
    bindOnPressOnce(KeyEvent.VK_ENTER, EnterEvent.class);
    bindOnPressOnce(KeyEvent.VK_UP, UpEvent.class);
    bindOnPressOnce(KeyEvent.VK_DOWN, DownEvent.class);
    bindOnPressOnce(KeyEvent.VK_LEFT, LeftEvent.class);
    bindOnPressOnce(KeyEvent.VK_RIGHT, RightEvent.class);
  }

  public void bindOnPress(Integer keyCode, Class<? extends Event> event) {
    bind(keyPressedControls, keyCode, event);
  }

  public void bindOnPressOnce(Integer keyCode, Class<? extends Event> event) {
    bind(keyPressedOnceControls, keyCode, event);
  }

  public void bindOnRelease(Integer keyCode, Class<? extends Event> event) {
    bind(keyReleasedControls, keyCode, event);
  }

  /**
   * Add an event linked to a key in the given map of controls.
   *
   * @param controls Binding map in which we need to register the given key binding.
   * @param keyCode  Key that will trigger the event
   * @param event    Event triggered
   */
  private void bind(Map<Integer, List<Class<? extends Event>>> controls, Integer keyCode, Class<? extends Event> event) {
    // The key is already in the map
    if (controls.containsKey(keyCode)) {
      controls.get(keyCode).add(event);
    }
    // The key is not in the map
    else {
      List<Class<? extends Event>> events = new ArrayList<>();
      events.add(event);
      controls.put(keyCode, events);
    }
  }

  public void unbindKeyPressed(Integer keyCode) {
    keyPressedControls.remove(keyCode);
  }

  public void unbindKeyPressed(Class<? extends Event> event) {
    unbind(keyPressedControls, event);
  }

  public void unbindKeyPressedOnce(Integer keyCode) {
    keyPressedOnceControls.remove(keyCode);
  }

  public void unbindKeyPressedOnce(Class<? extends Event> event) {
    unbind(keyPressedOnceControls, event);
  }

  public void unbindKeyReleased(Integer keyCode) {
    keyReleasedControls.remove(keyCode);
  }

  public void unbindKeyReleased(Class<? extends Event> event) {
    unbind(keyReleasedControls, event);
  }

  /**
   * Remove a key binding in all registered controls.
   * Warning : all linked event are removed.
   * After that, the given key code triggers no event.
   *
   * @param keyCode Key code of the key to remove its binding
   */
  public void unbind(Integer keyCode) {
    keyPressedControls.remove(keyCode);
    keyPressedOnceControls.remove(keyCode);
    keyReleasedControls.remove(keyCode);
  }

  /**
   * Remove all the binding for the given event i.e. this event couldn't be called anymore
   * in the all controls map.
   *
   * @param event    Event(s) that should now be bind anymore.
   */
  public void unbind(Class<? extends Event> event) {
    unbind(keyPressedControls, event);
    unbind(keyPressedOnceControls, event);
    unbind(keyReleasedControls, event);
  }

  /**
   * Remove all the binding for the given event i.e. this event couldn't be called anymore
   * in the given controls map.
   *
   * @param event    Event(s) that should now be bind anymore.
   * @param controls Binding map in which to remove the binding.
   */
  private void unbind(Map<Integer, List<Class<? extends Event>>> controls, Class<? extends Event> event) {
    Iterator<Map.Entry<Integer, List<Class<? extends Event>>>> iterator =
            controls.entrySet().iterator();

    // for all entries
    while (iterator.hasNext()) {
      List<Class<? extends Event>> events = iterator.next().getValue();

      // if the list of event contains the one we want to remove
      if (events.contains(event)) {
        events.remove(event);

        // if no event left in the list, remove the entry
        if (events.isEmpty()) {
          iterator.remove();
        }
      }
    }
  }

  public Optional<List<Class<? extends Event>>> getKeyPressedEvents(Integer keyCode) {
    return getEvents(keyPressedControls, keyCode);
  }

  public Optional<List<Class<? extends Event>>> getKeyPressedOnceEvents(Integer keyCode) {
    return getEvents(keyPressedOnceControls, keyCode);
  }

  public Optional<List<Class<? extends Event>>> getKeyReleasedEvents(Integer keyCode) {
    return getEvents(keyReleasedControls, keyCode);
  }

  /**
   * Retrieve the associated event from its key in the given controls map.
   *
   * @param controls Map of the binding in which we want to find the bind events.
   * @param keyCode  Key of the event (need to be registered in the input configuration file).
   * @return the event linked to the given key
   */
  private Optional<List<Class<? extends Event>>> getEvents(Map<Integer, List<Class<? extends Event>>> controls, Integer keyCode) {
    return Optional.ofNullable(controls.get(keyCode));
  }
}
