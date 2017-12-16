package com.example.trent.sleepapp;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by vinisha on 12/5/2017.
 */

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Sleep Quality Study");
        builder.setContentText("Please take available tests within the next 4 hour window");
        builder.setSmallIcon(R.drawable.sleep);
        builder.setPriority(Notification.PRIORITY_MAX);
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Pending Intent set");
        Intent notifyIntent = new Intent(this, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        builder.setAutoCancel(true);
//        builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
//        SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
//        if (!buttonPrefs.getBoolean("DayTime1Done",false)){
//            managerCompat.cancel(NOTIFICATION_ID);}
//        else {
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%% Notification Alive  %%%%%%%%%%%%%");
//        }
        }

    }

