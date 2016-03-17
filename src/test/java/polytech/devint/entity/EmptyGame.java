package polytech.devint.entity;

import polytech.devint.model.Model;

/**
 * An empty game for testing purpose
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class EmptyGame extends Game {

  int loops;

  public static final int TOTAL_LOOPS = 10;

  public EmptyGame(Model model) {
    super(model);
  }

  @Override
  public void gameLoop() {
    if (++loops == TOTAL_LOOPS) {
      this.terminate();
    }
  }

}
