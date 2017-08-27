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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//package com.example.trent.sleepapp;


public class SleepLogActivity extends AppCompatActivity {
    SharedPreferences sleepsharedPrefs;
    public static final String PREFNAME = "userPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_log);
        sleepsharedPrefs = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);


        ImageButton goToSleepLog = (ImageButton)findViewById(R.id.imageButtonSleep);
        goToSleepLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sleepsharedPrefs, 0);
                Intent intent = new Intent (SleepLogActivity.this, SleepEventLog.class);
                startActivity(intent);
            }
        });

        ImageButton goToWakeLog = (ImageButton)findViewById(R.id.imageButtonWake);
        goToWakeLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setPreferences(sleepsharedPrefs, 1);
                Intent intent = new Intent (SleepLogActivity.this, SleepEventLog.class);
                startActivity(intent);
            }
        });

        Button btn4 = (Button)findViewById(R.id.button3);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = buttonPrefs.edit();

                Date d = Calendar.getInstance().getTime();
                String[] dateString = d.toString().split(" ");
                String noon = "12:00:00";
                String pattern = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    if (dateFormat.parse(dateString[3]).before(dateFormat.parse(noon))) {
                        editor.putBoolean("bSleepLog", false);
                        editor.putBoolean("WakeTimeDone", true);
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

