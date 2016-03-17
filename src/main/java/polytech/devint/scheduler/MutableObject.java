package polytech.devint.scheduler;

/**
 * Used to bypass the 'variable not initialized compiling error
 *
 * @param <T> the type of the object
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class MutableObject<T> {

  private T value;

  /**
   * Instantiates with a given object
   *
   * @param object
   */
  public MutableObject(T object) {
    this.setValue(object);
  }

  /**
   * Instantiates with a null object
   */
  public MutableObject() {
    this(null);
  }

  /**
   * Sets the object
   *
   * @param object
   */
  public void setValue(T object) {
    this.value = object;
  }

  /**
   * @return The object
   */
  public T getValue() {
    return value;
  }
}
