//package com.example.trent.sleepapp;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//
//import static android.content.Context.NOTIFICATION_SERVICE;
//
///**
// * Created by vinisha on 12/5/2017.
// */
//
//public class MyReceiver extends BroadcastReceiver {
//    public MyReceiver() {
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        System.out.println("##############################Pending Intent set");
//        Intent intent1 = new Intent(context, MyNewIntentService.class);
//        context.startService(intent1);
//
//    }
//}
