package polytech.devint.event.entity;

import polytech.devint.entity.Button;

/**
 * Event corresponding to a button being clicked
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class ButtonClickEvent extends EntityEvent<Button> {

  /**
   * Instantiates a new button click event
   *
   * @param button The button being clicked
   */
  public ButtonClickEvent(Button button) {
    super(button);
  }
}
