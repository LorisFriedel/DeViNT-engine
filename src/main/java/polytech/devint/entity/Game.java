package polytech.devint.entity;

import java.util.concurrent.TimeUnit;

import polytech.devint.model.Model;
import polytech.devint.scheduler.CustomScheduler;
import polytech.devint.scheduler.RepeatableTask;

/**
 * A game It has a state
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public abstract class Game<M extends Model> extends Component<M> {

  protected GameState gameState;

  /**
   * Current delay between each game loop
   */
  public final int MILLISECONDS_PER_LOOP;

  /**
   * Instantiates a new game under a model with a new entity manager
   *
   * @param model The model
   */
  public Game(M model) {
    this(model, new EntityManager());
  }

  /**
   * Instantiates a new game under a model
   *
   * @param model         The model
   * @param entityManager The entity manager
   */
  public Game(M model, EntityManager entityManager) {
    super(model, entityManager);
    // A game is stopped by default, waiting for activate() to activate it
    gameState = GameState.STOPPED;
    MILLISECONDS_PER_LOOP = initLoopInterval();
  }

  @Override
  public void onRemove() {
    // We update the game state
    gameState = GameState.STOPPED;
  }

  @Override
  public void updateComponent() {
    // Do nothing, using a game loop approach instead
  }

  @Override
  public void activate() {
    gameState = GameState.ACTIVE;
    CustomScheduler.getDefaultCustomScheduler().repeat(new RepeatableTask() {

      @Override
      public void execute() {
        try {
          if (gameState.equals(GameState.STOPPED)) {
            // We need to stop the loop
            this.stop();
          } else if (gameState.equals(GameState.ACTIVE)) {
            entityManager.update();
            gameLoop();
          }
        } catch (Exception e) {
          // TODO: Remove, use logger
          e.printStackTrace();
        }
      }
    }, MILLISECONDS_PER_LOOP);
  }

  /**
   * Executed every game loop
   */
  public abstract void gameLoop();

  /**
   * Toggle ACTIVE mode to PAUSE mode
   */
  public void togglePause() {
    gameState = gameState.equals(GameState.PAUSED) ? GameState.ACTIVE : GameState.PAUSED;
  }

  public GameState getGameState() {
    return gameState;
  }

  /**
   * @return True if the game has been paused
   */
  public boolean isPaused() {
    return gameState.equals(GameState.PAUSED);
  }

  public void setGameState(GameState state) {
    gameState = state;
    if (gameState.equals(GameState.STOPPED)) {
      getEntityManager().foreach(Entity::setDead);
    }
  }

  /**
   * 
   * @return The loop interval
   */
  protected int initLoopInterval() {
    return 33;
  }

  /**
   * 
   * @return The constant loop interval
   */
  public int getLoopTime() {
    return MILLISECONDS_PER_LOOP;
  }
}
