package polytech.devint.entity;

/**
 * Represents a game state
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public enum GameState {

  /**
   * Represents an active state (when the game is running)
   */
  ACTIVE,

  /**
   * Represents a paused state (when the game has been paused)
   */
  PAUSED,

  /**
   * Represents a stopped state (when the game has been stopped or never been activated)
   */
  STOPPED
}
