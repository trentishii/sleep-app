package com.example.trent.sleepapp;

/**
 * Created by vinisha on 7/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trent.sleepapp.database.FileManipulation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

//package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trent.sleepapp.database.FileManipulation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.util.Date;

public class SleepEventLog extends AppCompatActivity {
    private TimePicker timePicker;
    private Button submit;
    SharedPreferences sharedPrefs;
    public static final String PREFNAME = "userPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_log);
        timePicker = (TimePicker) findViewById(R.id.timePickerTobacco);
        submit = (Button) findViewById(R.id.bSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int hour = timePicker.getHour();
//                int min = timePicker.getMinute();
//                FileManipulation.createDateFolder(t);
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("Journal");
//                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
//                Calendar c = Calendar.getInstance(tz);
//                Date d = c.getTime();
//                String[] dateString = d.toString().split(" ");
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH) + 1;
//                int date = c.get(Calendar.DATE);
//                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
//                JournalEvent jv = new JournalEvent(EVENT_NAMES[eventIndex], hour, min);
//                myRef.child(currentDate).setValue(jv);

//                SleepEvent se = new SleepEvent(EVENT_NAMES[eventIndex], hour, min);
//                myRef.child(currentDate).setValue(se);

//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.remove("EventType");
//                editor.commit();
                Intent timeSubmitted = new Intent(SleepEventLog.this, SleepLogActivity.class);
                SleepEventLog.this.startActivity(timeSubmitted);
            }
        });

    }
}

