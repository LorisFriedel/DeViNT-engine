package polytech.devint.view;


import java.awt.Color;

import polytech.devint.util.Identifiable;


/**
 * Represents a palette of colours used in a devint window for its components
 *
 * @author Antoine Aubé (aube.antoine@gmail.com)
 * @author Loris Friedel
 */
public class Palette extends Identifiable<String> {

    private final Color backgroundColor1;
    private final Color foregroundColor1;
    private final Color backgroundColor2;
    private final Color foregroundColor2;
    private final Color backgroundColor3;
    private final Color foregroundColor3;

    /**
     * Instantiates a palette with all its mandatory colours
     *
     * @param id               name (identifier) of the palette (used to get it from the map)
     * @param backgroundColor1 global background color of all non-button elements (also the
     *                         background color of a clicked button)
     * @param foregroundColor1 global foreground (font) color of all non-button elements
     * @param backgroundColor2 background color of a button when it is selected (but not
     *                         clicked yet)
     * @param foregroundColor2 foreground (font) color of a button when it is selected
     *                         (and/or clicked)
     * @param backgroundColor3 background color of a button when it is unselected (and
     *                         not clicked)
     * @param foregroundColor3 foreground (font) color of a button when it is
     *                         unselected (and not clicked)
     */
    public Palette(String id, Color backgroundColor1, Color foregroundColor1,
                   Color backgroundColor2, Color foregroundColor2,
                   Color backgroundColor3, Color foregroundColor3) {
        super(id);
        this.backgroundColor1 = backgroundColor1;
        this.foregroundColor1 = foregroundColor1;
        this.backgroundColor2 = backgroundColor2;
        this.foregroundColor2 = foregroundColor2;
        this.backgroundColor3 = backgroundColor3;
        this.foregroundColor3 = foregroundColor3;
    }

    /**
     * @return the global background color of all non-button elements (also the background color of a
     * clicked button)
     */
    public Color getBackgroundColor1() {
        return backgroundColor1;
    }

    /**
     * @return the global foreground (font) color of all non-button elements
     */
    public Color getForegroundColor1() {
        return foregroundColor1;
    }

    /**
     * @return the background color of a button when it is selected (but not clicked yet)
     */
    public Color getBackgroundColor2() {
        return backgroundColor2;
    }

    /**
     * @return the foreground (font) color of a button when it is selected (and/or clicked)
     */
    public Color getForegroundColor2() {
        return foregroundColor2;
    }

    /**
     * @return the background color of a button when it is unselected (and not clicked)
     */
    public Color getBackgroundColor3() {
        return backgroundColor3;
    }

    /**
     * @return the foreground (font) color of a button when it is unselected (and not clicked)
     */
    public Color getForegroundColor3() {
        return foregroundColor3;
    }
}
