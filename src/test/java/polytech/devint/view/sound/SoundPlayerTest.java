package polytech.devint.view.sound;

import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.SourceDataLine;

import java.io.File;

import static org.mockito.Mockito.*;

/**
 * @author Loris Friedel
 */
public class SoundPlayerTest {

  SoundPlayer sp;

  @Before
  public void setUp() {
    sp = new SoundPlayer();
  }

  @Test
  public void simplePlay() throws InterruptedException {
    // TODO
  }

  @Test
  public void queuePlay() throws InterruptedException {
    Sound s = mock(Sound.class);
    when(s.getDuration()).thenReturn(20L);
    SourceDataLine sdl = mock(SourceDataLine.class);
    when(sdl.isOpen()).thenReturn(false);
    when(s.getDataLine()).thenReturn(sdl);

    sp = spy(SoundPlayer.class);
    sp.enqueue(s);
    sp.enqueue(s);
    sp.enqueue(s);

    sp.playQueue();

    long marginMs = 100;
    Thread.sleep(s.getDuration()*3 + marginMs);

    verify(sp, times(3)).stopIfPlaying();
    verify(sp, times(4)).nextSound();
  }
}
