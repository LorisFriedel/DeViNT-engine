package polytech.devint.view.configuration;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import polytech.devint.util.CyclicIterator;

/**
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public class DisplayConfiguration {

  private Palette currentPalette;
  private Font currentFont;
  private final CyclicIterator<Palette> palettes;
  private final CyclicIterator<Font> fonts;

  private static DisplayConfiguration currentDisplay;

  private static final Logger LOGGER = LogManager.getLogger(DisplayConfiguration.class);

  static {
    try {
      currentDisplay = new DisplayConfiguration(new DisplayConfigurationLoader(
              ClassLoader.getSystemResourceAsStream("polytech/devint/defaultDisplay.conf")));
    } catch (JSONException | IOException e) {
      LOGGER.error("Couldn't read the display configuration resource", e);
    }
  }

  /**
   * Initiate a display configuration from a loader
   *
   * @param loader The loader
   */
  public DisplayConfiguration(DisplayConfigurationLoader loader) {
    palettes = new CyclicIterator<>(loader.getPalettes());
    fonts = new CyclicIterator<>(loader.getFonts());

    nextPalette();
    nextFont();
  }

  /**
   * Select the palette with given ID
   *
   * @param id the id of the palette we want
   */
  public void selectPalette(String id) {
    String endOfCycle = currentPalette.getId();
    do {
      nextPalette();
    } while (!currentPalette.getId().equals(endOfCycle)
            && !currentPalette.getId().equals(id));

    if (!currentPalette.getId().equals(id)) {
      throw new IllegalArgumentException("Palette with ID " + id + " not found.");
    }
  }

  /**
   * Select the font with given ID
   *
   * @param id the id of the font we want
   */
  public void selectFont(String id) {
    String endOfCycle = currentFont.getId();
    do {
      nextFont();
    } while (!currentFont.getId().equals(endOfCycle)
            && !currentFont.getId().equals(id));

    if (!currentFont.getId().equals(id)) {
      throw new IllegalArgumentException("Font with ID " + id + " not found.");
    }
  }

  /**
   * Set the current palette to the next one
   */
  public void nextPalette() {
    currentPalette = palettes.next();
  }

  /**
   * Set the current font to the next one
   */
  public void nextFont() {
    currentFont = fonts.next();
  }

  /**
   * @return the current palette
   */
  public Palette getCurrentPalette() {
    return currentPalette;
  }

  /**
   * @return the current font
   */
  public Font getCurrentFont() {
    return currentFont;
  }

  /**
   * @return The current display configuration
   */
  public static DisplayConfiguration getDefaultDisplay() {
    return currentDisplay;
  }

}
