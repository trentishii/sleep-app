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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.TimeZone.*;

public class PanasActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView pbText;
    private RelativeLayout pbView;
    private FirebaseUser user;
    private EditText ent1;
    private EditText ent2;
    private EditText ent3;
    private EditText ent4;
    private EditText ent5;
    private EditText ent6;
    private EditText ent7;
    private EditText ent8;
    private EditText ent9;
    private EditText ent10;
    private EditText ent11;
    private EditText ent12;
    private EditText ent13;
    private EditText ent14;
    private EditText ent15;
    private EditText ent16;
    private EditText ent17;
    private EditText ent18;
    private EditText ent19;
    private EditText ent20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panas);

//        pbView = (RelativeLayout) findViewById(R.id.panasProg);
//        pb = (ProgressBar) pbView.findViewById(R.id.progressBar);
//        pbText = (TextView) pbView.findViewById(R.id.tvProgress);
//        pb.setProgress(4);
//        pbText.setText("You have completed 4 of 7 tests");
        ent1 = (EditText) findViewById(R.id.editTextint);
        ent2 = (EditText) findViewById(R.id.editTextdis);
        ent3 = (EditText) findViewById(R.id.editTextexc);
        ent4 = (EditText) findViewById(R.id.editTextups);
        ent5 = (EditText) findViewById(R.id.editTextstr);
        ent6 = (EditText) findViewById(R.id.editTextgui);
        ent7 = (EditText) findViewById(R.id.editTextsca);
        ent8 = (EditText) findViewById(R.id.editTexthos);
        ent9 = (EditText) findViewById(R.id.editTextent);
        ent10 = (EditText) findViewById(R.id.editTextpro);
        ent11 = (EditText) findViewById(R.id.editTextirr);
        ent12 = (EditText) findViewById(R.id.editTextale);
        ent13 = (EditText) findViewById(R.id.editTextash);
        ent14 = (EditText) findViewById(R.id.editTextins);
        ent15 = (EditText) findViewById(R.id.editTextner);
        ent16 = (EditText) findViewById(R.id.editTextdet);
        ent17 = (EditText) findViewById(R.id.editTextatt);
        ent18 = (EditText) findViewById(R.id.editTextjit);
        ent19 = (EditText) findViewById(R.id.editTextact);
        ent20 = (EditText) findViewById(R.id.editTextafr);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Button btn3 = (Button)findViewById(R.id.button2);
        btn3.setOnClickListener(new View.OnClickListener() {
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
                PanasEvent event = new PanasEvent(ent1.getText().toString(), ent2.getText().toString(), ent3.getText().toString(), ent4.getText().toString(), ent5.getText().toString(), ent6.getText().toString(), ent7.getText().toString(), ent8.getText().toString(), ent9.getText().toString(), ent10.getText().toString(),ent11.getText().toString(),ent12.getText().toString(),ent13.getText().toString(),ent14.getText().toString(),ent15.getText().toString(),ent16.getText().toString(),ent17.getText().toString(),ent18.getText().toString(),ent19.getText().toString(),ent20.getText().toString());
                myRef.child("PANAS").child(currentDate).setValue(event);

                SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = buttonPrefs.edit();

                String bedtime = "20:00:00";
                String pattern = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    if (dateFormat.parse(dateString[3]).after(dateFormat.parse(bedtime))) {
                        editor.putBoolean("bPANAS", false);
                        editor.putBoolean("PANASDone" , true);
                        editor.putBoolean("SleepTimeDone", true);
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
