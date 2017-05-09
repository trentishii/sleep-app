package com.example.trent.sleepapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SSSActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView pbText;
    private RelativeLayout pbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sss);
        pbView = (RelativeLayout) findViewById(R.id.sssProg);
        pb = (ProgressBar) pbView.findViewById(R.id.progressBar);
        pbText = (TextView) pbView.findViewById(R.id.tvProgress);
        pb.setProgress(3);
        pbText.setText("You have completed 3 of 7 tests");
        Button btn1 = (Button)findViewById(R.id.scale_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(1);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });

        Button btn2 = (Button)findViewById(R.id.scale_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(2);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });

        Button btn3 = (Button)findViewById(R.id.scale_3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(3);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });

        Button btn4 = (Button)findViewById(R.id.scale_4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(4);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });

        Button btn5 = (Button)findViewById(R.id.scale_5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(5);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });

        Button btn6 = (Button)findViewById(R.id.scale_6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(6);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });

        Button btn7 = (Button)findViewById(R.id.scale_7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SSS");
                TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                Calendar c = Calendar.getInstance(tz);
                Date d = c.getTime();
                String[] dateString = d.toString().split(" ");
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                String currentDate = month + "-" + date + "-" + year + ":" + dateString[3];
                SSSEvent event = new SSSEvent(7);
                myRef.child(currentDate).setValue(event);
                Intent intent = new Intent(getApplicationContext(), PanasActivity.class);
                startActivity(intent);
            }
        });
    }
}
