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
 * Represent a configuration of inputs in the current environment. It define the key binding.
 *
 * @author Loris Friedel
 */
public class InputConfiguration {

  final Map<Integer, List<Class<? extends Event>>> controls;

  /**
   * Create an empty input configuration
   */
  public InputConfiguration() {
    this.controls = new HashMap<>();
    loadDefault();
  }

  /**
   * Load the default input configuration
   */
  private void loadDefault() {
    addBinding(KeyEvent.VK_F1, F1Event.class);
    addBinding(KeyEvent.VK_F2, F2Event.class);
    addBinding(KeyEvent.VK_F3, F3Event.class);
    addBinding(KeyEvent.VK_F4, F4Event.class);
    addBinding(KeyEvent.VK_F5, F5Event.class);
    addBinding(KeyEvent.VK_F6, F6Event.class);

    // En
    addBinding(KeyEvent.VK_SPACE, SpaceEvent.class);
    addBinding(KeyEvent.VK_ESCAPE, EscapeEvent.class);
    addBinding(KeyEvent.VK_ENTER, EnterEvent.class);
    addBinding(KeyEvent.VK_UP, UpEvent.class);
    addBinding(KeyEvent.VK_DOWN, DownEvent.class);
    addBinding(KeyEvent.VK_LEFT, LeftEvent.class);
    addBinding(KeyEvent.VK_RIGHT, RightEvent.class);
  }

  /**
   * Add an event linked to a key in the map of controls
   *
   * @param key   key that will trigger the event
   * @param event event triggered
   */
  public void addBinding(Integer key, Class<? extends Event> event) {
    // The key is already in the map
    if (controls.containsKey(key)) {
      controls.get(key).add(event);
    }
    // The key is not in the map
    else {
      List<Class<? extends Event>> events = new ArrayList<>();
      events.add(event);
      controls.put(key, events);
    }
  }

  /**
   * Remove a key binding in the controls Warning : all the event linked to this key are removed
   * After that, the key triggers no event
   *
   * @param key key binding to remove
   */
  public void removeBind(int key) {
    controls.remove(key);
  }

  /**
   * Remove all the binding for the given event i.e. this event couldn't be called anymore in this
   * controls set
   *
   * @param event event(s) that should now be bind anymore
   */
  public void removeBind(Class<? extends Event> event) {
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


  /**
   * Retrieve the associated event from its key
   *
   * @param key key of the event (need to be registered in the input configuration file)
   * @return the event linked to the given key
   */
  public Optional<List<Class<? extends Event>>> getEvents(int key) {
    return Optional.ofNullable(controls.get(key));
  }
}
