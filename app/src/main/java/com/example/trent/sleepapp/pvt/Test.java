package com.example.trent.sleepapp.pvt;

import com.example.trent.sleepapp.pvt.PVTBooleanParam;
import com.example.trent.sleepapp.pvt.PVTConfigParamsMap;
import com.example.trent.sleepapp.pvt.PVTIntParam;
import com.example.trent.sleepapp.pvt.PVTStringParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Test is a series of Trials. Typically a test will take a few minutes,
 * maybe 10-20.
 * 
 * @author Dan Tasse
 */
public class Test implements Serializable {
	/** Strings allocated in beginning to prevent garbage collection. */
	private static final String UNKNOWN = "Unknown";
	
	// auto-generated, must match server's Trial class ID
	private static final long serialVersionUID = -5944847445097703164L;

	/** See the SummaryStat enum for more info about these parameters. */
	public int testNum;
	public int subjectId;
	public int batch;
	public String subjectHand;
	public int lengthTrials;

	public int countdownDelay;
	public int restartDelay;
	public int gameOverDelay;
	public int anticipateDelay;
	public int minorLapseDelay;
	public int reminderDelay;
	public int deadline;
	public int minDelay;
	public int maxDelay;

	/** duration planned for this test (it might last a little longer b/c the
	 * last trial won't just cut off when the time's up.) */
	public int duration;
	/** when the PVT session started (ms since epoch) */
	public long startTimestamp;
	/** when the PVT session started (uptime ms) */
	public long startTime;
	/** when the PVT session ended (ms since epoch) */
	public long finishTimestamp;

	/** the "ms" number on the dot. */
	public boolean countUpTimerEnabled;
  /** If true, displays user's response time after each trial. */
  public boolean visibleResponseTimeEnabled;
	/** the text on the on-screen button to show the current trial number. */
	public boolean trialCounterEnabled;
	/** whether the user can rest their finger on the screen and the push down
	 * harder to react. */
	public boolean pressureSensorEnabled;
	/** if true, the user can touch anywhere on the screen as a response
	 * Behaviour when this and touchUpEnabled are true is undefined. */
	public boolean touchDownEnabled;
	/** if true, the user can touch and then lift anywhere on the screen as a response. 
	 * Behaviour when this and touchDownEnabled are true is undefined. */
	public boolean touchUpEnabled;
	/** if true, the user can press various physical phone buttons (e.g., volume, menu) as a response. */
	public boolean physicalButtonEnabled;
  /** if true, the user can use goal crossing (moving their finger from one side to another) as a response. */
  public boolean goalCrossingEnabled;
  /** if true, some (visual) feedback is provided when the response is detected */
  public boolean responseFeedbackEnabled;
  /** free-form tag used to label this test */
  public String testTag;
	
	public List<Trial> trials = new ArrayList<Trial>();
	
	public List<String> eventLog = new ArrayList<String>(10000);	//preallocate large array here since we're going to have a lot of events (mitigates GC)

	public Test(PVTConfigParamsMap params, int testNum) {
		this.testNum = testNum;
		this.subjectId = params.getIntValueOrDefault(PVTIntParam.subject_id);
		this.batch = 0; //TODO
		this.subjectHand = UNKNOWN; //TODO
		this.lengthTrials = 0; //TODO

		countdownDelay = params.getIntValueOrDefault(PVTIntParam.countdown_delay);
		restartDelay = params.getIntValueOrDefault(PVTIntParam.restart_delay);
		gameOverDelay = params.getIntValueOrDefault(PVTIntParam.game_over_delay);
		anticipateDelay = params.getIntValueOrDefault(PVTIntParam.anticipate_delay);
		minorLapseDelay = params.getIntValueOrDefault(PVTIntParam.minor_lapse_delay);
		reminderDelay = params.getIntValueOrDefault(PVTIntParam.reminder_delay);
		deadline = params.getIntValueOrDefault(PVTIntParam.deadline);
		minDelay = params.getIntValueOrDefault(PVTIntParam.min_delay);
		maxDelay = params.getIntValueOrDefault(PVTIntParam.max_delay);
		duration = params.getIntValueOrDefault(PVTIntParam.test_duration);
		countUpTimerEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.count_up_timer_enabled);
		visibleResponseTimeEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.visible_response_time_enabled);
		trialCounterEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.trial_counter_enabled);
		pressureSensorEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.pressure_sensor_enabled);
		touchDownEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.touch_down_enabled);
		touchUpEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.touch_up_enabled);
		physicalButtonEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.physical_button_enabled);
		goalCrossingEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.goal_crossing_enabled);
		responseFeedbackEnabled = params.getBooleanValueOrDefault(PVTBooleanParam.response_feedback_enabled);
		testTag = params.getStringValueOrDefault(PVTStringParam.test_tag);
	}

}
