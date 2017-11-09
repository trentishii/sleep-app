package com.example.trent.sleepapp;

/**
 * Created by vinisha on 7/14/2017.
 */

public class SleepEvent {
    public String type;
    public String date;
    public String time;
    public int hour;
    public int minute;



    public SleepEvent(String type, int hour, int minute) {
        this.type = type;
        this.hour = hour;
        this.minute = minute;
    }
}