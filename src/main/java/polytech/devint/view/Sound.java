package polytech.devint.view;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.tika.Tika;

import polytech.devint.scheduler.Scheduler;
import polytech.devint.util.Identifiable;
import polytech.devint.util.Timeout;
import polytech.devint.view.exception.SoundInitializationFailedException;
import polytech.devint.view.exception.SoundPlayException;

/**
 * Represent a playable sound The sound data are stored (raw) such as it is faster to play it again,
 * it is not reloaded each play.
 *
 * @author Loris Friedel
 * @author Gunther
 */
public class Sound extends Identifiable<Integer> implements Runnable {

  private static int nextId = 0;

  private String name;
  private AudioFormat format;
  private SourceDataLine dataLine;
  private byte[] soundData;
  private int dataLength;
  private long duration;

  private static final String[] WAV_MIMETYPES =
          {"audio/x-wav", "audio/vnd.wave", "audio/wav", "audio/wave"};

  /**
   * Create a playable sound from a .wav file
   *
   * @param waveFile the .wav file of the sound
   * @throws SoundInitializationFailedException if the sound cannot be properly initialized
   */
  public Sound(final File waveFile) throws SoundInitializationFailedException {
    super(nextId++);

    try {
      if (!isWave(waveFile)) {
        throw new SoundInitializationFailedException(waveFile.getName(), "Not a wav file");
      }
    } catch (IOException e) { // NOSONAR
      throw new SoundInitializationFailedException(waveFile.getName(),
              "Failed to detect file format");
    }

    try (AudioInputStream inputStream = AudioSystem.getAudioInputStream(waveFile)) {
      this.name = waveFile.getName();
      this.format = inputStream.getFormat();
      this.dataLine = AudioSystem.getSourceDataLine(format);

      this.soundData = new byte[inputStream.available()];
      this.dataLength = inputStream.read(soundData);
      this.duration = (long) (1000 * (inputStream.getFrameLength() / format.getFrameRate()));

    } catch (IOException | LineUnavailableException // NOSONAR
            | UnsupportedAudioFileException e) {
      throw new SoundInitializationFailedException(waveFile.getName());
    }
  }

  /**
   * Check if the given file is a wav file
   *
   * @param file file to test
   * @return true if the given file is a .wav audio file, false otherwise
   */
  private static boolean isWave(File file) throws IOException {
    Tika tika = new Tika();
    String format = tika.detect(file);
    for (String mimeType : WAV_MIMETYPES) {
      if (mimeType.equals(format)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return the sound duration in milliseconds
   */
  public long getDuration() {
    return duration;
  }

  /**
   * @return the name of the file that contains the current sound
   */
  public String getName() {
    return name;
  }

  /**
   * Play the sound until the end of it
   */
  public void play() {
    Scheduler.getDefaultScheduler().execute(() -> {
      if (!dataLine.isOpen()) {
        try {
          dataLine.open(format);
        } catch (LineUnavailableException e) { // NOSONAR
          throw new SoundPlayException();
        }
      }

      dataLine.start();
      dataLine.write(soundData, 0, dataLength);
      dataLine.drain();
      dataLine.stop();
    });
  }

  /**
   * Stop the sound being played and close the source data line
   */
  public void stop() {
    Scheduler.getDefaultScheduler().execute(() -> {
      if (dataLine.isOpen()) {
        dataLine.stop();
        dataLine.flush();
        dataLine.close();
      }
    });
  }

  @Override
  public void run() {
    play();
  }
}
