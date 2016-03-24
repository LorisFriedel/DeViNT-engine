package polytech.devint.view.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import polytech.devint.model.Model;
import polytech.devint.view.ContextHelp;
import polytech.devint.view.DevintView;
import polytech.devint.view.configuration.DisplayConfiguration;
import polytech.devint.view.configuration.Palette;

/**
 * Represents a Swing View
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class SwingView<M extends Model> extends DevintView<M> {

  // TODO make dimension modular
  public static final int DEFAULT_WIDTH = 800;
  public static final int DEFAULT_HEIGHT = 600;
  static final String DEFAULT_NAME = "DeViNT";

  public static JFrame defaultView;
  public SwingKeyDispatcher currentKeyDispatcher;

  protected JPanel currentPanel;
  protected final JFrame frame;

  /**
   * Instantiates a new swing view
   *
   * @param frame the frame containing the view
   */
  public SwingView(JFrame frame, ContextHelp contextHelp) {
    super(contextHelp);
    this.frame = frame;
  }

  /**
   * @return the current JFrame of the swing view.
   */
  public JFrame getFrame() {
    return frame;
  }

  private void initPanel() {
    this.currentPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        SwingView.this.paintComponent(g);
      }
    };

    currentPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        if (isActive()) {
          update();
        }
      }
    });
  }

  /**
   * Instantiates a new view with the default frame and the given context help
   *
   * @param contextHelp help of this view
   */
  public SwingView(ContextHelp contextHelp) {
    this(getDefaultFrame(), contextHelp);
  }

  /**
   * Instantiates a default new view with the default frame and default context help (empty)
   */
  SwingView() {
    this(getDefaultFrame(), new ContextHelp());
  }

  /**
   * Called when the main panel is being paint
   *
   * @param g
   */
  protected void paintComponent(Graphics g) {
    // Nothing more to draw
  }

  /**
   * Initiates a default swing view
   */
  static JFrame getDefaultFrame() {
    if (defaultView != null) {
      return defaultView;
    }

    defaultView = new JFrame();
    defaultView.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    defaultView.setTitle(DEFAULT_NAME);
    defaultView.setLocationRelativeTo(null);
    defaultView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    defaultView.setExtendedState(defaultView.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    return defaultView;
  }


  @Override
  public void setupContent() {
    frame.setVisible(true);
    initPanel();
    frame.setContentPane(currentPanel);
    // Using AWT to register our keyboard inputs
    currentKeyDispatcher = new SwingKeyDispatcher<>(controller);
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(currentKeyDispatcher);
    initView();
    frame.revalidate();
    frame.repaint();
    update();
  }

  /**
   * Resets the view, Calls destroyContent() and initContent()
   */
  public void reset() {
    destroyContent();
    setupContent();
  }

  @Override
  public void destroyContent() {
    currentPanel.removeAll();
    frame.setContentPane(new JPanel());
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
    .removeKeyEventDispatcher(currentKeyDispatcher);
    controller.resetKeys();
    destroyView();
  }

  /**
   * Called when a swing view is being initialized
   */
  public abstract void initView();

  /**
   * Called when a swing view is being destroyed
   */
  public abstract void destroyView();

  /**
   * Called on each view update
   */
  @Override
  public void update() {
    Palette palette = DisplayConfiguration.getDefaultDisplay().getCurrentPalette();
    currentPanel.setBackground(palette.getBackgroundColor1());
    frame.repaint();
  }

  /**
   * Get from the current display configuration the new font
   * that corresponds to the one we uses, but updated according
   * to the current size of font defined by the user.
   *
   * @param font font we are currently using
   * @return the font updated according to the current font size of the global display configuration
   */
  protected Font getUpdatedFont(Font font) {
    return font.deriveFont(
            (float) DisplayConfiguration.getDefaultDisplay().getCurrentFont().getSize());
  }

  ///////////////////////////////////
  ////// Generic update method //////
  ///////////////////////////////////

  /**
   * Update the font and the foreground color of the given label
   *
   * @param label label to update
   */
  protected void updateLabel(JLabel label) {
    updateLabelForeground(label);
    updateLabelFont(label);
  }

  /**
   * Update the foreground color of the given label
   * according to the current display configuration.
   *
   * @param label label to update its foreground color
   */
  protected void updateLabelForeground(JLabel label) {
    // Text (foreground) color update
    label.setForeground(
            DisplayConfiguration.getDefaultDisplay().getCurrentPalette().getForegroundColor1());
  }

  /**
   * Update the font of the given label according
   * to the current display configuration.
   *
   * @param label label to update its font
   */
  protected void updateLabelFont(JLabel label) {
    // Font update
    label.setFont(
            getUpdatedFont(label.getFont()));
  }

  /**
   * Update the background color of the given panel
   *
   * @param panel panel to update its background color
   */
  protected void updatePanelColor(JPanel panel) {
    // Background color update
    panel.setBackground(
            DisplayConfiguration.getDefaultDisplay().getCurrentPalette().getBackgroundColor1());
  }
}
