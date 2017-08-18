package com.example.trent.sleepapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class AlarmActivity extends AppCompatActivity {
    private TimePicker tpAlarm;
    private Button setButton;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private PendingIntent pendingLight;
    private Recorder audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_timePicker);
        Button setButton = (Button) findViewById(R.id.bSetAlarm);
        final TimePicker tpAlarm = (TimePicker) findViewById(R.id.timePickerAlarm);
        Button startButton = (Button) findViewById(R.id.bStart);
        Button stopButton = (Button) findViewById(R.id.bStop);

        audio = new Recorder(getApplicationContext());

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AlarmManager", "Alarm is set!");
//                int hour = tpAlarm.getHour();
//                int min = tpAlarm.getMinute();
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AccLogService.class);
                pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent lightInt = new Intent(getApplicationContext(), LightLogService.class);
                pendingLight = PendingIntent.getService(getApplicationContext(), 0, lightInt, PendingIntent.FLAG_UPDATE_CURRENT);
                //
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10 * 1000, 15 * 1000, pendingIntent);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 15 * 1000, 15 * 1000, pendingLight);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AlarmManager", "record audio!");
                audio.recordNewAudio();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AlarmManager", "stop audio!");
                audio.stopRecording();
            }
        });
    }
}
