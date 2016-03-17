package polytech.devint.view;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import polytech.devint.configuration.ConfigurationLoader;

/**
 * @author Antoine Aubé (aube.antoine@gmail.com)
 * @author Loris Friedel
 */
public class DisplayConfigurationLoader extends ConfigurationLoader {

  private Palette[] palettes;
  private Font[] fonts;

  /**
   * Initiate one display configuration loader from a configuration file
   *
   * @param configurationFile the configuration file
   * @throws IOException
   * @throws JSONException
   */
  public DisplayConfigurationLoader(InputStream configurationFile) throws IOException {
    super(configurationFile);

    JSONObject content = getContentAsJSONObject();

    setPalettes(content.getJSONArray("palettes"));
    setFonts(content.getJSONArray("fonts"));
  }


  /**
   * Set up the palettes array field
   *
   * @param JSONPalettes json containing all palette's data
   */
  private void setPalettes(JSONArray JSONPalettes) {
    palettes = new Palette[JSONPalettes.length()];

    for (int i = 0; i < palettes.length; i++) {
      JSONObject JSONItem = JSONPalettes.getJSONObject(i);

      palettes[i] =
              new Palette(JSONItem.getString("id"), extractColor(JSONItem.getJSONObject("background")),
                      extractColor(JSONItem.getJSONObject("foreground")),
                      extractColor(JSONItem.getJSONObject("button_background_selected")),
                      extractColor(JSONItem.getJSONObject("button_foreground_selected")),
                      extractColor(JSONItem.getJSONObject("button_background_unselected")),
                      extractColor(JSONItem.getJSONObject("button_foreground_unselected")));
    }
  }

  /**
   * Extract a color from a JSON object
   *
   * @param JSONColor json containing integer value of the color
   * @return the color described in the JSON object
   */
  private static Color extractColor(JSONObject JSONColor) {
    int red = JSONColor.getInt("red");
    int green = JSONColor.getInt("green");
    int blue = JSONColor.getInt("blue");

    return new Color(red, green, blue);
  }

  /**
   * Set up the font array
   *
   * @param JSONFonts json containing the data of the fonts
   */
  private void setFonts(JSONArray JSONFonts) {
    List<Font> list = new ArrayList<>();

    for (int i = 0; i < JSONFonts.length(); i++) {
      JSONObject JSONItem = JSONFonts.getJSONObject(i);

      String fontFamily = JSONItem.getString("font_family");

      JSONArray possibleSizes = JSONItem.getJSONArray("possible_sizes");

      for (int j = 0; j < possibleSizes.length(); j++) {
        int size = possibleSizes.getInt(j);
        String id = fontFamily + size;

        list.add(new Font(id, fontFamily, size));
      }
    }

    fonts = list.toArray(new Font[list.size()]);
  }

  /**
   * @return the palettes
   */
  public Palette[] getPalettes() {
    return palettes;
  }

  /**
   * @return the fonts
   */
  public Font[] getFonts() {
    return fonts;
  }
}
