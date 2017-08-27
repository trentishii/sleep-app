package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.trent.sleepapp.pvt.PVTHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static java.util.TimeZone.*;
import java.util.List;
import java.util.TimeZone;

public class LeedsActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView pbText;
    private RelativeLayout pbView;
    private FirebaseUser user;
    private SeekBar sb1;
    private SeekBar sb2;
    private SeekBar sb3;
    private SeekBar sb4;
    private SeekBar sb5;
    private SeekBar sb6;
    private SeekBar sb7;
    private SeekBar sb8;
    private SeekBar sb9;
    private SeekBar sb10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leeds);
//        pbView = (RelativeLayout) findViewById(R.id.leedsProg);
//        pb = (ProgressBar) pbView.findViewById(R.id.progressBar);
//        pbText = (TextView) pbView.findViewById(R.id.tvProgress);
//        pb.setProgress(2);
//        pbText.setText("You have completed 2 of 7 tests");
        sb1 = (SeekBar) findViewById(R.id.seekBar2);
        sb2 = (SeekBar) findViewById(R.id.seekBar3);
        sb3 = (SeekBar) findViewById(R.id.seekBar5);
        sb4 = (SeekBar) findViewById(R.id.seekBar);
        sb5 = (SeekBar) findViewById(R.id.seekBar4);
        sb6 = (SeekBar) findViewById(R.id.seekBar6);
        sb7 = (SeekBar) findViewById(R.id.seekBar7);
        sb8 = (SeekBar) findViewById(R.id.seekBar8);
        sb9 = (SeekBar) findViewById(R.id.seekBar9);
        sb10 = (SeekBar) findViewById(R.id.seekBar10);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Button btn2 = (Button)findViewById(R.id.bSubmit);
        btn2.setOnClickListener(new View.OnClickListener() {
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
                LeedsEvent event = new LeedsEvent(sb1.getProgress(), sb2.getProgress(), sb3.getProgress(), sb4.getProgress(), sb5.getProgress(), sb6.getProgress(), sb7.getProgress(), sb8.getProgress(), sb9.getProgress(), sb10.getProgress());
                myRef.child("Leeds").child(currentDate).setValue(event);

                SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = buttonPrefs.edit();

                String noon = "12:00:00";
                String pattern = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    if (dateFormat.parse(dateString[3]).before(dateFormat.parse(noon))) {
                        editor.putBoolean("bLEEDS", false);
                        editor.putBoolean("LLEDSDone", true);
                        editor.commit();
                    }
                }catch (Exception e) {
                    e.printStackTrace();        }

                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
        });
    }
}
