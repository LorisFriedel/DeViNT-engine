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

  /*
  Deux cas:
   - on veut jouer des sons en mode forcé: on lance un seul son, et si on en lance un second, alors celui qui était en train de jouer ce stop illico
   - on veut jouer une suite de sons: on veut savoir quand c'est fini de jouer
      (callback ou isFinish()) et on veut qu'ils s'enchaine avec un interval précis (0 ou plus de MS)
   */

  public void enqueue(Sound sound) {
    soundQueue.add(sound);
  }

  public void clearQueue() {
    //stopQueue();
    soundQueue.clear();
  }

  public void forcePlay(Sound sound) {
    // stop all sound being played, clear the queue and play the given sound
    // //stop and clear the queue and enqueue it, or stop the current one, add it to the beginning of the queue ?
  }

  public void playQueue() {
    playQueue(() -> {});
  }

  public void playQueue(Runnable onFinish) {
    // on play un son
    // on dégage le son de la queue une fois qu'il a FINI de jouer
    // quand la queue est empty, on appelle onFinish
  }

  /**
   * Play the sound until the end of it.
   */
  private synchronized void play(Sound sound) {
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
  private synchronized void stop(Sound sound) {
    getExecutor().execute(() -> {
      if (sound.getDataLine().isOpen()) {
        sound.getDataLine().stop();
        sound.getDataLine().flush();
        sound.getDataLine().close();
      }
    });
  }
}
