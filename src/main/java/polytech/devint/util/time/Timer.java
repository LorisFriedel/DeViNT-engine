package polytech.devint.util.time;


import polytech.devint.scheduler.SchedulerReady;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static polytech.devint.util.time.Timer.State.*;

/**
 * Represent a generic timer
 *
 * @author Loris Friedel
 */
public abstract class Timer extends SchedulerReady {

  protected long interval;
  protected long elapsedTime;
  protected long duration;
  protected volatile State state;
  protected ScheduledFuture<?> updateTask;

  protected static final long WAITING_TIME_BEFORE_START = 0L;
  protected static final long DEFAULT_INTERVAL_MS = 10L;

  enum State {
    READY, RUNNING, PAUSED, FINISHED, CANCELED
  }

  /**
   * @param duration The period in milliseconds for which the timer should run.
   *                 After this time has passed, the timer will stop.
   * @param interval The time gap between each tick in milliseconds.
   */
  public Timer(long duration, long interval) {
    super();
    this.duration = duration;
    this.interval = interval;
    this.elapsedTime = 0;
    this.state = READY;
  }

  /**
   * This constructor uses the default interval : 10 ms
   *
   * @param duration The period in milliseconds for which the timer should run.
   *                 After this time has passed, the timer will stop.
   */
  public Timer(long duration) {
    this(duration, DEFAULT_INTERVAL_MS);
  }

  /**
   * @return True if the timer is ready to be started, after a reset or if it was never started,
   * false otherwise.
   */
  public boolean isReady() {
    return state.equals(READY);
  }

  /**
   * @return True if the timer is currently running, false otherwise.
   */
  public boolean isRunning() {
    return state.equals(RUNNING);
  }

  /**
   * @return True if the timer is currently paused, false otherwise.
   */
  public boolean isPaused() {
    return state.equals(PAUSED);
  }

  /**
   * @return True if the timer run for its total duration without being canceled, false otherwise.
   */
  public boolean hasFinished() {
    return state.equals(FINISHED);
  }

  /**
   * @return True if the timer has been canceled and is not yet ready, false otherwise.
   */
  public boolean hasBeenCanceled() {
    return state.equals(CANCELED);
  }

  /**
   * Starts the timer. If the timer was already running, this call is ignored.
   */
  public void start() {
    if (canSwitchTo(RUNNING)) {
      startProcess();
      switchState(RUNNING);
      onStart();
    }
  }

  /**
   * Process that starts the timer.
   */
  private void startProcess() {
    startTimerThread();
  }

  /**
   * Start the timer update task on a new thread
   */
  private void startTimerThread() {
    updateTask = getExecutor().scheduleAtFixedRate(
            this::tick,
            WAITING_TIME_BEFORE_START,
            interval, TimeUnit.MILLISECONDS);
  }

  /**
   * This method is called periodically with the interval set as the delay between subsequent calls.
   */
  private void tick() {
    onTick();
    elapsedTime += interval;
    if (elapsedTime >= duration) {
      finish();
    }
  }

  /**
   * Paused the timer. The timer can be resume after being paused.
   * If the timer is not running, this call is ignored.
   */
  public void pause() {
    if (canSwitchTo(PAUSED)) {
      killTimerTask();
      switchState(PAUSED);
    }
  }

  /**
   * Resumes the timer if it was paused, else does nothing
   */
  public void resume() {
    if (isPaused()) {
      start();
    }
  }

  /**
   * End the timer.
   * After this method being called:
   * - The timer is considered finished, i.e. it run its total duration.
   * - The timer is not considered canceled.
   */
  private void finish() {
    if (canSwitchTo(FINISHED)) {
      finishProcess();
      switchState(FINISHED);
      onFinish();
    }
  }

  /**
   * Process the logic to stop the timer.
   */
  private void finishProcess() {
    killTimerTask();
  }

  /**
   * Stop the timer and reset it.
   * This method can only be called if the timer is running or paused.
   * If the timer is not running and already ready to use, then this call does nothing.
   */
  public void cancel() {
    if (canSwitchTo(CANCELED)) {
      killTimerTask();
      resetTime();
      switchState(CANCELED);
    }
  }

  /**
   * Reset the timer.
   * If the timer is running, this method does nothing.
   * This method can only be used if the timer has finished or has been canceled.
   * After a call of this method, the timer will be ready to use again.
   */
  public void reset() {
    if (canSwitchTo(READY)) {
      resetTime();
      switchState(READY);
    }
  }

  /**
   * Restart the timer. The elapsed time is set to 0 and the timer is started after being cancelled.
   */
  public void restart() {
    cancel();
    start();
  }

  /**
   * This method is called when the timer is started.
   * (the call happens right after the timer is started)
   */
  protected void onStart() {
    // empty by default
  }

  /**
   * This method is called periodically with the interval set as the delay between subsequent calls.
   * (the call happens right before the check of the end of the timer)
   */
  protected void onTick() {
    // empty by default
  }


  /**
   * This method is called once the timer has run for the specified duration
   * and has not been canceled. (the call happens right after the timer ended)
   */
  protected void onFinish() {
    // empty by default
  }

  /**
   * @return The elapsed time (in milliseconds) since the start of the timer.
   */
  public long getElapsedTime() {
    return elapsedTime;
  }

  /**
   * Set the elapsed time to 0.
   */
  private void resetTime() {
    elapsedTime = 0;
  }

  /**
   * Kill the update task of the timer
   */
  private void killTimerTask() {
    updateTask.cancel(false);
  }

  /**
   * @return The time remaining (in milliseconds) for the timer to stop.
   */
  public long getRemainingTime() {
    return duration - elapsedTime;
  }

  /**
   * Check if the timer can switch to the given state, regarding its current state.
   *
   * @param state state we want to switch on
   * @return true if the switch is authorized, false otherwise
   */
  private boolean canSwitchTo(State state) {
    switch (state) {
      case READY:
        return !isReady() && (hasFinished() || hasBeenCanceled());
      case RUNNING:
        return !isRunning() && (isReady() || isPaused() || hasBeenCanceled());
      case PAUSED:
        return !isPaused() && (isRunning());
      case FINISHED:
        return !hasFinished() && (isRunning());
      case CANCELED:
        return !hasBeenCanceled() && (isRunning() || isPaused());
      default:
        return false;
    }
  }

  /**
   * Make the timer switch to the given state
   *
   * @param newState new state of the timer
   */
  private void switchState(State newState) {
    state = newState;
  }
}
