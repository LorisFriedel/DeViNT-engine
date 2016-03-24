package polytech.devint.view.sound.exception;

/**
 * @author Loris Friedel
 */
public class SoundInitializationFailedException extends RuntimeException {
  public SoundInitializationFailedException() {
    super();
  }

  public SoundInitializationFailedException(String msg) {
    super("Could not initialize the sound from: " + msg);
  }

  public SoundInitializationFailedException(String msg, String details) {
    super("Could not initialize the sound from: " + msg + " - " + details);
  }
}
