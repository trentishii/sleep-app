package com.example.trent.sleepapp;

/**
 * Created by vinisha on 7/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trent.sleepapp.database.FileManipulation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.Date;
import static java.util.TimeZone.*;

//package com.example.trent.sleepapp;


import org.json.JSONException;


public class SleepEventLog extends AppCompatActivity {
    private TimePicker timePicker;
    private Button submit;
    private TextView question;
    SharedPreferences sleepsharedPrefs;
    private final static String[] EVENT_NAMES = {"sleep", "wake"};
    private final static String[] EVENT_PHRASES = {" sleep?", " wake up?"};
    public static final String PREFNAME = "userPrefs";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_log);
        timePicker = (TimePicker) findViewById(R.id.timePicker3);
        submit = (Button) findViewById(R.id.button4);
        question = (TextView) findViewById(R.id.textView68);
        final Context t = this;
        sleepsharedPrefs = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        final int eventIndex = sleepsharedPrefs.getInt("EventType", -1);
        question.append(EVENT_PHRASES[eventIndex]);
        user = FirebaseAuth.getInstance().getCurrentUser();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, min;
                if (Build.VERSION.SDK_INT >= 23 ) {
                    hour = timePicker.getHour();
                    min = timePicker.getMinute();
                }else{
                    hour = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }
                FileManipulation.createDateFolder(t);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String name = user.getEmail();
                String[] newName = name.split("@");
                DatabaseReference myRef = database.getReference(newName[0]);
                java.util.TimeZone tz = getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SleepEvent se = new SleepEvent(EVENT_NAMES[eventIndex], hour, min);
                myRef.child("Sleep Log").child(currentDate).setValue(se);
                SharedPreferences.Editor editor = sleepsharedPrefs.edit();
                editor.remove("EventType");
                editor.commit();
                Intent timeSubmitted = new Intent(SleepEventLog.this, SleepLogActivity.class);
                SleepEventLog.this.startActivity(timeSubmitted);
            }
        });

    }
}

