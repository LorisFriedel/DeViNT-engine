package polytech.devint.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import polytech.devint.controller.Controller;
import polytech.devint.model.Model;

/**
 *
 * @author Antoine Aubé (aube.antoine@gmail.com)
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class ViewTest {

  /**
   * Verify that init() & destroy() works well
   */
  @Test
  public void testInitAndDestroy() {
    Model m = new Model() {};

    @SuppressWarnings("unchecked")
    Controller<Model, TestView> c = mock(Controller.class);
    when(c.getModel()).thenReturn(m);

    TestView v = spy(new TestView());

    assertFalse(v.isActive());
    v.setController(c);
    v.init();
    assertTrue(v.isActive());

    verify(v, times(1)).setupContent();

    v.destroy();

    verify(v, times(1)).destroyContent();
  }
}
