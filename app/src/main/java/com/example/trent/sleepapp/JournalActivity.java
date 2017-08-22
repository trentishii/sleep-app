package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JournalActivity extends AppCompatActivity {
    SharedPreferences sharedPrefs;
    public static final String PREFNAME = "userPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        sharedPrefs = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);



        ImageButton goToTobaccoLog = (ImageButton)findViewById(R.id.imageButtonTobacco);
        goToTobaccoLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sharedPrefs, 0);
                Intent intent = new Intent (JournalActivity.this, EventLog.class);
                startActivity(intent);
            }
        });

        ImageButton goToCoffeeLog = (ImageButton)findViewById(R.id.imageButtonCoffee);
        goToCoffeeLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sharedPrefs, 1);
                Intent intent = new Intent (JournalActivity.this, EventLog.class);
                startActivity(intent);
            }
        });

        ImageButton goToExerciseLog = (ImageButton)findViewById(R.id.imageButtonExercise);
        goToExerciseLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sharedPrefs, 2);
                Intent intent = new Intent (JournalActivity.this, EventLog.class);
                startActivity(intent);
            }
        });

        ImageButton goToMealLog = (ImageButton)findViewById(R.id.imageButtonMeal);
        goToMealLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sharedPrefs, 3);
                Intent intent = new Intent (JournalActivity.this, EventLog.class);
                startActivity(intent);
            }
        });

        ImageButton goToAlcoholLog = (ImageButton)findViewById(R.id.imageButtonAlcohol);
        goToAlcoholLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sharedPrefs, 4);
                Intent intent = new Intent (JournalActivity.this, EventLog.class);
                startActivity(intent);
            }
        });

        ImageButton goToPillLog = (ImageButton)findViewById(R.id.imageButtonPill);
        goToPillLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sharedPrefs, 5);
                Intent intent = new Intent (JournalActivity.this, EventLog.class);
                startActivity(intent);
            }
        });

        Button btn3 = (Button)findViewById(R.id.button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = buttonPrefs.edit();

                Date d = Calendar.getInstance().getTime();
                String[] dateString = d.toString().split(" ");
                String bedtime = "20:00:00";
                String pattern = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    if (dateFormat.parse(dateString[3]).after(dateFormat.parse(bedtime))) {
                        editor.putBoolean("bJournal", false);
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
//    public void somefunction(){
//        Bundle info_bundle = createInfoBundle();
//        Long measurement_time = System.currentTimeMillis();
//        Utils_Json mJsonUtil = new Utils_Json();
//        String json_string = mJsonUtil.toJSon(info_bundle);
//        System.out.println(json_string);
//        String filePath = getExternalFilesDir(null).getAbsolutePath()
//                + "/" + Integer.toString(computer_generated_id)
//                + "-" + Long.toString(measurement_time)
//                + ".info";
//        try {
//            FileOutputStream os = new FileOutputStream(filePath, true);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
//            outputStreamWriter.write(json_string + "\r\n");
//            outputStreamWriter.close();
//        } catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//
//    }
}
