package polytech.devint.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import polytech.devint.model.Model;

/**
 * Testing a game
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class GameTest {

  /**
   * Testing the game loop (if it properly stops after terminating it)
   *
   * @throws InterruptedException
   */
  @Test
  public void testingGameLoop() throws InterruptedException {
    Model model = new Model() {};
    EmptyGame game = new EmptyGame(model);
    game.activate();
    Thread.sleep((long) (33 * EmptyGame.TOTAL_LOOPS * 1.5));
    assertEquals(EmptyGame.TOTAL_LOOPS, game.loops);
  }

  /**
   * Testing the game pause
   *
   * @throws InterruptedException
   */
  @Test
  public void testingGamePause() throws InterruptedException {
    Model model = new Model() {};
    EmptyGame game = new EmptyGame(model);
    game.activate();
    game.togglePause();
    assertTrue(game.isPaused());
    Thread.sleep(33 * 5);
    // We allow one loop because it could have executed a first loop
    // between the activate() and togglePause() method calls
    assertTrue(game.loops <= 1);
    game.togglePause();
    assertFalse(game.isPaused());
    Thread.sleep((long) (33 * EmptyGame.TOTAL_LOOPS * 1.5));
    assertEquals(EmptyGame.TOTAL_LOOPS, game.loops);
  }

}
