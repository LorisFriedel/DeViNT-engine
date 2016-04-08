package polytech.devint.model.menu;

import polytech.devint.model.Model;
import polytech.devint.view.sound.Sound;
import polytech.devint.view.swing.menu.Button;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Loris Friedel
 */
public class SwingMenuModel extends Model {

  private List<Button> buttonList;

  public SwingMenuModel() {
    this.buttonList = new LinkedList<>();
  }

  public void addButton(String name, Runnable action) {
    buttonList.add(new Button(name, action));
  }

  public void addButton(String name, Runnable action, Sound sound) {
    buttonList.add(new Button(name, action, sound));
  }

  public void removeButton(Button button) {
    buttonList.remove(button);
  }

  public List<Button> getButtons() {
    return buttonList;
  }
}
