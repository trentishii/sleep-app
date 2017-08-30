package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class SSSActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView pbText;
    private RelativeLayout pbView;
    private FirebaseUser user;
    private ArrayList<Button> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sss);
//        pbView = (RelativeLayout) findViewById(R.id.sssProg);
//        pb = (ProgressBar) pbView.findViewById(R.id.progressBar);
//        pbText = (TextView) pbView.findViewById(R.id.tvProgress);
//        pb.setProgress(3);
//        pbText.setText("You have completed 3 of 7 tests");

        buttons = new ArrayList<>();
        buttons.add((Button) findViewById(R.id.scale_1));
        buttons.add((Button) findViewById(R.id.scale_2));
        buttons.add((Button) findViewById(R.id.scale_3));
        buttons.add((Button) findViewById(R.id.scale_4));
        buttons.add((Button) findViewById(R.id.scale_5));
        buttons.add((Button) findViewById(R.id.scale_6));
        buttons.add((Button) findViewById(R.id.scale_7));


//        Button btn1 = (Button)findViewById(R.id.scale_1);
        user = FirebaseAuth.getInstance().getCurrentUser();
        for (int i = 0; i <= 6; i++) {
            final int event_idx = i+1;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    SSSEvent event = new SSSEvent(event_idx);
                    myRef.child("SSS").child(currentDate).setValue(event);

                    SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = buttonPrefs.edit();

                    String noon = "12:00:00";
                    String evening = "14:38:00";
                    String bedtime = "20:00:00";
                    String pattern = "HH:mm:ss";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    try {
                        if (dateFormat.parse(dateString[3]).before(dateFormat.parse(noon))) {
                            editor.putBoolean("bSSS", false);
                            editor.putBoolean("WakeTimeDone", true);
                            editor.commit();
                        } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(noon)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(evening))) {
                            editor.putBoolean("b2SSS", false);
                            editor.putBoolean("DayTime1Done", true);
                            editor.commit();
                        } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(evening)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(bedtime))) {
//                            editor.putBoolean("b3SSS", false);
                            editor.putBoolean("DayTime2Done", true);
                            editor.commit();
                        } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(bedtime))) {
                            editor.putBoolean("b4SSS", false);
                            editor.putBoolean("SleepTimeDone", true);
                            editor.commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
