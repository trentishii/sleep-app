package com.example.trent.sleepapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Trent on 3/7/2017.
 */

public class AlarmReceiver extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "BaroLoggerService";

    private SensorManager sensorManager = null;
    private Sensor sensorAcc = null;
    private Sensor sensorLight = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "Sensor Service Started");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        sensorManager.registerListener(this, sensorAcc,
//                SensorManager.SENSOR_DELAY_NORMAL);
//
//        sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("AlarmReceiver", "Sensor Changed");

        // grab the values and timestamp -- off the main thread
        new AlarmReceiver.SensorEventLoggerTask().execute(event);
        // stop the service
        stopSelf();
        Log.d("AlarmReceiver", "Service Stopped");
        sensorManager.unregisterListener(this);
    }

    private class SensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent accEvent = events[0];
            float accX = accEvent.values[0];
            float accY = accEvent.values[1];
            float accZ = accEvent.values[2];
//            SensorEvent lightEvent = events[1];
//            float lux = lightEvent.values[0];
            // log the value
            Log.d("Accelerometer Values", accX + " " + accY + " " + accZ);
//            Log.d("Light Brightness", lux + " lux");
            return null;
        }
    }


}
