package polytech.devint.util;


/**
 * Represent an identifiable object
 *
 * @param <T> type of the identifier
 * @author Loris Friedel
 */
public abstract class Identifiable<T> {

  private final T id;

  protected Identifiable(T id) {
    this.id = id;
  }

  public T getId() {
    return id;
  }
}
