package com.example.trent.sleepapp.pvt;

import android.os.SystemClock;

import java.util.Random;

/**
 * Creates new Trial objects. Two reasons:
 * 1. You can mock it out for tests.
 * 2. You will never have an uninitialized Trial object.
 *
 * @author Dan Tasse
 */
public class TrialFactory {

  private Test test;
  private Random random;

  /**
   * @param test The Test that this Trial is part of.
   * @param random Any java.util.Random object. Just make a new one.
   */
  public TrialFactory(
      Test test,
      Random random) {
    this.test = test;
    this.random = random;
  }

  public Trial newTrial(int newTrialNumber) {
    Trial trial = new Trial();
    trial.subjectId = test.subjectId;
    trial.testTag = test.testTag;
    trial.timeStamp = System.currentTimeMillis();
    trial.trialNum = newTrialNumber;
    // wait a random amount of time
    int waitTime = Math.max(0,
        random.nextInt(test.maxDelay - test.minDelay) + test.minDelay);
    trial.length = waitTime;
    trial.foreperiodStart = SystemClock.uptimeMillis();
    return trial;
  }
}
