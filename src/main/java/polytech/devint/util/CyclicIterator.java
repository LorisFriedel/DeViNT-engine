package polytech.devint.util;

import java.util.Iterator;

/**
 * Used to loop over an array We go through the array from index 0 to the last one Then we restart
 * at index 0
 *
 * @author Loris Friedel
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public class CyclicIterator<T> implements Iterator<T> {

  private final T[] array;
  private int index;

  /**
   * Initiates a cyclic iterator from an array
   *
   * @param array data array on which the iterator loop
   * @throws IllegalArgumentException if the given data array is empty
   */
  public CyclicIterator(T[] array) {
    if (array.length <= 0) {
      throw new IllegalArgumentException("Unauthorized array length: " + array.length);
    }
    this.array = array;
    this.index = 0;
  }

  /**
   * Return always true : no cyclic iterator can be instantiated with an empty array So, there is
   * always a next element.
   *
   * @return always true
   */
  @Override
  public boolean hasNext() {
    return true;
  }

  /**
   * Return the next element in the data array We loop over it
   *
   * @return the next element to get from the data array
   */
  @Override
  public T next() { // NOSONAR
    T item = array[index++];

    if (index >= array.length) {
      reset();
    }

    return item;
  }

  /**
   * Reset the iterator to start from index 0 of the data array
   */
  public void reset() {
    index = 0;
  }

  /**
   * Get the size of the cyclicIterator (the number of element in the data array)
   *
   * @return the size of the cyclicIterator (the number of element in the data array)
   */
  public int size() {
    return array.length;
  }
}
