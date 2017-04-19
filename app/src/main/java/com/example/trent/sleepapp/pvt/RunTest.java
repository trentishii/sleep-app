package com.example.trent.sleepapp.pvt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.trent.sleepapp.PAMActivity;
import com.example.trent.sleepapp.R;
import com.example.trent.sleepapp.pvt.PVTConfig;
import com.example.trent.sleepapp.pvt.PVTConfig.PVTConfigTest;
import com.example.trent.sleepapp.pvt.PVTConfigGetter;
import com.example.trent.sleepapp.pvt.PVTConfigParamsMap;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import com.example.trent.sleepapp.pvt.*;

/**
 * This is the main Activity that runs a flight of trials. Trials are returned
 * in the result to the PVT activity which called us.
 */
public class RunTest extends Activity implements Observer {
	/** Strings allocated in beginning to prevent garbage collection. */
	private static final String SPOT = "spot";
	private static final String EXCEPTION = "A PVT Config must have at least one spot test";
	private static final String LAST_SPOT_TEST_NUM = "last_spot_test_num";
	private static final String PVT = "pvt";
	private static final String T0 = "t_0 = ";
	private static final String D = ", d = ";
	private static final String ZEROS = "000000";
	private static final String TEST = "test";
	private static final String TRIAL = "Trial ";
	private static final String MS = "ms";
	private static final String T1 = "t_1 = ";
	private static final String T2 = "t_2 = ";
	private static final String MEASURED_D = ", measured d = ";
	private static final String EPSILON = ", epsilon = ";
	private static final String TOO_EARLY = "Too early";
	
	private static final int NEUTRAL_COLOR = Color.WHITE;
	private static final int STIMULUS_COLOR = Color.BLACK;
	private static final int RESPONSE_FEEDBACK_COLOR = Color.LTGRAY;

	public static final String TAG = "PVT"; //getClass().getSimpleName();
	public static final String PREFS_NAME = "pvt_settings";
	public static final boolean AUTOMATED = false;	//are we running automated tests? (no user interaction)

  private float restThreshHold = 0.230000f; // screen pressure to register
                                            // "resting" position
  private float downThreshHold = 0.500000f; // screen pressure to register
                                            // "down" position

  /**
   * States for the test
   */
  public static enum PVTState {
  	/** Beginning of the test */
    START,
    /** Begin counting down to start the test */
    COUNTDOWN3,
    /** Counting down to start the test */
    COUNTDOWN2, 
    /** Last step in countdown to start the test */
    COUNTDOWN1, 
    /** User is waiting for stimulus to be shown (no stimulus visible yet) */
    FOREPERIOD, 
    /** Show stimulus and wait for user response */
    SHOW_STIMULUS,
    /** User provided response before the stimulus was shown */
    FALSE_START,
    /** Did not response to the stimulus before the deadline elapsed */
    DEADLINE,
    /** User reacted after stimulus was shown; indicated that their reaction was
     * received (also show their RT if RT display is enabled) */ 
    SHOW_REACTION, 
    /** Test time elapsed and the test is over */
    TEST_COMPLETE, 
    /** Cleanup post-test */
    CLEANUP_AND_FINISH,
    /** Cancel the test */
    CANCEL,
    /** Test is done: it was canceled or has finished cleanly */
    DONE,
  }

  private SharedPreferences prefs;
  private SharedPreferences.Editor prefsEditor;
  private Test test;
  private TestUiModel uiModel;
  private PVTRun mPVTRun;
  private Handler handler;
  private CountUpTimer countUpTimer = new CountUpTimer();
  /**
   * Triggers a reminder when fired.
   */
  private Runnable reminderTimer;
  private TonePlayer reminderTone = new TonePlayer();
  /**
   * Triggers the deadline when fired.
   */
  private Runnable deadlineTimer;

  // layout objects
  private View mainView;
  private View goalLine;
  private InstrumentedImageSurfaceView dot;
  private TextView timeText;
  private TextView readyText;
  private TextView numText;
  
  //vars for measuring timing delays
  public long stimulusDelay;		//target delay for time until stimulus is shown
  public long t_0;							//time that delay for stimulus is called
  public long t_1;							//time that delay for stimulus is called
  public long t_2;							//time that delay for stimulus is called
  public long t_3;							//time that delay for stimulus is called
	private WakeLock wl;

	/** State used for goal crossing detection */
	private static enum GoalCrossState {
		/** user is not touching*/
		UP,
		/** user is touching on left side */
		LEFT,
		/** user is touching on right side */
		RIGHT
	}

	@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // remove title bar
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.run_test);

    // get params from the pvt_config.xml file
    PVTConfig mConfiguration = new PVTConfigGetter(this).get();
    PVTConfigParamsMap params = null;
    for (int i = 0; i < mConfiguration.tests.size(); i++) {
    	PVTConfigTest test = mConfiguration.tests.get(i);
      if (test.type.equals(SPOT)) {
        params = test.testParams;
        break;
      }
    }
    if (params == null) {
      throw new RuntimeException(EXCEPTION);
      // TODO handle this error some other way?
    }
    // get extra test params; from prefs in this case
    prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    prefsEditor = prefs.edit();
    int testNum = prefs.getInt(LAST_SPOT_TEST_NUM, 0) + 1;
    prefsEditor.putInt(LAST_SPOT_TEST_NUM, testNum).commit();

    // feed them all into the Test model class.
    test = new Test(params, testNum);

    // handler to run our test
    handler = new Handler();

    // state machine
    uiModel = new TestUiModel(test, new TrialFactory(test, new Random()));
    uiModel.addObserver(this);
    mPVTRun = new PVTRun();
    reminderTimer = new Runnable() {
			@Override
			public void run() {
				//TODO: this should be a part of the model
		    switch (uiModel.getCurrentState()) {
		    case SHOW_STIMULUS:		//reminder delay elapsed while stimulus shown; play reminder sound
		    	//we don't remove all callbacks here (that is, we don't call removeHandlerCallbacks) 
		    	//because we don't transition out of SHOW_STIMULUS
		    	
		    	//TODO: this should cause a brief state change in the model or be routed through
		    	//the model rather than just done here.
		    	
		    	//play tone
		    	Log.d(TAG, "Reminder");
		    	reminderTone.play();
		    	
		      break;
		    default:
		      break;
		    }
			}
    };
    deadlineTimer = new Runnable() {
			@Override
			public void run() {
				//TODO: this should be a part of the model
		    switch (uiModel.getCurrentState()) {
		    case SHOW_STIMULUS:		//deadline passed while stimulus shown; transition to DEADLINE
					removeHandlerCallbacks();
		      uiModel.deadline(SystemClock.uptimeMillis());
		      handler.postDelayed(mPVTRun, test.restartDelay);
		      break;
		    default:
		      break;
		    }
			}
    };

    // grab layout objects
    mainView = findViewById(R.id.main_view);
    goalLine = findViewById(R.id.goal_line);
    dot = (InstrumentedImageSurfaceView) findViewById(R.id.stimulus);
    dot.runTest = this;
    timeText = (TextView) findViewById(R.id.time);
    readyText = (TextView) findViewById(R.id.ready_message);
    numText = (TextView) findViewById(R.id.test_num);
    if (!test.trialCounterEnabled) {
      numText.setVisibility(View.GONE);
    }

    if (test.touchDownEnabled) {
      mainView.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
          if (e.getAction() == MotionEvent.ACTION_DOWN) {
          	long t = e.getEventTime();
//          	test.eventLog.add("PRESS at: " + t);
            reactToStimulus(t);
          }
          return true;
        }
      });
    }
    if (test.touchUpEnabled) {
      mainView.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
          if (e.getAction() == MotionEvent.ACTION_UP) {
          	long t = e.getEventTime();
//          	test.eventLog.add("LIFT at: " + t);
            reactToStimulus(t);
          }
          return true;
        }
      });
    }
    if (test.pressureSensorEnabled) {
    	mainView.setOnTouchListener(new OnTouchListener() {
    		@Override
    		public boolean onTouch(View v, MotionEvent e) {
    			if (e.getAction() == MotionEvent.ACTION_MOVE) {
    				float pressure = e.getSize();		//use size instead of pressure since capacitive screens don't do well with pressure
    				long t = e.getEventTime();
//    				test.eventLog.add("Pressure: " + pressure + " at: " + t);
    				if (pressure > restThreshHold && pressure < downThreshHold) { // rest
    				} else if (pressure > downThreshHold) { // down
//    					test.eventLog.add("DOWN");
    					reactToStimulus(t);
    				} else { // up
//    					test.eventLog.add("UP");
    				}
    			}
    			return true;
    		}
    	});
    }
    if (test.goalCrossingEnabled) {
    	mainView.setOnTouchListener(new OnTouchListener() {
    		private GoalCrossState state = GoalCrossState.UP;
    		@Override
    		public boolean onTouch(View v, MotionEvent e) {
    			int action = e.getAction();
    			float x = e.getX();
    			float half = v.getWidth() / 2;
    			long t = e.getEventTime();
    			GoalCrossState newSide = x < half ? GoalCrossState.LEFT : GoalCrossState.RIGHT;
    			
//    			test.eventLog.add("Touch x: " + x + " y: " + e.getY() + " at: " + t + " action:" + action);
    			if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
    				state = GoalCrossState.UP;
    			}
    			else if (state == GoalCrossState.UP) {
    				if (action == MotionEvent.ACTION_DOWN) state = newSide;
    			}
    			else if (state != newSide) {	//side changed => goal was crossed
//    				test.eventLog.add("CROSS: " + t);
  					reactToStimulus(t);
  					state = newSide;
    			}
    			return true;
    		}
    	});
    	goalLine.setVisibility(View.VISIBLE);
    }

    //make sure screen stays on during test
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, PVT);
    wl.setReferenceCounted(false);
    wl.acquire();
    
    //run the GC before the beginning of the test so that it will take as long as possible before it is triggered again
    System.gc();
    uiModel.lastGcTime = 0;	//don't throw out a trial because of our initial explicit GC
    
    //transition in from black to hide the initial black area caused by using a surface view (a little hacky)
    mainView.setBackgroundColor(Color.BLACK);
    handler.postDelayed(new Runnable() { public void run() {
	    mainView.setBackgroundColor(NEUTRAL_COLOR);
	    dot.color = NEUTRAL_COLOR;
			dot.draw();

			// go!
	    handler.postDelayed(mPVTRun, test.countdownDelay);
    }}, 500);
  }
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (test.physicalButtonEnabled) {
			switch (e.getKeyCode()) {
			case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if (e.getAction() == KeyEvent.ACTION_DOWN) {
					reactToStimulus(e.getEventTime());
				}
				return true;
			}
	  }
		return super.dispatchKeyEvent(e);
	}
	
  /** Notifies the model every so often to update the amount of time elapsed.*/
  /* This shouldn't interfere with hit detection, because at most you'll have
   * one timer task posted to the handler at any given time.
   * In a mini-benchmark run on 5/24/11 on the emulator, it took 0-3ms from
   * onTouch() to TestUiModel.fairHit() with or without this timer running.*/
  private class CountUpTimer implements Runnable {
    @Override
    public void run() {
      uiModel.countupTimerTick();
      // if we're still showing the dot, do it again.
      if (PVTState.SHOW_STIMULUS.equals(uiModel.getCurrentState())) {
        handler.post(this);
      }
    }
  }

  /**
   * Called when a reaction to the stimulus is received
   * @param reactionReceivedTime	The time at which the reaction was received (as returned by {@link SystemClock#uptimeMillis()}).
   */
  private void reactToStimulus(long reactionReceivedTime) {
  	//TODO: this really should be a part of the model, as should all the timing stuff.
    switch (uiModel.getCurrentState()) {
    case FOREPERIOD: // user responded before stimulus shown, transition to FALSE_START
			removeHandlerCallbacks();
      uiModel.falseStart(reactionReceivedTime);
      handler.postDelayed(mPVTRun, test.restartDelay);
      break;
    case SHOW_STIMULUS: // fair hit
			removeHandlerCallbacks();
      uiModel.fairHit(reactionReceivedTime);
      handler.postDelayed(mPVTRun, test.restartDelay);
      break;
    default:
      break;
    }
  }

  /**
   * Remove pending callbacks from {@link #handler}. Removes all callbacks
   * used to schedule future events, including {@link #mPVTRun} and {@link #countUpTimer}.
   * Also stops the reminder tone if it is playing.
   */
  private void removeHandlerCallbacks() {
		handler.removeCallbacks(mPVTRun);
		handler.removeCallbacks(countUpTimer);
		handler.removeCallbacks(reminderTimer);
		handler.removeCallbacks(deadlineTimer);
  	reminderTone.stop();	//TODO: move this somewhere better	
  }
  
  /**
   * Main class to facilitate a test
   *
   * @author Jonathan Schooler
   */
  private class PVTRun implements Runnable {
		/** Consider this and the buttonClick method the MVC "controller" */
		@Override
		public void run() {
			// step the UI Model. This will update the view too.
			uiModel.step();

			//clear any pending future events tied to our current state
			removeHandlerCallbacks();

			// Set timers as needed to step again.
			switch (uiModel.getCurrentState()) {
			case START: 
				break;
			case COUNTDOWN3:
				handler.postDelayed(mPVTRun, 1000);
				break;
			case COUNTDOWN2:
				handler.postDelayed(mPVTRun, 1000);
				break;
			case COUNTDOWN1:
				handler.postDelayed(mPVTRun, 1000);
				break;
			case FOREPERIOD: // user waits for stimulus
				stimulusDelay = uiModel.getCurrentTrial().length;
				handler.postDelayed(mPVTRun, stimulusDelay);
				t_0 = System.nanoTime();
				Log.d(TAG, T0 + t_0 + D + stimulusDelay + ZEROS);
				break;
			case SHOW_STIMULUS: // start the count-up timer, if it's enabled
				if (test.countUpTimerEnabled) {
					handler.post(countUpTimer);
				}
				if (AUTOMATED) {
					reactToStimulus(SystemClock.uptimeMillis());
				}
				// start the reminder timer to play a reminder tone if the user does not respond quickly enough
				handler.postDelayed(reminderTimer, test.reminderDelay);
				// start the deadline timer stop this trial if the user does not respond quickly enough
				handler.postDelayed(deadlineTimer, test.deadline);
				break;
			case FALSE_START: 
				break;
			case DEADLINE:
				break;
			case SHOW_REACTION: // when response is detected
				handler.postDelayed(mPVTRun, test.restartDelay);
				break;
			case TEST_COMPLETE:
				handler.postDelayed(mPVTRun, test.gameOverDelay);
				break;
			case CLEANUP_AND_FINISH:
				//return the resulting test to the activity that called us
				Log.d(TAG, "Test complete");

				Intent i = getIntent();
				i.putExtra(TEST, (Serializable) test);
				setResult(RESULT_OK, i);
				finish();
				Intent timeSubmitted = new Intent(RunTest.this, PAMActivity.class);
				RunTest.this.startActivity(timeSubmitted);
				break;
			case CANCEL:
			case DONE:
			default: 
				break;
			}
		}
	}

  /** Update the UI when the model changes. Consider this the MVC "view".*/
  @Override
  public void update(Observable observable, Object data) {
  	if (observable != uiModel) {
  		return; // we should only be observing the UI Model.
  	}

  	switch(uiModel.getCurrentState()) {
  	case START: 
  		break;
  	case COUNTDOWN3:
  		readyText.setVisibility(View.VISIBLE);
  		readyText.setText(R.string.countdown3);
  		dot.color = NEUTRAL_COLOR;
  		dot.draw();
  		timeText.setVisibility(View.INVISIBLE);
  		break;
  	case COUNTDOWN2:
  		readyText.setVisibility(View.VISIBLE);
  		readyText.setText(R.string.countdown2);
  		dot.color = NEUTRAL_COLOR;
  		dot.draw();
  		timeText.setVisibility(View.INVISIBLE);
  		break;
  	case COUNTDOWN1:
  		readyText.setVisibility(View.VISIBLE);
  		readyText.setText(R.string.countdown1);
  		dot.color = NEUTRAL_COLOR;
  		dot.draw();
  		timeText.setVisibility(View.INVISIBLE);
  		break;
  	case FOREPERIOD:
  		numText.setText(TRIAL + uiModel.getCurrentTrial().trialNum);
  		readyText.setVisibility(View.INVISIBLE);
  		dot.color = NEUTRAL_COLOR;
  		dot.draw();
  		timeText.setVisibility(View.INVISIBLE);
  		break;
  	case SHOW_STIMULUS: // show stimulus and begin counting
  		if (test.countUpTimerEnabled) {
  			timeText.setText(uiModel.getElapsedTime() + MS);
  			timeText.setVisibility(View.VISIBLE);
  		} else {
  			timeText.setVisibility(View.INVISIBLE);
  		}

  		
  		long t_1 = System.nanoTime();		//TODO: remove these timings
  		dot.color = STIMULUS_COLOR;
  		dot.measure = true;	//tell imageview to measure timings the next time it is drawn
  		dot.draw();
  		uiModel.foreperiodEnded();

  		long measuredDelay = t_1 - t_0;
  		long epsilon = measuredDelay - stimulusDelay * 1000000;
  		Log.d(TAG, T1 + t_1 + MEASURED_D + measuredDelay + EPSILON + epsilon);

  		measuredDelay = t_2 - t_0;
  		epsilon = measuredDelay - stimulusDelay * 1000000;
  		Log.d(TAG, T2 + t_2 + MEASURED_D + measuredDelay + EPSILON + epsilon);

  		readyText.setVisibility(View.INVISIBLE);
  		break;
  	case FALSE_START:
  		timeText.setVisibility(View.VISIBLE);
  		readyText.setVisibility(View.INVISIBLE);
  		timeText.setText(TOO_EARLY);
  		break;
  	case DEADLINE:
  		timeText.setVisibility(View.VISIBLE);
  		readyText.setVisibility(View.INVISIBLE);
  		timeText.setText("Timeout. Please try again.");
  		break;
  	case SHOW_REACTION: // when response is detected
  		timeText.setVisibility(View.VISIBLE);
  		readyText.setVisibility(View.INVISIBLE);
  		if (test.visibleResponseTimeEnabled) {	//provide visual feedback of response time
  			timeText.setText(uiModel.getCurrentTrial().score + MS);
  		}
  		else {
  			timeText.setText("");
  		}
  		if (test.responseFeedbackEnabled) {			//provide visual feedback on success
    		dot.color = RESPONSE_FEEDBACK_COLOR;
    		dot.draw();
  		}
  		break;
  	case TEST_COMPLETE:
  		readyText.setVisibility(View.VISIBLE);
  		readyText.setText(R.string.game_over);
  		dot.color = NEUTRAL_COLOR;
  		timeText.setVisibility(View.INVISIBLE);
  		break;
  	case CLEANUP_AND_FINISH:
  		break;
  	case CANCEL:
			//return a cancelled result to the activity that called us
			Log.d(TAG, "Test canceled");
			setResult(RESULT_CANCELED);
			finish();
			break;
  	case DONE:
  	default:
  	}
  }

  @Override
  protected void onResume() {
  	super.onResume();
  	
  	wl.acquire();
  }
  
	@Override
	public void onPause() {
		super.onPause();
		
		//pause during the test -> cancel the test
		uiModel.cancel();

		//stop any active reminder tone
		reminderTone.stop();

		wl.release();
  }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		reminderTone.release();
	}
}