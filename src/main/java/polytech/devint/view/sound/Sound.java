package polytech.devint.view.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.tika.Tika;

import polytech.devint.util.Identifiable;
import polytech.devint.view.sound.exception.SoundInitializationFailedException;

import java.util.Arrays;
import java.util.List;

/**
 * Represent a sound.
 * The sound data are stored (raw) such as it is faster to play it again,
 * it is not reloaded each play.
 * Support only wav sound.
 * To play a sound, a sound player must be used.
 *
 * @author Loris Friedel
 */
public class Sound extends Identifiable<Integer> {

  private static int nextId = 0;

  private String name;
  private AudioFormat format;
  private SourceDataLine dataLine;
  private byte[] soundData;
  private int dataLength;
  private long duration;

  private static final List<String> WAV_MIMETYPES =
          Arrays.asList("audio/x-wav", "audio/vnd.wave", "audio/wav", "audio/wave");
  private static final int MS_IN_ONE_SECOND = 1000;

  /**
   * Create a playable sound from a .wav file
   *
   * @param waveFile the .wav file of the sound
   * @throws SoundInitializationFailedException if the sound cannot be properly initialized
   */
  public Sound(final File waveFile) {
    super(nextId++);

    try {
      if (!isWave(waveFile)) {
        throw new SoundInitializationFailedException(waveFile.getName(), "Not a wav file");
      }
    } catch (IOException e) { // NOSONAR
      throw new SoundInitializationFailedException(waveFile.getName(), "Failed to detect file format");
    }

    try (AudioInputStream inputStream = AudioSystem.getAudioInputStream(waveFile)) {
      this.name = waveFile.getName().replace(".wav", "");
      this.format = inputStream.getFormat();
      this.dataLine = AudioSystem.getSourceDataLine(format);

      this.soundData = new byte[inputStream.available()];
      this.dataLength = inputStream.read(soundData);
      this.duration = (long) (MS_IN_ONE_SECOND * (inputStream.getFrameLength() / format.getFrameRate()));

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
  static boolean isWave(File file) throws IOException {
    return WAV_MIMETYPES.contains(new Tika().detect(file));
  }

  /**
   * @return the sound duration in milliseconds
   */
  public long getDuration() {
    return duration;
  }

  /**
   * @return the menuName of the file that contains the current sound
   */
  public String getName() {
    return name;
  }

  /**
   * @return the audio format of this sound
   */
  public AudioFormat getFormat() {
    return format;
  }

  /**
   * @return the data line of this sound
   */
  public SourceDataLine getDataLine() {
    return dataLine;
  }

  /**
   * @return the raw data of the sound
   */
  public byte[] getSoundData() {
    return soundData;
  }

  /**
   * @return the number of byte that composed this sound
   */
  public int getDataLength() {
    return dataLength;
  }
}
