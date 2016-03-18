package polytech.devint.view;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import polytech.devint.view.configuration.DisplayConfiguration;
import polytech.devint.view.configuration.DisplayConfigurationLoader;
import polytech.devint.view.configuration.Font;
import polytech.devint.view.configuration.Palette;


/**
 * @author Antoine Aubé (aube.antoine@gmail.com)
 *
 */
public class DisplayConfigurationTest {

  private DisplayConfigurationLoader dcl;

  /**
   * Sets up a display configuration loader for tests
   */
  @Before
  public void before() {
    Palette p1 = mock(Palette.class);
    when(p1.getId()).thenReturn("a");
    Palette p2 = mock(Palette.class);
    when(p2.getId()).thenReturn("b");
    Palette p3 = mock(Palette.class);
    when(p3.getId()).thenReturn("c");


    Palette[] palettes = new Palette[] {p1, p2, p3};

    Font[] fonts =
        new Font[] {new Font("a", "ff1", 1), new Font("b", "ff2", 2), new Font("c", "ff3", 3)};

    dcl = mock(DisplayConfigurationLoader.class);
    when(dcl.getPalettes()).thenReturn(palettes);
    when(dcl.getFonts()).thenReturn(fonts);
  }

  /**
   * Verify if selectPalette() works well in normal case
   */
  @Test
  public void testSelectPalette() {
    DisplayConfiguration dc = new DisplayConfiguration(dcl);

    dc.selectPalette("c");

    assertEquals("c", dc.getCurrentPalette().getId());
  }

  /**
   * Test selectPalette() with an unknown ID
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSelectPaletteFail() {
    DisplayConfiguration dc = new DisplayConfiguration(dcl);

    dc.selectPalette("d");
  }

  /**
   * Verify if selectFontSize() works well in normal case
   */
  @Test
  public void testSelectFont() {
    DisplayConfiguration dc = new DisplayConfiguration(dcl);

    dc.selectFont("c");

    assertEquals("c", dc.getCurrentFont().getId());
  }

  /**
   * Test selectFontSize() with an unknown ID
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSelectFontFail() {
    DisplayConfiguration dc = new DisplayConfiguration(dcl);

    dc.selectFont("d");
  }
}
