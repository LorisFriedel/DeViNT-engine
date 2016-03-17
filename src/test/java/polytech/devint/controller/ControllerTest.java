package polytech.devint.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import polytech.devint.controller.input.InputConfiguration;
import polytech.devint.model.Model;
import polytech.devint.view.swing.SwingMenuView;

/**
 * @author Loris Friedel
 */
public class ControllerTest {

  class ControllerDefault extends Controller<Model, SwingMenuView<Model>> {
    public ControllerDefault(Model model, InputConfiguration inputConfiguration) {
      super(model, inputConfiguration);
    }
  }

  @Test
  public void basicConstruction() {
    Model model = mock(Model.class);
    InputConfiguration config = mock(InputConfiguration.class);
    // Create a controller that contains an empty model and an empty config
    ControllerDefault controller = new ControllerDefault(model, config);

    // The controller need to at least return the empty model and config,
    // but it need to instantiate a new event manager that will manage the event between it and the
    // views it contains
    assertEquals(model, controller.getModel());
    assertEquals(config, controller.inputConfiguration);
    assertNotNull(controller.getEventManager());
  }

  @Test
  public void addAndRemoveView() {
    Model model = mock(Model.class);
    InputConfiguration config = mock(InputConfiguration.class);
    // Create a controller that contains an empty model and an empty config
    ControllerDefault controller = new ControllerDefault(model, config);

    assertNotNull(controller.views);
    assertTrue(controller.views.isEmpty());

    @SuppressWarnings("unchecked")
    SwingMenuView<Model> v = mock(SwingMenuView.class);
    controller.addView(v);

    assertEquals(1, controller.views.size());

    controller.removeView(v);
    assertTrue(controller.views.isEmpty());
  }
}
