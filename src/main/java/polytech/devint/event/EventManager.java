package polytech.devint.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Used to register observers and notify them
 *
 * @author Günther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class EventManager {

  final Map<Object, Map<Class<? extends Event>, List<Method>>> observers;

  private static final Logger LOGGER = LogManager.getLogger(EventManager.class);

  /**
   * Instantiates a new event manager
   */
  public EventManager() {
    this.observers = new ConcurrentHashMap<>();
  }

  /**
   * Registers a new observer
   *
   * @param observer The observer object
   */
  @SuppressWarnings("unchecked")
  public void registerObserver(Object observer) {
    Map<Class<? extends Event>, List<Method>> methods = new HashMap<>();
    for (Method method : observer.getClass().getMethods()) {
      if (method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1
              && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
        // We need to be able to access the method
        method.setAccessible(true);
        Class<? extends Event> key = (Class<? extends Event>) method.getParameterTypes()[0];
        List<Method> methodList;
        if (methods.containsKey(key)) {
          methodList = methods.get(key);
        } else {
          methodList = new ArrayList<>();
          methods.put(key, methodList);
        }
        methodList.add(method);
      }
    }
    observers.put(observer, methods);
  }

  /**
   * Removes the observer from the event manager
   *
   * @param observer observer to remove
   */
  public void removeObserver(Object observer) {
    this.observers.remove(observer);
  }

  /**
   * Notifies all the observers. This will call @EventManager methods (with corresponding tyoes) of
   * all the observers
   *
   * @param event The event to send to the observers
   */
  public void notify(Event event) {
    observers.entrySet().forEach(entry -> {
      Object observer = entry.getKey();
      Map<Class<? extends Event>, List<Method>> map = entry.getValue();
      if (map.containsKey(event.getClass())) {
        map.get(event.getClass()).forEach(method -> {
          try {
            method.invoke(observer, event);
          } catch (Exception e) {
            LOGGER.error("Error while calling " + method.getName() + ":", e);
          }
        });
      }
    });
  }

  /**
   * Removes all observers
   */
  public void removeAllObservers() {
    this.observers.clear();
  }

  /**
   * @param observer The observer
   * @return True if the observer has been registered
   */
  public boolean hasObserver(Object observer) {
    return observers.containsKey(observer);
  }
}
