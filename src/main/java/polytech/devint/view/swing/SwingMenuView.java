package polytech.devint.view.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import polytech.devint.entity.Button;
import polytech.devint.entity.Menu;
import polytech.devint.event.entity.ButtonClickEvent;
import polytech.devint.model.Model;
import polytech.devint.view.ContextHelp;
import polytech.devint.view.configuration.DisplayConfiguration;
import polytech.devint.view.configuration.Palette;

/**
 * A display based on a menu (button list)
 * Before being initialized, this view MUST HAVE A MENU with at least one button.
 *
 * @param <M>
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class SwingMenuView<M extends Model> extends SwingView<M> {

  protected final LayoutManager layoutManager;
  protected final GridBagConstraints menuConstraints;
  protected final GridBagConstraints buttonConstraints;

  protected Menu<M> currentMenu;
  protected final Map<Button<M>, JButton> registeredButtons;
  protected final List<Button<M>> buttonList;
  protected Button<M> currentSelection;
  protected final String name;
  protected JLabel labelMenuName;
  protected JPanel buttonPanel;
  protected JPanel buttonSubPanel;


  /**
   * Instantiates a new swing menu view
   * Before being initialized, this view MUST HAVE A MENU with at least one button.
   *
   * @param name        name of the menu
   * @param contextHelp help of this view context
   */
  public SwingMenuView(String name, ContextHelp contextHelp) {
    this(getDefaultFrame(), contextHelp, name);
  }

  /**
   * Instantiates a menu under a jframe
   * Before being initialized, this view MUST HAVE A MENU with at least one button.
   *
   * @param frame       the jframe
   * @param name        name of the menu
   * @param contextHelp help of this view context
   */
  public SwingMenuView(JFrame frame, ContextHelp contextHelp, String name) {
    super(frame, contextHelp);
    this.registeredButtons = new HashMap<>();
    this.name = name;
    this.buttonList = new LinkedList<>();
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
    // Draw the menu
    currentSelection = null;
    drawMenu();
    update();
  }

  @Override
  public void destroyCustomContent() {
    // Remove all components
    currentPanel.removeAll();
  }

  /**
   * Register a new menu under the view
   *
   * @param menu the menu to register
   */
  public void registerMenu(Menu<M> menu) {
    currentMenu = menu;
  }

  /**
   * Draws the menu
   */
  protected void drawMenu() { // TODO comment this method
    // We clear the lists/maps
    currentPanel.removeAll();
    registeredButtons.clear();
    currentPanel.setLayout(layoutManager);

    // Adding the name?

    menuConstraints.gridy = 0;
    menuConstraints.weighty = 3;
    labelMenuName = generateLabelMenuName();
    currentPanel.add(labelMenuName, menuConstraints);

    menuConstraints.gridy = 1;
    menuConstraints.weighty = 10;
    buttonPanel = generateButtonPanel();
    currentPanel.add(buttonPanel, menuConstraints);

    menuConstraints.gridy = 2;
    menuConstraints.weighty = 2;
    currentPanel.add(new JLabel(), menuConstraints);
  }

  /**
   * Update the font of the current labelMenuName
   */
  protected void updateMenuName() {
    updateLabelFont(labelMenuName);
  }

  /**
   * Update the font of the given JLabel according to the current display configuration
   *
   * @param jLabel JLabel to update
   */
  protected void updateLabelFont(JLabel jLabel) {
    jLabel.setFont(getUpdatedFont(jLabel.getFont()));
    jLabel.setForeground(
            DisplayConfiguration.getDefaultDisplay().getCurrentPalette().getForegroundColor1());
  }

  /**
   * Generate a brand new labelMenuName, ready to be added to the menu The text is centered
   *
   * @return a brand new labelMenuName, ready to be added to the menus
   */
  protected JLabel generateLabelMenuName() {
    JLabel resultLabel = new JLabel(name);
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
    List<Button<M>> currentButtonList = currentMenu.getButtons();

    GridLayout gridLayout = new GridLayout(currentButtonList.size(), 1);
    gridLayout.setVgap(20);
    buttonSubPanel.setLayout(gridLayout);

    buttonList.clear();
    // Create and add all button in the list
    currentButtonList.forEach(button -> {
      JButton jbutton = new JButton(button.getName());
      buttonSubPanel.add(jbutton);
      initButton(button, jbutton);
      registeredButtons.put(button, jbutton);
      buttonList.add(button);
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
  protected void initButton(Button<M> button, JButton jbutton) {
    // Adding the action listener
    jbutton.addActionListener(e -> controller.getEventManager().notify(new ButtonClickEvent(button)));

    // Adding the mouse listener
    jbutton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        currentSelection = button;
        updateButtons();
      }

      @Override
      public void mouseExited(java.awt.event.MouseEvent evt) {
        currentSelection = null;
        updateButtons();
      }
    });
  }

  /**
   * Updates the color of the buttons
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
    if (button.equals(currentSelection)) {
      jbutton.setBackground(palette.getBackgroundColor2());
      jbutton.setForeground(palette.getForegroundColor2());
    } else {
      jbutton.setBackground(palette.getBackgroundColor3());
      jbutton.setForeground(palette.getForegroundColor3());
    }
  }

  /**
   * Updating buttons
   */
  @Override
  public void update() {
    super.update();
    updateButtons();
    updateMenuName();
    currentPanel.setBackground(
            DisplayConfiguration.getDefaultDisplay().getCurrentPalette().getBackgroundColor1());
  }

  public void changeButtonSelection(ButtonChangeDirection direction) {
    Iterator<Button<M>> buttons = buttonList.iterator();
    if (!buttons.hasNext()) {
      return;
    }
    if (currentSelection == null) {
      currentSelection = buttons.next();
      update();
      return;
    }
    handleButtonChange(direction, buttons);
    update();
  }

  /**
   * Handle a button change
   *
   * @param direction
   * @param buttons
   */
  protected void handleButtonChange(ButtonChangeDirection direction, Iterator<Button<M>> buttons) {
    switch (direction) {
      case DOWN:
        while (buttons.hasNext()) {
          Button<M> next = buttons.next();
          if (next == currentSelection) {
            // On prend le suivant
            currentSelection = buttons.hasNext() ? buttons.next() : buttonList.get(0);
            break;
          }
        }
        break;
      case UP:
        Button<M> previous = buttons.next();
        while (buttons.hasNext()) {
          Button<M> next = buttons.next();
          if (next == currentSelection) {
            break;
          }
          previous = next;
        }
        currentSelection = previous;
        break;
    }
  }

  public void pressButton() {
    if (currentSelection != null) {
      currentSelection.update();
    }
  }

  public enum ButtonChangeDirection {
    UP, DOWN
  }
}
