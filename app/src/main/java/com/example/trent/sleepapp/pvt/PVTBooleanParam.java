package com.example.trent.sleepapp.pvt;

/**
 * A boolean parameter for a PVT test. Most of these are for turning on/off
 * parts of the app.
 *
 * @author Dan Tasse
 */
public enum PVTBooleanParam implements PVTParam {

  /** If true, will display "trial 1" etc as you do the PVT. */
  trial_counter_enabled(false),
  /** If true, displays user's response time after each trial. */
  visible_response_time_enabled(true),
  /** If true, will display an increasing timer on the dot as it appears
   * until the user reacts. */	//TODO: fix this or remove it
  count_up_timer_enabled(false),
  /** If true, the user can rest his finger on the screen and then push down
   * harder to react to the stimulus. */
  pressure_sensor_enabled(false),
	/** if true, the user can touch anywhere on the screen as a response
	 * Behaviour when this and touchUpEnabled are true is undefined. */
	touch_down_enabled(false),
	/** if true, the user can touch and then lift anywhere on the screen as a response. 
	 * Behaviour when this and touchDownEnabled are true is undefined. */
	touch_up_enabled(false),
	/** if true, the user can press various physical phone buttons (e.g., volume, menu) as a response. */
	physical_button_enabled(false),
  /** if true, the user can use goal crossing (moving their finger from one side to another) as a response. */
  goal_crossing_enabled(false),
  /** if true, some (visual) feedback is provided when the response is detected */
  response_feedback_enabled(true);

  public boolean defaultValue;

  private PVTBooleanParam(boolean defaultValue) {
    this.defaultValue = defaultValue;
  }
}
