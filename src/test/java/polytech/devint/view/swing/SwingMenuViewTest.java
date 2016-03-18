package polytech.devint.view.swing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.swing.JFrame;

import org.junit.Test;

import polytech.devint.controller.Controller;
import polytech.devint.entity.Button;
import polytech.devint.entity.Menu;
import polytech.devint.model.Model;
import polytech.devint.view.ContextHelp;
import polytech.devint.view.configuration.DisplayConfiguration;

/**
 * Testing a swing menu view
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class SwingMenuViewTest {

  /**
   * Testing if a menu view is properly adding buttons
   */
  @Test
  public void testButtons() {
    Model m = new Model() {};
    Controller<Model, SwingMenuView<Model>> c = new Controller<Model, SwingMenuView<Model>>(m) {};
    SwingMenuView<Model> v =
        spy(new SwingMenuView<>(mock(JFrame.class), mock(ContextHelp.class), "toto"));
    c.addView(v);
    Menu menu = new Menu(m);
    Button button = menu.addButton("test 1", () -> System.out.println("toto"));
    v.registerMenu(menu);
    v.init();
    verify(v, times(2)).updateColorButton(
        DisplayConfiguration.getDefaultDisplay().getCurrentPalette(), button,
        v.registeredButtons.get(button));
    assertEquals(3, v.currentPanel.getComponentCount());
    v.destroy();
    assertEquals(0, v.currentPanel.getComponentCount());
  }

  /**
   * Testing if a menu view is properly updating buttons
   */
  @Test
  public void testUpdateButtons() {
    Model m = new Model() {};
    Controller<Model, SwingMenuView<Model>> c = new Controller<Model, SwingMenuView<Model>>(m) {};
    SwingMenuView<Model> v =
        spy(new SwingMenuView<>(mock(JFrame.class), mock(ContextHelp.class), "toto"));
    c.addView(v);
    Menu menu = new Menu(m);
    Button button = menu.addButton("test 1", () -> System.out.println("toto"));
    v.registerMenu(menu);
    v.init();
    v.update();
    verify(v, times(3)).updateColorButton(
        DisplayConfiguration.getDefaultDisplay().getCurrentPalette(), button,
        v.registeredButtons.get(button));
  }

}
