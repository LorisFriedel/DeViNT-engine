package polytech.devint.view;

import polytech.devint.model.Model;

/**
 * Represent a specific view used in the devint project
 * It forces the user to pass a "context help" when constructing a view
 *
 * @author Loris Friedel
 */
public abstract class DevintView<M extends Model> extends View<M> {

  private final ContextHelp contextHelp;

  public DevintView(ContextHelp contextHelp) {
    super();
    this.contextHelp = contextHelp;
  }

  /**
   * @return the context help that contains at least a general context help text
   */
  public ContextHelp getContextHelp() {
    return contextHelp;
  }

  @Override
  public void init() {
    super.init();

    playHelp();
  }

  @Override
  public void destroy() {
    super.destroy();

    stopHelp();
  }

  /**
   * Play the general help on the speaker
   */
  public void playHelp() {
    // get the sound and play it with the player (add it to the queue)
  }

  /**
   * Play the detailed help on the speaker
   */
  public void playDetailedHelp() {
    // get the sound and play it with the player (add it to the queue)
  }

  public void stopHelp() {
    // force stop playing the sound in the sound player
  }

  public void stopDetailedHelp() {
    // force stop playing the sound in the sound player
  }
}
