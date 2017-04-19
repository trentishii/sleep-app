package com.example.trent.sleepapp.pvt;

/**
 * A parameter for a PVT test. For example, duration, or min/max time between
 * dots.
 *
 * @author Dan Tasse
 */
public enum PVTIntParam implements PVTParam {

  /** Overall PVT duration, in ms. E.g. 1000 * 60 * 5 for 5 minutes. */
  test_duration(1000 * 60 * 5),
  /** Minimum delay between stimuli, in ms. */
  min_delay(2000),
  /** Maximum delay between stimuli, in ms. */
  max_delay(10000),
  /** Delay (ms) between numbers in the initial "3, 2, 1" countdown. */
  countdown_delay(2000),
  /** Delay (ms) after one trial is over before starting the next. */
  restart_delay(1000),
  /** Delay (ms) after the PVT is over before going back to the main screen. */
  game_over_delay(2000),
  /** "Don't cheat" delay, in ms. E.g. if the anticipate_delay is 100ms, and
   * the user clicks 20ms after a dot appears, we say "that's unreasonably
   * fast, the user must have been anticipating and just got lucky." */
  anticipate_delay(100),
  /** The user was a little too slow. */
  minor_lapse_delay(500),
  /** The user was too slow. N.B.: not currently used, but included to prevent
   * old config files that use it from causing errors. TODO: come up with more elegant solution here. */
  major_lapse_delay(9999000),
  /** The user was way too slow; show a reminder. */
  reminder_delay(9999000),
  /** The user was way too slow; go to next trial. */
  deadline(9999000),
  /** subject number */
  subject_id(1);

  public int defaultValue;

  private PVTIntParam(int defaultValue) {
    this.defaultValue = defaultValue;
  }
}
