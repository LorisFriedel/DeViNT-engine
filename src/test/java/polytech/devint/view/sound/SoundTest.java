package polytech.devint.view.sound;

import org.junit.Test;
import polytech.devint.view.sound.exception.SoundInitializationFailedException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Loris Friedel
 */
public class SoundTest {
  @Test
  public void isWavTest() throws SoundInitializationFailedException, IOException {
    URL pathWav = SoundTest.class.getResource("/sound/do_1.wav");
    URL pathNotWav = SoundTest.class.getResource("/sound/textFile.txt");
    assertTrue(Sound.isWave(new File(pathWav.getFile())));
    assertFalse(Sound.isWave(new File(pathNotWav.getFile())));
  }
}
