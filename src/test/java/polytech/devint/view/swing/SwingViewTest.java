package polytech.devint.view.swing;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import javax.swing.JFrame;

import org.junit.Test;

import polytech.devint.controller.Controller;
import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.model.Model;
import polytech.devint.view.ContextHelp;

/**
 * Testing swing views
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class SwingViewTest {

  /**
   * Testing if a view is properly being initialized
   */
  @Test
  public void testInitView() {
    Model m = new Model() {};
    Controller<Model, SwingTestView> c = new Controller<Model, SwingTestView>(m, mock(InputConfiguration.class)) {};
    SwingTestView v = spy(new SwingTestView(mock(JFrame.class)));
    c.addView(v);
    v.init();
    verify(v, times(1)).initCustomContent();
    // Checking if the JPanel has been properly added-
    assertTrue(v.currentPanel != null);
  }

  private class SwingTestView extends SwingView<Model> {

    public SwingTestView(JFrame mock) {
      super(mock, new ContextHelp());
    }

    @Override
    public void initCustomContent() {
      // TODO Auto-generated method stub

    }

    @Override
    public void destroyCustomContent() {
      // TODO Auto-generated method stub

    }


  }
}
