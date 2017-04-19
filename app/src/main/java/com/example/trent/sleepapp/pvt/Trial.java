package com.example.trent.sleepapp.pvt;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to store PVT trial data. You can instantiate one directly, but the
 * best way to get a new Trial is via a TrialFactory.
 *
 * @author Jonathan Schooler
 */
public class Trial implements Serializable, Comparable<Trial> {
	/** Strings allocated in beginning to prevent garbage collection. */
	private static final String TIMESTAMP = "timeStamp:";
	private static final String TRIALNUM = " trialNum:";
	private static final String TEST = " test:";
	private static final String CONFIGVER = " configVer:";
	private static final String LENGTH = " length:";
	private static final String ACTUAL_LENGTH = " actual length:";
	private static final String SCORE = " score:";
	private static final String REPORTED = " reported:";
	private static final String ID = " ID";
  private static final String DATE_FORMAT = "yyyy-MM-dd_kk.mm.ss";		//modified ISO date format
	private static final String VARSIGN = "$";
	private static final String COMMA = ",";
	private static final String BLANK = "";
	private static final String CSV_HEADER = 
		"timestamp,foreperiod_start,foreperiod_end,response_received,subject,trial,test,requested_foreperiod,actual_foreperiod,response_time,note,tag,garbage_collection";

  // auto-generated, must match server's Trial class ID
  private static final long serialVersionUID = -1844972824460195943L;

  public enum Result {
    /** Default type. Indicates the user reacted relatively quickly. */
    success,
    /** The user reacted before the stimulus appeared. */
    false_start,
    /** The user reacted so soon after the stimulus appeared, there's no
     * way he/she could have actually done it so quickly. He/she must have
     * been anticipating the stimulus and got lucky. */
    anticipate,
    /** The user was pretty slow. (traditionally >500ms) */
    minor_lapse,
    /** The user was very slow and the reminder was displayed. (traditionally >3 sec) */
    reminder,
    /** The user was very slow and the trial timed out (passed the deadline) */
    deadline,
  }

  public String id;
  public int subjectId;
  public String testTag;
  public int trialNum;
  /** Which test this trial is a part of */
  public int testNum;
  public int configVer;
  /** When the trial started, approximately, in ms since epoch. Useful only for human-readable things, NOT accurate timing */
  public long timeStamp;
  /** When the foreperiod started, according to SystemClock.uptimeMillis() */
  public long foreperiodStart;
  /** When the foreperiod actually ended, according to SystemClock.uptimeMillis(). (i.e. when the stimulus was drawn) */
  public long foreperiodEnd;
  /** When the response was received, according to SystemClock.uptimeMillis(). */
  public long responseReceived;
  /** Desired amount of time the user will wait (in ms) before the stimulus appears. (actual amount is foreperiodEnd - foreperiodStart; see below) */
  public int length;
  /** Actual length of time the user waited (in ms) (i.e. actual foreperiod: foreperiodEnd - foreperiodStart) */
  public int actualLength;
  /** Time (ms) between when the stimulus appeared and the user reacted (responseReceived - foreperiodEnd). */
  public int score;
  /** What type of hit it was: false start, success, etc. */
  public Result result = Result.success;
  public boolean reported;
  /** True if a garbage collection event may have occurred during this trial */
  public boolean garbageCollection = false;

  @Override
  public String toString() {
    return TIMESTAMP + timeStamp + TRIALNUM + trialNum + TEST
        + testNum + CONFIGVER + configVer + LENGTH + length
        + ACTUAL_LENGTH + actualLength + SCORE
        + score + REPORTED + reported + ID + id;
  }

  /**
   * @return this trial's data as a parse-able string to be sent as POST
   *         information
   */
	@SuppressLint("SimpleDateFormat")	//ignore locale warning on SimpleDateFormat; we are using the ISO format
  public String toPostLine() { //TODO: is this even used? update it or remove it
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    CharSequence timeString = formatter.format(new Date(timeStamp));
    return timeString + VARSIGN + trialNum + VARSIGN + testNum + VARSIGN + configVer
    	+ VARSIGN + length + VARSIGN + score + VARSIGN + id;
  }

  /**
   * @return this trial's data as a CSV line
   */
	@SuppressLint("SimpleDateFormat")	//ignore locale warning on SimpleDateFormat; we are using the ISO format
  public String toCSVLine() {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    CharSequence timeString = formatter.format(new Date(timeStamp));
    return timeString
        + COMMA
        + foreperiodStart
        + COMMA
        + foreperiodEnd
        + COMMA
        + responseReceived
        + COMMA
        + subjectId
        + COMMA
        + trialNum
        + COMMA
        + testNum
        + COMMA
        + length
        + COMMA
        + actualLength
        + COMMA
        + score
        + COMMA
        + (result != Result.success ? result.toString() : BLANK)
        + COMMA
        + testTag
        + COMMA
        + garbageCollection
        ;
  }

  /**
   * @return the header row for the CSV file
   */
  public static String headerCSV() {
    return CSV_HEADER;
  }

  @Override
  public int compareTo(Trial other) {
    return (int) (this.timeStamp - other.timeStamp);
  }
}
