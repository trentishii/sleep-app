package com.example.trent.sleepapp.pvt;

import android.os.SystemClock;
import android.util.Log;

import com.example.trent.sleepapp.pvt.RunTest.PVTState;
import com.example.trent.sleepapp.pvt.Trial.Result;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Observable;

/**
 * A TestUiModel represents the state of the UI for one Test.
 *
 * @author Dan Tasse
 */
public class TestUiModel extends Observable {
	//string preallocation to reduce GC pressure
	private static final String GC_RAN_AT = "GC ran at ";
	private static final String THROWING_OUT_TRIAL = "; throwing out trial ";

  private PVTState currentState = PVTState.START;

  /** The current trial, or most recent if one isn't going on now. */
  private Trial currentTrial;

  /** Time since the current trial started. */
  private int elapsedTime = 0;

  /** Data model for the test. */
  private Test test;
  private TrialFactory trialFactory;

	//garbage collection detection (based on http://stackoverflow.com/questions/2298784/determine-when-the-android-gc-runs)
	WeakReference<GcWatcher> gcWatcher = new WeakReference<GcWatcher>(new GcWatcher());
	volatile long lastGcTime = 0;		//nonzero if the GC ran
	class GcWatcher {
		@Override
		protected void finalize() throws Throwable {
			//when the garbage collector runs this object should be cleaned up and this method will be called
			//we use this to detect GCs and throw out trials a GC event may have interfered with
			gcWatcher = new WeakReference<GcWatcher>(new GcWatcher());
			lastGcTime = System.currentTimeMillis();
    	Log.d(RunTest.TAG, GC_RAN_AT + lastGcTime);
		}
	}

	public TestUiModel(
      Test test,
      TrialFactory trialFactory) {
    this.test = test;
    this.trialFactory = trialFactory;
  }

  public PVTState getCurrentState() {
    return currentState;
  }

  public List<Trial> getTrials() {
    return test.trials;
  }

  public Trial getCurrentTrial() {
    return currentTrial;
  }
  
  public int getElapsedTime() {
    return elapsedTime;
  }
  
  public void startNewTrial() {
    int newTrialNumber;
    if (currentTrial == null) {
      // first trial. Zero index to match up with the data that'll be written.
      newTrialNumber = 0;
      test.startTimestamp = System.currentTimeMillis();
      test.startTime = SystemClock.uptimeMillis();
    } else {
      newTrialNumber = currentTrial.trialNum + 1;
    }
    currentTrial = trialFactory.newTrial(newTrialNumber);
  }

  /** @return the next state that will happen by default (unless the
   * user false-starts or something).
   *  
   * State diagram is as follows (N.B. much of this state diagram is not controlled 
   * by this method. TODO: consolidate controllers of model state):
   *                   
   *                                            ---> *START              
   *                                                    |              
   *                                                    V              
   *                                               *COUNTDOWN3         
   *                                                    | (1s elapsed) 
   *                                                    V              
   *                                               *COUNTDOWN2         
   *                                                    | (1s elapsed) 
   *                                                    V              
   *                                               *COUNTDOWN1         
   *  - - - - - - - - - - - - - - - - - - - - - - - - - | - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -                                             
   *                                  .--------------.  |          
   *      (test.restartDelay elapsed) |              |  |
   *                                  |              |  | (1s elapsed)
   *                            *FALSE_START         |  |
   *                                  Λ              |  |
   *                   (user reacted) |              V  V
   *                                  '----------- *FOREPERIOD <--------------------------------. 
   *                                                    | (currentTrial.length elapsed)         |
   *                                  .--------------.  |                                       |
   *                                  |              |  |                                       |
   *     (test.reminderDelay elapsed) |              V  V                                       | 
   *                                  '--------- *SHOW_STIMULUS                                 |
   *                                               |         |                                  |
   *                                (user reacted) |         | (test.deadline elapsed)          |
   *                                               V         V                                  |
   *                                    *SHOW_REACTION    *DEADLINE                             |
   *                                               |         |                                  | (test.restartDelay elapsed)
   *                                               '----.----'                                  |
   *                                                    |                                       |
   *                                                    V                                       |
   *                                                    O---------------------------------------'
   *                                                    | (test.restartDelay elapsed AND test.duration has passed)
   *  - - - - - - - - - - - - - - - - - - - - - - - - - | - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -                                             
   *                                                    V                                              
   *           *(test activity onPause)           TEST_COMPLETE
   *                      |                             | (test.gameOverDelay elapsed)
   *                      V                             V
   *                    CANCEL                  CLEANUP_AND_FINISH
   *                      |                             |
   *                      '-----------> DONE <----------'
   *                                    |  Λ
   *                                    '--'
   **/
  private PVTState getNextState() {
    switch(currentState) {
    	case START: return PVTState.COUNTDOWN3;
      case COUNTDOWN3: return PVTState.COUNTDOWN2;
      case COUNTDOWN2: return PVTState.COUNTDOWN1;
      case COUNTDOWN1: return PVTState.FOREPERIOD;
      case FOREPERIOD: return PVTState.SHOW_STIMULUS;
      case FALSE_START: return PVTState.FOREPERIOD;
      case SHOW_STIMULUS: return PVTState.SHOW_REACTION;
      case DEADLINE: return getStateAfterReactionOrDeadline();
      case SHOW_REACTION: return getStateAfterReactionOrDeadline();
      case TEST_COMPLETE: return PVTState.CLEANUP_AND_FINISH;
      case CLEANUP_AND_FINISH:
      case CANCEL:
      case DONE:
      default:
        return PVTState.DONE;
    }
  }
  /**
   * Get the next state after a reaction to stimulus: i.e. the transition out of DEADLINE
   * or SHOW_REACTION.
   */
  private PVTState getStateAfterReactionOrDeadline() {
    if ((int)(SystemClock.uptimeMillis() - test.startTime) > test.duration) {
      return PVTState.TEST_COMPLETE;
    } else {
      return PVTState.FOREPERIOD;
    }  	
  }

  /** Note the time that's elapsed since the test start, and update the UI. */
  public void countupTimerTick() {
    long stimulusStart = currentTrial.foreperiodEnd; //currentTrial.timeStamp + currentTrial.length;
    elapsedTime = 0;//(int)(SystemClock.uptimeMillis() - stimulusStart); TODO: reinstate and fix countup timer
    setChanged();
    notifyObservers();
  }

  /** Increment the current state, assuming nothing unusual happens (like
   * a false-start or a deadline). */
  public void step() {
    currentState = getNextState();
    // start a new trial whenever the user starts waiting.
    if (PVTState.FOREPERIOD.equals(currentState)) {
      startNewTrial();
    }
    setChanged();
    notifyObservers();
  }

  /** 
   * This happens if the user false-starts. 
   * @param reactionReceivedTime	The time at which the user reacted as return by {@link SystemClock#uptimeMillis()}
   */
  public void falseStart(long reactionReceivedTime) {
    currentState = PVTState.FALSE_START;
    long supposedToAppearTime = currentTrial.foreperiodStart + currentTrial.length;
    currentTrial.responseReceived = reactionReceivedTime;
    currentTrial.score = (int) (reactionReceivedTime - supposedToAppearTime);
    currentTrial.result = Result.false_start;
    
    addTrial(currentTrial);
    setChanged();
    notifyObservers();
  }

  /** This happens if the user clicks after the dot has appeared and before
   * the deadline. 
   * @param reactionReceivedTime	The time at which the user reacted as return by {@link SystemClock#uptimeMillis()}
   */
  public void fairHit(long reactionReceivedTime) {
    currentState = PVTState.SHOW_REACTION;
    long appearedTime = currentTrial.foreperiodEnd;
    currentTrial.responseReceived = reactionReceivedTime;
    currentTrial.score = (int) (reactionReceivedTime - appearedTime);
    currentTrial.result = classifyResult(currentTrial.score);

    addTrial(currentTrial);
    setChanged();
    notifyObservers();
  }
  
  /**
   * Indicates that the trial deadline passed (i.e. {@link #test}.{@link Test#deadline} elapsed after a stimulus
   * was shown before the user never reacted to the stimulus). Causes a transition to the {@link PVTState#DEADLINE} state.
   */
  public void deadline(long reactionReceivedTime) {
  	currentState = PVTState.DEADLINE;
    long appearedTime = currentTrial.foreperiodEnd;
    currentTrial.score = (int) (reactionReceivedTime - appearedTime);
    currentTrial.result = Result.deadline;

    addTrial(currentTrial);
    setChanged();
    notifyObservers();
  }
  
  /**
   * Called when the foreperiod has ended (i.e. when the stimulus is shown).
   */
  public void foreperiodEnded() {
  	currentTrial.foreperiodEnd = SystemClock.uptimeMillis();
  	currentTrial.actualLength = (int)(currentTrial.foreperiodEnd - currentTrial.foreperiodStart);
  }
  
  /**
   * Add given trial to the set of completed trials. Drops
   * trial if the garbage collector ran during it.
   * @param trial
   */
  private void addTrial(Trial trial) {
    if (lastGcTime > 0) {
    	Log.d(RunTest.TAG, GC_RAN_AT + lastGcTime + THROWING_OUT_TRIAL + currentTrial.trialNum);
    	trial.garbageCollection = true;
    	lastGcTime = 0;
    }
    test.trials.add(trial);
  }
  
  /**
   * Called to cancel the current test in progress. If the test is running (i.e. the state is
   * anything except {@link PVTState#CANCEL}, {@link PVTState#DONE}, {@link PVTState#TEST_COMPLETE},
   * or {@link PVTState#CLEANUP_AND_FINISH}), changes the state to {@link PVTState#CANCEL} and 
   * notifies any observers of this model of the change. 
   */
  public void cancel() {
		switch (currentState) {
  	case TEST_COMPLETE:
  	case CLEANUP_AND_FINISH:
  	case CANCEL:
  	case DONE:
  		break;
  	default:
    	currentState = PVTState.CANCEL;
    	setChanged();
    	notifyObservers();
		}
  }
  
  // visible for testing.
  Result classifyResult(int score) {
    if (score >= test.reminderDelay) {
      return Result.reminder;
    } else if (score >= test.minorLapseDelay) {
      return Result.minor_lapse;
    } else if (score < test.anticipateDelay) {
      return Result.anticipate;
    } else {
      return Result.success;
    }
  }
}
