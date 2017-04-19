package com.example.trent.sleepapp.pvt;

/**
 * Various types of per-test statistics we want to output.
 *
 * @author Dan Tasse
 */

public enum SummaryStat {

	/** AKA Test ID. */
	SessionID,
	/** The subject's name. Currently always "Default User." TODO: fix this. */
	SubjectID,
	/** One batch is a series of tests all performed by the same subject. */
	BatchID,
	/** Free-form string tag applied to a test */
	TestTag,
	/** R or L */
	SubjectHand,
	/** Scheduled length in seconds. */
	LengthSeconds,
	/** Scheduled number of trials. The test will go on until either
	 * |LengthSeconds| seconds pass or |LengthTrials| trials happen. */
	LengthTrials,
	/** Min time between trials in seconds */
	FPMin,
	/** Max time between trials in seconds */
	FPMax,
	//  /** Step between possible time-between-trial values, I think? */
	//  FPStep,
	//  TODO: figure out if FPStep is required.
	/** If you react before this time (in ms), it's counted as an "anticipate"
	 * (too fast). */
	Anticipation,
	/** In the format MM/dd/yyyy hh:mm:ss aa, e.g. 06/17/2011 08:40:49 PM */
	StartTimeStamp,
	/** In the format MM/dd/yyyy hh:mm:ss aa, e.g. 06/17/2011 08:40:49 PM */
	FinishTimeStamp,
	/** How was the session ended? TODO: implement this. */
	SessionTermination,
	/** Rs = Trials. Returns the number of trials. */
	TotalRs,
	/** Valid = success or minor lapse */
	ValidRs,
	/** Invalid = anticipate or false start */
	InvalidRs,
	/** Mean response time in ms */
	MeanRT,
	/** Standard deviation of response time in ms */
	StdDevRT,
	/** Median response time in ms */
	MedianRT,
	/** Minimum response time in ms */
	MinimumRT,
	/** Maximum response time in ms */
	MaximumRT,
	/** Number of times the user anticipated. */
	TotalAnticipations,
	Reminders,
	MinorLapses,
	GarbageCollections,
	// %RTs>2*MedianRT renamed to PercentRTsOver2TimesMedianRT
	PercentRTsOver2TimesMedianRT;

	static String headerCSV() {
		String csv = "";
		SummaryStat[] values = values();
		for (int i = 0; i < values.length; i++) {
			csv += values[i].toString();
			if (i < values.length - 1) {
				csv += ",";
			}
		}
		return csv;
	}
}
