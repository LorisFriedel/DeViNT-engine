package polytech.devint.view.sound;

import polytech.devint.scheduler.SchedulerReady;
import polytech.devint.view.exception.SoundPlayException;

import javax.sound.sampled.LineUnavailableException;
import java.util.LinkedList;

/**
 * @author Loris Friedel
 */
public class SoundPlayer extends SchedulerReady {

  private LinkedList<Sound> soundQueue;

  public SoundPlayer() {
    this.soundQueue = new LinkedList<>();
  }

  /**
   * Play the sound until the end of it.
   */
  public void play(Sound sound) {
    getExecutor().execute(() -> {
      if (!sound.getDataLine().isOpen()) {
        try {
          sound.getDataLine().open(sound.getFormat());
        } catch (LineUnavailableException e) {
          throw new SoundPlayException(e);
        }
      }

      sound.getDataLine().start();
      sound.getDataLine().write(sound.getSoundData(), 0, sound.getDataLength());
      sound.getDataLine().drain();
      sound.getDataLine().stop();
    });
  }

  /**
   * Stop the sound if it is currently being played
   * and close the source data line
   */
  public void stop(Sound sound) {
    getExecutor().execute(() -> {
      if (sound.getDataLine().isOpen()) {
        sound.getDataLine().stop();
        sound.getDataLine().flush();
        sound.getDataLine().close();
      }
    });
  }
}
