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
        PendingIntent pendingIntent = intent.getParcelableExtra("key");
        PendingIntent other = intent.getParcelableExtra("light");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        am.cancel(other);
        Log.d("Service Cancel", "Alarm manager services cancelled");
    }
}
