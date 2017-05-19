package com.example.trent.sleepapp;

/**
 * Created by Trent on 5/18/2017.
 */

public class UserSchedule {
    public int wakeHour;
    public int wakeMin;
    public int sleepHour;
    public int sleepMin;

    public UserSchedule() {

    }

    public UserSchedule(int wakeHour, int wakeMin, int sleepHour, int sleepMin) {
        this.wakeHour = wakeHour;
        this.wakeMin = wakeMin;
        this.sleepHour = sleepHour;
        this.sleepMin = sleepMin;
    }

}
