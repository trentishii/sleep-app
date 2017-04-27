package com.example.trent.sleepapp;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import java.util.Date;

/**
 * Created by Trent on 4/26/2017.
 */

public class JournalEvent {
    public String type;
    public String date;
    public String time;
    public int hour;
    public int minute;

    public JournalEvent() {

    }

    public JournalEvent(String type, int hour, int minute) {
        this.type = type;
        this.hour = hour;
        this.minute = minute;
    }
}
