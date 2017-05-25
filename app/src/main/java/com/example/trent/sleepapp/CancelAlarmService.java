package com.example.trent.sleepapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Trent on 5/19/2017.
 */

public class CancelAlarmService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Service Cancel", "Alarm Cancelled");
        context.stopService(new Intent(context, AccLogService.class));
        Log.d("Service Cancel", "Alarm manager services cancelled");
    }
}
