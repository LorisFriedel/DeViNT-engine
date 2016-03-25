package polytech.devint.view;

import polytech.devint.model.Model;
import polytech.devint.view.sound.SoundPlayer;

/**
 * Represent a specific view used in the devint project
 * It forces the user to pass a "context help" when constructing a view
 *
 * @author Loris Friedel
 */
public abstract class DevintView<M extends Model> extends View<M> {

  private static final SoundPlayer soundPlayer = new SoundPlayer();

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

  // TODO faire un register main view static, qui se mettra en place a chaque fois qu'on fait échap ?

  protected SoundPlayer soundPlayer() {
    return soundPlayer;
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
    if(contextHelp.getAudio().isPresent()) {
      soundPlayer.forcePlay(contextHelp.getAudio().get());
    } else {
      // SIVOX
    }
  }

  /**
   * Play the detailed help on the speaker
   */
  public void playDetailedHelp() {
    if(contextHelp.getDetailedAudio().isPresent()) {
      soundPlayer.forcePlay(contextHelp.getDetailedAudio().get());
    } else {
      // SIVOX
    }
  }

  public void stopHelp() {
    if(contextHelp.getAudio().isPresent()) {
      soundPlayer.stopPlay();
    } else {
      // STOP SIVOX
    }
  }

  public void stopDetailedHelp() {
    if(contextHelp.getDetailedAudio().isPresent()) {
      soundPlayer.stopPlay();
    } else {
      // STOP SIVOX
    }
  }
}
