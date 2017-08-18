package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trent.sleepapp.database.FileManipulation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventLog extends AppCompatActivity {
    private TimePicker timePicker;
    private Button submit;
    private TextView question;
    private final static String[] EVENT_NAMES = {"tobacco", "coffee", "exercise", "meal", "alcohol", "medicine"};
    private final static String[] EVENT_PHRASES = {"smoke?", "have coffee?", "exercise?", "eat?", "have an alcoholic beverage?", "have medicine?"};
    SharedPreferences sharedPrefs;
    public static final String PREFNAME = "userPrefs";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventLog_timePicker);
        timePicker = (TimePicker) findViewById(R.id.timePickerTobacco);
        submit = (Button) findViewById(R.id.bSubmit);
        question = (TextView) findViewById(R.id.tvQuestion);
        final Context t = this;
        sharedPrefs = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        final int eventIndex = sharedPrefs.getInt("EventType", -1);
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
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                JournalEvent jv = new JournalEvent(EVENT_NAMES[eventIndex], hour, min);
                myRef.child("Journal").child(currentDate).setValue(jv);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.remove("EventType");
                editor.commit();
                Intent timeSubmitted = new Intent(EventLog.this, JournalActivity.class);
                EventLog.this.startActivity(timeSubmitted);
            }
        });

    }
}
