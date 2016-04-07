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
 * Used to register observers and notify them, using an event system.
 *
 * @author Loris Friedel
 * @author Günther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class Observable {

  final Map<Object, Map<Class<? extends Event>, List<Method>>> observers;

  private static final Logger LOGGER = LogManager.getLogger(Observable.class);

  /**
   * Construct an Observable with zero observers.
   */
  public Observable() {
    this.observers = new ConcurrentHashMap<>();
  }

  /**
   * Adds an observer to the set of observers for this object,
   * provided that it is not the same as some observer already in the set.
   *
   * @param observer An observer to be added.
   */
  @SuppressWarnings("unchecked")
  public void addObserver(Object observer) {
    Map<Class<? extends Event>, List<Method>> methods = new HashMap<>();
    for (Method method : observer.getClass().getMethods()) {
      if (method.isAnnotationPresent(EventHandler.class)
              && method.getParameterCount() == 1
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
   * Notifies all the observers. This will call @EventManager methods (with corresponding types)
   * of all the observers
   *
   * @param event The event to be sent to the observers
   */
  public void notifyObservers(Event event) {
    observers.forEach((observer, map) -> {
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
   * Check if the given object is currently an observer of the object that contains this manager.
   *
   * @param observer The object to test if it is registered as an observer in this manager.
   * @return True if the observer has been registered
   */
  public boolean hasObserver(Object observer) {
    return observers.containsKey(observer);
  }

  /**
   * Deletes an observer from the set of observers of this object.
   *
   * @param observer Observer to be deleted
   */
  public void deleteObserver(Object observer) {
    this.observers.remove(observer);
  }

  /**
   * Clears the observer list so that this object no longer has any observers.
   */
  public void deleteObservers() {
    this.observers.clear();
  }

  /**
   * @return The number of observers of this Observable object.
   */
  public int countObservers() {
    return observers.size();
  }

  /**
   * Remove all link between this on the given observable.
   * The two object will no longer be observer of each other.
   *
   * @param o1 Observable to remove its link with the current object.
   */
  public void unlink(Observable o1) {
    if(o1 == null) {
      return;
    }
    o1.deleteObserver(this);
    this.deleteObserver(o1);
  }
}
