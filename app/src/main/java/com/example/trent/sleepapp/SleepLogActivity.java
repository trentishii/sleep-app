package com.example.trent.sleepapp;

/**
 * Created by vinisha on 7/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//package com.example.trent.sleepapp;


public class SleepLogActivity extends AppCompatActivity {
    SharedPreferences sleepsharedPrefs;
    public static final String PREFNAME = "userPrefs";
    private SeekBar sb;
    private FirebaseUser user;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_log);
        sleepsharedPrefs = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        final SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = buttonPrefs.edit();

        final ImageButton goToSleepLog = (ImageButton)findViewById(R.id.imageButtonSleep);
        goToSleepLog.setEnabled(buttonPrefs.getBoolean("bSleep", true));
        if (! buttonPrefs.getBoolean("bSleep", true)){
            goToSleepLog.setImageResource(R.drawable.sleep_done);}
        goToSleepLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sleepsharedPrefs, 0);
                Intent intent = new Intent (SleepLogActivity.this, SleepEventLog.class);
                startActivity(intent);
                editor.putBoolean("bSleep", false);
                editor.commit();

            }
        });

        final ImageButton goToWakeLog = (ImageButton)findViewById(R.id.imageButtonWake);
        goToWakeLog.setEnabled(buttonPrefs.getBoolean("bWake", true));
        if (! buttonPrefs.getBoolean("bWake", true)){
            goToWakeLog.setImageResource(R.drawable.wake_done);}
        goToWakeLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sleepsharedPrefs, 1);
                Intent intent = new Intent (SleepLogActivity.this, SleepEventLog.class);
                startActivity(intent);
                editor.putBoolean("bWake", false);
                editor.commit();
            }
        });


        Button btn4 = (Button)findViewById(R.id.button3);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = buttonPrefs.edit();

                sb= (SeekBar) findViewById(R.id.seekBar11);
                user = FirebaseAuth.getInstance().getCurrentUser();
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

                int answer = sb.getProgress();
                SleepScale sc = new SleepScale(answer);
                myRef.child("Sleep Log").child(currentDate).setValue(sc);

                String pattern = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    if (dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("noon",null)))) {
                        editor.putBoolean("bSleepLog", false);
                        editor.putBoolean("SleepLogDone" , true);
                        editor.putBoolean("WakeTimeDone", true);
                        editor.putBoolean("bSleep", true);
                        editor.putBoolean("bWake",true);
                        editor.commit();
                    }
                }catch (Exception e) {
                    e.printStackTrace();        }

                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setPreferences(SharedPreferences sp, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("EventType", value);
        editor.commit();
    }

}

