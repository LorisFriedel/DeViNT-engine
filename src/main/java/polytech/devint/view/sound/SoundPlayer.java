package polytech.devint.view.sound;

import polytech.devint.scheduler.SchedulerReady;
import polytech.devint.view.sound.exception.SoundPlayException;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Loris Friedel
 */
public class SoundPlayer extends SchedulerReady {

  private static final long MARGIN_INTERVAL_MS = 5;
  private ConcurrentLinkedDeque<Sound> soundQueue;
  private Sound currentSound;
  private ScheduledFuture<?> updateTask;
  private Runnable onQueueEnd = () -> {};

  public SoundPlayer() {
    this.soundQueue = new ConcurrentLinkedDeque<>();
  }

  /////// GENERAL ///////

  /**
   * Stop the current sound if it is playing.
   */
  void stopIfPlaying() {
    if (currentSound != null) {
      stop(currentSound);
    }
  }

  private void setCurrent(Sound sound) {
    this.currentSound = sound;
  }

  /////// SIMPLE PLAY ///////

  /**
   * Force the given sound to be played by the player:
   * If a previous sound was playing, force it to stop.
   *
   * @param sound sound to play no matter what.
   */
  public void forcePlay(Sound sound) {
    stopPlay();
    play(sound);
  }

  /**
   * Force the current playing sound to stop.
   */
  public void stopPlay() {
    stopQueue();
  }

  /**
   * Play the sound until the end of it.
   */
  private void play(Sound sound) {
    setCurrent(sound);
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
  private void stop(Sound sound) {
    if (sound.getDataLine().isOpen()) {
      sound.getDataLine().stop();
      sound.getDataLine().flush();
      sound.getDataLine().close();
    }
  }


  /////// QUEUE ///////

  /**
   * Add the given sound to the sound queue of the player.
   *
   * @param sound Sound to add to the playlist queue.
   */
  public void enqueue(Sound sound) {
    soundQueue.add(sound);
  }

  /**
   * Stop and clear the sound queue.
   */
  public void flushQueue() {
    stopQueue();
    soundQueue.clear();
  }

  /**
   * Play all sound that are currently in the playlist of the sound player.
   */
  public void playQueue() {
    playQueue(() -> {
    });
  }

  /**
   * Play all sound that are currently in the playlist of the sound player.
   * Run the given runnable at the end of the playlist, when all sound have been played.
   *
   * @param onFinish runnable to run a the end of the playlist.
   */
  public void playQueue(Runnable onFinish) {
    onQueueEnd = onFinish;
    nextSound();
  }

  /**
   * Force the playlist to stop being played.
   */
  public void stopQueue() {
    if (updateTask != null && !updateTask.isDone()) {
      updateTask.cancel(true);
    }
    stopIfPlaying();
  }

  /**
   * Play the next sound in the playlist
   */
  void nextSound() {
    if (soundQueue.isEmpty() && onQueueEnd != null) {
      new Thread(onQueueEnd).start();
      return;
    }

    stopIfPlaying();
    setCurrent(soundQueue.pop());
    play(currentSound);

    updateTask = schedule(
            this::nextSound,
            currentSound.getDuration() + MARGIN_INTERVAL_MS);
  }
}
