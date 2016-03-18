package polytech.devint.view.configuration;


import polytech.devint.util.Identifiable;

/**
 * Represents a font
 *
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public class Font extends Identifiable<String> {

  private final String fontFamily;
  private final int size;

  private static final int STYLE = java.awt.Font.BOLD;

  /**
   * Initiates a DeViNTFont from specified id and font
   *
   * @param id         name of the font
   * @param fontFamily style of the font
   * @param size       the point size of the font
   */
  public Font(String id, String fontFamily, int size) {
    super(id);

    this.fontFamily = fontFamily;
    this.size = size;
  }

  /**
   * @return the awt font object
   */
  public java.awt.Font getFont() {
    return new java.awt.Font(fontFamily, STYLE, size);
  }

  /**
   * @return the point size of the font
   */
  public int getSize() {
    return size;
  }
}
