package polytech.devint.view;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;
import polytech.devint.view.configuration.DisplayConfigurationLoader;
import polytech.devint.view.configuration.Font;
import polytech.devint.view.configuration.Palette;


/**
 * @author Antoine Aubé (aube.antoine@gmail.com)
 *
 */
public class DisplayConfigurationLoaderTest {

  /**
   * Verify that fields have been correctly loaded
   * 
   * @throws JSONException
   * @throws IOException
   */
  @Test
  public void correctLoadedConfig() throws JSONException, IOException {
    File file = new File("src/main/resources/polytech/devint/defaultDisplay.conf");
    FileInputStream fis = new FileInputStream(file);
    DisplayConfigurationLoader dcl = new DisplayConfigurationLoader(fis);

    assertEquals(fis, dcl.getConfigurationFile());

    Palette[] palettes = dcl.getPalettes();
    assertEquals(new Color(221, 138, 68), palettes[0].getBackgroundColor1());
    assertEquals(Color.BLACK, palettes[0].getForegroundColor1());
    assertEquals(new Color(130, 26, 26), palettes[0].getBackgroundColor2());
    assertEquals(Color.WHITE, palettes[0].getForegroundColor2());
    assertEquals(Color.BLACK, palettes[0].getBackgroundColor3());
    assertEquals(Color.WHITE, palettes[0].getForegroundColor3());

    Font[] fonts = dcl.getFonts();
    assertEquals("Arial", fonts[0].getFont().getName());
    assertEquals(40, fonts[0].getFont().getSize());
  }
}
