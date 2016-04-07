package polytech.devint.view.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import polytech.devint.model.Model;
import polytech.devint.view.ContextHelp;
import polytech.devint.view.DevintView;
import polytech.devint.view.configuration.DisplayConfiguration;

/**
 * Represents a Swing View.
 * This view contains a JFrame and a JPanel when initialized.
 * You must use the main JPanel
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 * @author Loris Friedel
 */
public abstract class SwingView<M extends Model> extends DevintView<M> {

  // TODO make dimension modular
  private static int defaultWidth = 800;
  private static int defaultHeight = 600;
  private static String defaultName = "DeViNT";
  private static JFrame defaultFrame;

  private final JFrame frame;
  private SwingKeyDispatcher<M> currentKeyDispatcher;
  private JPanel mainPanel;
  private boolean autoUpdate = true;

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
  public SwingView() {
    this(getDefaultFrame(), new ContextHelp());
  }


  /**
   * Change the default dimension of all swing windows.
   *
   * @param width  New default width of all frame.
   * @param height new default height of all frame.
   */
  public static void setDefaultDimension(int width, int height) {
    SwingView.defaultWidth = width;
    SwingView.defaultHeight = height;
  }

  /**
   * Change the default name of all swing windows.
   *
   * @param name New default name of all frame.
   */
  public static void setDefaultName(String name) {
    SwingView.defaultName = name;
  }

  /**
   * @return The current JFrame of the swing view.
   */
  protected JFrame getFrame() {
    return frame;
  }

  /**
   * @return The main panel that is inside the JFrame of this window.
   */
  protected JPanel getMainPanel() {
    return mainPanel;
  }

  private void initPanel() {
    this.mainPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        SwingView.this.paintComponent(g);
      }
    };

    mainPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateIfActive();
      }
    });
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
  protected static JFrame getDefaultFrame() {
    if (defaultFrame != null) {
      return defaultFrame;
    }

    defaultFrame = new JFrame();
    defaultFrame.setSize(defaultWidth, defaultHeight);
    defaultFrame.setTitle(defaultName);
    defaultFrame.setLocationRelativeTo(null);
    defaultFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    defaultFrame.setExtendedState(defaultFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    return defaultFrame;
  }


  @Override
  public void setupContent() {
    frame.setVisible(true);
    initPanel();
    frame.setContentPane(mainPanel);
    // Using AWT to register our keyboard inputs
    currentKeyDispatcher = new SwingKeyDispatcher<>(controller);
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(currentKeyDispatcher);
    initCustomContent();
    frame.revalidate();
    frame.repaint();
    update();
  }

  @Override
  public void destroyContent() {
    mainPanel.removeAll();
    frame.setContentPane(new JPanel());
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .removeKeyEventDispatcher(currentKeyDispatcher);
    destroyCustomContent();
  }

  /**
   * Called when a swing view is being initialized
   */
  public abstract void initCustomContent();

  /**
   * Called when a swing view is being destroyed
   */
  public abstract void destroyCustomContent();

  /**
   * Updates all components of this swing view and repaint the JFrame.
   */
  @Override
  public void update() {
    if(isAutoUpdateActivated()) {
      updateComponents();
    }
    frame.repaint();
  }

  /**
   * @return True is the auto update system is currently active, false otherwise.
   */
  public boolean isAutoUpdateActivated() {
    return autoUpdate;
  }

  /**
   * Activate the auto update system.
   * By default, it is activated.
   * This system automatically update JLabel and JPanel font and color according
   * to the current display configuration of the program.
   */
  public void activateAutoUpdate() {
    autoUpdate = true;
  }

  /**
   * Disable the auto update system.
   * JPanel and JLabel in this swing view will no longer be updated automatically.
   */
  public void disableAutoUpdate() {
    autoUpdate = false;
  }

  private void updateIfActive() {
    if(isActive()) {
      update();
    }
  }

  private void updateComponents() {
    updateComponent(getFrame().getContentPane());
  }

  private void updateComponent(Component component) {
    if(component instanceof JLabel) {
      updateLabel((JLabel) component);
    }
    else if(component instanceof JPanel) {
      JPanel panel = (JPanel) component;
      updatePanelColor(panel);
      for(Component c : panel.getComponents()) {
        updateComponent(c);
      }
    }
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
