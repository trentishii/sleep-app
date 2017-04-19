package com.example.trent.sleepapp.pvt;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates summary statistics for one Test.
 *
 * @author Dan Tasse
 */
@SuppressLint("SimpleDateFormat")	//ignore locale warning on SimpleDateFormat; we are using the ISO format
public class SummaryCalculator {

  private Test test;

  private static final String DATE_FORMAT = "yyyy-MM-dd_kk.mm.ss";		//modified ISO date format
  private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

  public SummaryCalculator(Test test) {
    this.test = test;
  }

  public Map<SummaryStat, String> calculateSummaryStats() {
    Map<SummaryStat, String> summaryStats = new HashMap<SummaryStat, String>();
    summaryStats.put(SummaryStat.SessionID, Integer.toString(test.testNum));
    summaryStats.put(SummaryStat.SubjectID, Integer.toString(test.subjectId));
    summaryStats.put(SummaryStat.BatchID, Integer.toString(test.batch));
    summaryStats.put(SummaryStat.TestTag, test.testTag);
    summaryStats.put(SummaryStat.SubjectHand, test.subjectHand);
    summaryStats.put(SummaryStat.LengthSeconds,
        Double.toString(test.duration / 1000.0));
    summaryStats.put(SummaryStat.LengthTrials,
        Integer.toString(test.lengthTrials));
    summaryStats.put(SummaryStat.FPMin, Double.toString(test.minDelay / 1000.0));
    summaryStats.put(SummaryStat.FPMax, Double.toString(test.maxDelay / 1000.0));
    summaryStats.put(SummaryStat.Anticipation,
        Integer.toString(test.anticipateDelay));
    summaryStats.put(SummaryStat.StartTimeStamp,
        dateFormat.format(new Date(test.startTimestamp)));
    summaryStats.put(SummaryStat.FinishTimeStamp,
        dateFormat.format(new Date(test.finishTimestamp)));
    summaryStats.put(SummaryStat.TotalRs, Integer.toString(test.trials.size()));

    summaryStats.putAll(calculateNumOfEachResult());
    summaryStats.putAll(calculateRtStats());

    return summaryStats;
  }

  Map<SummaryStat, String> calculateNumOfEachResult() {
    int minorLapses = 0;
    int reminders = 0;
    int anticipates = 0;
    int validRs = 0;
    int invalidRs = 0;
    //TODO: add deadline count
    int garbageCollections = 0;
    for (Trial trial : test.trials) {
      switch(trial.result) {
        case success: validRs++; break;
        case minor_lapse: validRs++; minorLapses++; break;
        case reminder: validRs++; reminders++; break;
        case anticipate: invalidRs++; anticipates++; break;
        case false_start: invalidRs++; break;
      }
      if (trial.garbageCollection) {
      	garbageCollections++;
      }
    }

    Map<SummaryStat, String> numOfEachResult = new HashMap<SummaryStat, String>();
    numOfEachResult.put(SummaryStat.MinorLapses, Integer.toString(minorLapses));
    numOfEachResult.put(SummaryStat.Reminders, Integer.toString(reminders));
    numOfEachResult.put(SummaryStat.TotalAnticipations, Integer.toString(anticipates));
    numOfEachResult.put(SummaryStat.ValidRs, Integer.toString(validRs));
    numOfEachResult.put(SummaryStat.InvalidRs, Integer.toString(invalidRs));
    numOfEachResult.put(SummaryStat.GarbageCollections, Integer.toString(garbageCollections));
    //TODO: add deadline count
    return numOfEachResult;
  }

  /** Calculates mean, standard deviation, etc for response times. */
  /* visible for testing */ Map<SummaryStat, String> calculateRtStats() {
    Map<SummaryStat, String> rtStats = new HashMap<SummaryStat, String>();
    int min = Integer.MAX_VALUE;
    int max = 0;
    List<Double> scores = new ArrayList<Double>();
    for(int i = 0; i < test.trials.size(); i++) {
      Trial trial = test.trials.get(i);
      if (trial.score < min && trial.score > 0) { // ignore false starts
        min = trial.score;
      }
      if (trial.score > max) {
        max = trial.score;
      }
      // ignore false start negative scores for computing everything.
      if (trial.score > 0) {
        scores.add((double) trial.score);
      }
    }
    rtStats.put(SummaryStat.MeanRT, Double.toString(mean(scores)));
    rtStats.put(SummaryStat.StdDevRT, Double.toString(stDev(scores)));
    rtStats.put(SummaryStat.MedianRT, Double.toString(median(scores)));
    rtStats.put(SummaryStat.MinimumRT, Integer.toString(min));
    rtStats.put(SummaryStat.MaximumRT, Integer.toString(max));
    rtStats.put(SummaryStat.PercentRTsOver2TimesMedianRT,
        Double.toString(percentNumsOver2TimesMedian(scores)));
    return rtStats;
  }

  private double mean(List<Double> numbers) {
    double sum = 0.0;
    for (int i = 0; i < numbers.size(); i++) {
      sum += numbers.get(i);
    }
    return sum / numbers.size();
  }

  private double stDev(List<Double> numbers) {
    double mean = mean(numbers);
    double sumSquaredDiffs = 0.0;
    for (int i = 0; i < numbers.size(); i++) {
      double diff = numbers.get(i) - mean;
      sumSquaredDiffs += (diff * diff);
    }
    sumSquaredDiffs /= numbers.size();
    return Math.sqrt(sumSquaredDiffs);
  }

  private double median(List<Double> numbers) {
    Collections.sort(numbers);
    if (numbers.size() % 2 == 0) {
      int middleIndex = numbers.size() / 2 - 1;
      return (numbers.get(middleIndex) + numbers.get(middleIndex + 1)) / 2;
    } else {
      return numbers.get(numbers.size() / 2);
    }
  }

  /** Returns the number of numbers in the list that are greater than double
   * the median. */
  private double percentNumsOver2TimesMedian(List<Double> numbers) {
    int count = 0;
    double twoTimesMedian = 2 * median(numbers);
    for(int i = 0; i < numbers.size(); i++) {
      if (numbers.get(i) > twoTimesMedian) {
        count++;
      }
    }
    return count * 1.0 / numbers.size();
  }
}
