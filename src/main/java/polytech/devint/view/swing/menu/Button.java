package polytech.devint.view.swing.menu;

import polytech.devint.view.sound.Sound;

import java.util.Optional;


/**
 * @author Loris Friedel
 */
public class Button {

  private final Runnable action;
  private final String name;
  private final Optional<Sound> enunciation;

  public Button(String name, Runnable action) {
    this(name, action, null);
  }

  public Button(String name, Runnable action, Sound enunciation) {
    this.action = action;
    this.name = name;
    this.enunciation = Optional.ofNullable(enunciation);
  }

  public String getName() {
    return name;
  }

  public Runnable getAction() {
    return action;
  }

  public Optional<Sound> getEnunciation() {
    return enunciation;
  }
}
