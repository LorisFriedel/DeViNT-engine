package polytech.devint.view.swing.menu;

import polytech.devint.model.menu.SwingMenuModel;
import polytech.devint.view.ContextHelp;
import polytech.devint.view.configuration.DisplayConfiguration;
import polytech.devint.view.configuration.Palette;
import polytech.devint.view.swing.SwingView;
import polytech.devint.view.swing.menu.event.ButtonTriggeredEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

/**
 * A swing view based on a menu (button list)
 * Before being initialized, this view MUST HAVE A MENU with at least one button.
 *
 * @author Friedel Loris
 */
public class SwingMenuView<M extends SwingMenuModel> extends SwingView<M> {

  protected final String menuName;

  protected final Map<Button, JButton> registeredButtons;
  protected final List<Button> currentButtonList;

  protected final LayoutManager layoutManager;
  protected final GridBagConstraints menuConstraints;
  protected final GridBagConstraints buttonConstraints;

  protected Button currentSelectedButton;
  protected JPanel headerPanel;
  protected JLabel labelMenuName;
  protected JPanel buttonPanel;
  protected JPanel buttonSubPanel;

  public enum Direction {
    UP, DOWN
  }

  /**
   * Instantiates a new swing menu view
   * Before being initialized, this view MUST HAVE A MENU with at least one button.
   *
   * @param menuName    menuName of the menu
   * @param contextHelp help of this view context
   */
  public SwingMenuView(String menuName, ContextHelp contextHelp) {
    this(getDefaultFrame(), contextHelp, menuName);
  }

  // TODO when initialized, make the first button selected instead of null

  /**
   * Instantiates a menu under a jframe
   * Before being initialized, this view MUST HAVE A MENU with at least one button.
   *
   * @param frame       the Jframe
   * @param menuName    menuName of the menu
   * @param contextHelp help of this view context
   */
  public SwingMenuView(JFrame frame, ContextHelp contextHelp, String menuName) {
    super(frame, contextHelp);
    this.menuName = menuName;
    this.registeredButtons = new HashMap<>();
    this.currentButtonList = new LinkedList<>();
    this.layoutManager = generateLayoutManager();
    this.menuConstraints = generateMenuConstraints();
    this.buttonConstraints = generateButtonConstraints();
  }

  /**
   * Initialize and return the final instance of the layout manager associated to the menu
   *
   * @return the layout manager associated to the menu
   */
  private LayoutManager generateLayoutManager() {
    return new GridBagLayout();
  }

  /**
   * Initialize and return the final instance of the grid bag constraints associated to the buttons
   *
   * @return the grid bag constraints associated to the buttons
   */
  private GridBagConstraints generateButtonConstraints() {
    final GridBagConstraints result = new GridBagConstraints();
    result.fill = GridBagConstraints.BOTH;
    return result;
  }

  /**
   * Initialize and return the final instance of the grid bag constraints associated to the menu
   *
   * @return the grid bag constraints associated to the menu
   */
  private GridBagConstraints generateMenuConstraints() {
    final GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.anchor = GridBagConstraints.CENTER;
    constraints.weightx = 1;
    return constraints;
  }

  @Override
  public void initCustomContent() {
    assert !getLinkedModel().getButtons().isEmpty();

    // Draw the menu
    drawMenu();
    //changeButtonSelection(Direction.DOWN);
    update();
  }

  @Override
  public void destroyCustomContent() {
    // Remove all components
    getMainPanel().removeAll();
  }

  /**
   * Draws the menu
   */
  protected void drawMenu() { // TODO comment this method
    // We clear the lists/maps
    getMainPanel().removeAll();
    getMainPanel().setLayout(layoutManager);

    headerPanel = new JPanel();
    headerPanel.setLayout(new GridLayout());

    menuConstraints.gridy = 0;
    menuConstraints.weighty = 3;
    labelMenuName = generateLabelMenuName();
    headerPanel.add(labelMenuName);
    getMainPanel().add(headerPanel, menuConstraints);

    menuConstraints.gridy = 1;
    menuConstraints.weighty = 10;
    buttonPanel = generateButtonPanel();
    getMainPanel().add(buttonPanel, menuConstraints);

    menuConstraints.gridy = 2;
    menuConstraints.weighty = 2;
    getMainPanel().add(new JLabel(), menuConstraints);
  }

  /**
   * Generate a brand new labelMenuName, ready to be added to the menu The text is centered
   *
   * @return a brand new labelMenuName, ready to be added to the menus
   */
  protected JLabel generateLabelMenuName() {
    JLabel resultLabel = new JLabel(menuName);
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    updateLabelFont(resultLabel);
    return resultLabel;
  }

  /**
   * Generate the JPanel containing the buttons
   *
   * @return the JPanel containing the buttons
   */
  protected JPanel generateButtonPanel() { // TODO comment a little bit this method
    JPanel panel = new JPanel(new GridBagLayout());

    buttonSubPanel = new JPanel();
    buttonSubPanel.setBackground(
            DisplayConfiguration.getDefaultDisplay().getCurrentPalette().getBackgroundColor1());

    List<Button> buttonList = getController().getModel().getButtons();

    GridLayout gridLayout = new GridLayout(buttonList.size(), 1);
    gridLayout.setVgap(20);
    buttonSubPanel.setLayout(gridLayout);

    currentButtonList.clear();
    registeredButtons.clear();
    // Create and add all button in the list
    buttonList.forEach(button -> {
      JButton jbutton = new JButton(button.getName());
      buttonSubPanel.add(jbutton);
      initButton(button, jbutton);
      registeredButtons.put(button, jbutton);
      currentButtonList.add(button);
    });

    buttonConstraints.weightx = 1;
    buttonConstraints.weighty = 0;
    panel.add(new JLabel(), buttonConstraints);

    buttonConstraints.weightx = 10;
    buttonConstraints.weighty = 1;
    panel.add(buttonSubPanel, buttonConstraints);

    buttonConstraints.weightx = 1;
    buttonConstraints.weighty = 0;
    panel.add(new JLabel(), buttonConstraints);

    return panel;
  }

  /**
   * Initializes a new button
   *
   * @param button  The button
   * @param jbutton The corresponding jbutton
   */
  protected void initButton(Button button, JButton jbutton) {
    // Adding the action listener
    jbutton.addActionListener(e -> notifyController(new ButtonTriggeredEvent(button)));

    // Adding the mouse listener
    jbutton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        currentSelectedButton = button;
        updateButtons();
      }

      @Override
      public void mouseExited(java.awt.event.MouseEvent evt) {
        currentSelectedButton = null;
        updateButtons();
      }
    });
  }

  /**
   * Updates the button's colors
   */
  protected void updateButtons() {
    Palette palette = DisplayConfiguration.getDefaultDisplay().getCurrentPalette();

    // Update the background color of the panel containing all the buttons
    buttonPanel.setBackground(
            DisplayConfiguration.getDefaultDisplay().getCurrentPalette().getBackgroundColor1());

    // Update the background color of all components contained in the button panel
    // the button panel contains empty panels and a jpanel containing all the buttons
    for (Component component : buttonPanel.getComponents()) {
      component.setBackground(buttonPanel.getBackground());
    }

    // Update each button
    registeredButtons.entrySet().forEach(entry -> {
      updateColorButton(palette, entry.getKey(), entry.getValue());
      entry.getValue().setFont(getUpdatedFont(entry.getValue().getFont()));
      entry.getValue().setBorder(new LineBorder(palette.getForegroundColor2(), 12));
    });
  }

  /**
   * Updates a given button with a given palette of colours
   *
   * @param palette the palette of colours
   * @param button  the button to update
   * @param jbutton the corresponding jbutton
   */
  public void updateColorButton(Palette palette, Button button, JButton jbutton) {
    if (button.equals(currentSelectedButton)) {
      jbutton.setBackground(palette.getBackgroundColor2());
      jbutton.setForeground(palette.getForegroundColor2());
    } else {
      jbutton.setBackground(palette.getBackgroundColor3());
      jbutton.setForeground(palette.getForegroundColor3());
    }
  }

  /**
   * Updates buttons
   */
  @Override
  public void update() {
    super.update();
    updateButtons();
  }


  /// TODO Review this !!!
  /// TODO Review this !!!
  /// TODO Review this !!!
  public void changeButtonSelection(Direction direction) {
    Iterator<Button> buttons = currentButtonList.iterator();
    if (currentSelectedButton == null) {
      currentSelectedButton = buttons.next();
    } else {
      handleButtonChange(direction, buttons);
    }
    enunciateButton();
    update();
  }

  private void enunciateButton() {
    if(currentSelectedButton.getEnunciation().isPresent()) {
      soundPlayer().forcePlay(currentSelectedButton.getEnunciation().get());
    } else {
      // TODO voice synthesis
    }
  }

  /// TODO Review this !!!
  /// TODO Review this !!!
  /// TODO Review this !!!
  protected void handleButtonChange(Direction direction, Iterator<Button> buttons) {
    switch (direction) {
      case DOWN:
        while (buttons.hasNext()) {
          Button next = buttons.next();
          if (next == currentSelectedButton) {
            // On prend le suivant
            currentSelectedButton = buttons.hasNext() ? buttons.next() : currentButtonList.get(0);
            break;
          }
        }
        break;
      case UP:
        Button previous = buttons.next();
        while (buttons.hasNext()) {
          Button next = buttons.next();
          if (next == currentSelectedButton) {
            break;
          }
          previous = next;
        }
        currentSelectedButton = previous;
        break;
    }
  }

  public Button getCurrentSelectedButton() {
    return currentSelectedButton;
  }

  public JPanel getHeaderPanel() {
    return headerPanel;
  }
}
