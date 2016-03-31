package polytech.devint.view.swing.menu;

/**
 * @author Loris Friedel
 */
public class Button {

  private final Runnable action;
  private final String name;

  public Button(String name, Runnable action) {
    this.action = action;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Runnable getAction() {
    return action;
  }
}
