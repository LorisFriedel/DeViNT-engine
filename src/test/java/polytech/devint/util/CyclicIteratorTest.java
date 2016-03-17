package polytech.devint.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.omg.CORBA.Object;


/**
 * @author Antoine Aubé (aube.antoine@gmail.com)
 * @author Loris Friedel
 *
 */
public class CyclicIteratorTest {

  /**
   * Verify that next() works well
   */
  @Test
  public void nextTest() {
    CyclicIterator<Integer> iterator = new CyclicIterator<>(new Integer[] {1, 2, 3});

    assertTrue(iterator.hasNext());

    assertEquals(1, iterator.next().intValue());
    assertEquals(2, iterator.next().intValue());
    assertEquals(3, iterator.next().intValue());
    assertEquals(1, iterator.next().intValue());
  }

  /**
   * Verify that size() works well
   */
  @Test
  public void sizeTest() {
    assertEquals(1, new CyclicIterator<>(new Integer[] {1}).size());
    assertEquals(4, new CyclicIterator<>(new Integer[] {1, 2, 3, 4}).size());
    assertEquals(2, new CyclicIterator<>(new Integer[] {1, 2}).size());
    assertEquals(11, new CyclicIterator<>(new Integer[] {1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4}).size());
  }

  /**
   * Verify that reset() works well
   */
  @Test
  public void resetTest() {
    CyclicIterator<Integer> iterator = new CyclicIterator<>(new Integer[] {1, 2, 3, 4, 5, 6, 100});

    assertEquals(1, iterator.next().intValue());
    assertEquals(2, iterator.next().intValue());
    iterator.reset();
    assertEquals(1, iterator.next().intValue());
    assertEquals(2, iterator.next().intValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyArrayIteratorTest() {
    @SuppressWarnings("unused")
    CyclicIterator<Object> iterator = new CyclicIterator<>(new Object[] {});
  }
}
