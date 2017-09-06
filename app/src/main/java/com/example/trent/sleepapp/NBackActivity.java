package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vinisha on 8/29/2017.
 */

public class NBackActivity extends AppCompatActivity {
        public ImageView setStar;
        public ImageView setSquare;
        public ImageView setCircle;
        public ImageView setTriangle;
        public ImageView setHexagon;
        public long timeElapsed;
        public int randIdx;
        Timer timer;
        private static final String TAG = "NBackActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nback);
            setStar = (ImageView)findViewById(R.id.imageStar);
            setCircle = (ImageView)findViewById(R.id.imageCircle);
            setSquare = (ImageView)findViewById(R.id.imageSquare);
            setTriangle = (ImageView)findViewById(R.id.imageTriangle);
            setHexagon = (ImageView)findViewById(R.id.imageHexagon);
//            setCircle.setClickable(true);
//            setHexagon.setClickable(true);
//            setSquare.setClickable(true);
//            setTriangle.setClickable(true);
//            setStar.setClickable(true);
            int after = 0;
            int interval = 1000;
            timer = new Timer();
            timeElapsed = (System.currentTimeMillis()/1000)%60;
            timer.scheduleAtFixedRate(new pickShape(), 0, 1500);
            timer.scheduleAtFixedRate(new flash(), 1400, 1500);
        }


        class pickShape extends TimerTask {
            public void run() {
                if (((System.currentTimeMillis() / 1000) % 60) - timeElapsed <= 5) {
                    Random r = new Random();
                    randIdx = r.nextInt(5) + 1;
                    Log.d(TAG, "*************"+Integer.toString(randIdx));
                    runOnUiThread(new Thread(new Runnable() {
                        public void run() {
                            switch (randIdx) {
                                case 1:
                                    setStar.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    setCircle.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    setSquare.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    setTriangle.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    setHexagon.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    setStar.setVisibility(View.INVISIBLE);
                                    setSquare.setVisibility(View.INVISIBLE);
                                    setCircle.setVisibility(View.INVISIBLE);
                                    setTriangle.setVisibility(View.INVISIBLE);
                                    setHexagon.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }
                        }));
                    }else {
                        timer.cancel();
                        Log.e(TAG, "**************Task Ended***************");
                        stopTask();
                        return;
                    }
                }
            }

        class flash extends TimerTask {
            public void run() {
                if (((System.currentTimeMillis() / 1000) % 60) - timeElapsed <= 5) {
                    runOnUiThread(new Thread(new Runnable() {
                        public void run() {
                            setStar.setVisibility(View.INVISIBLE);
                            setSquare.setVisibility(View.INVISIBLE);
                            setCircle.setVisibility(View.INVISIBLE);
                            setTriangle.setVisibility(View.INVISIBLE);
                            setHexagon.setVisibility(View.INVISIBLE);
                        }
                    }));
                } else {
                    timer.cancel();
                    Log.e(TAG, "**************Flash Ended***************");
                    stopTask();
                    return;
                }

            }
        }

        public void stopTask(){
            SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = buttonPrefs.edit();

            Date d = Calendar.getInstance().getTime();
            String[] dateString = d.toString().split(" ");
            String noon = "12:00:00";
            String evening = "18:00:00";
            String bedtime = "20:00:00";
            String pattern = "HH:mm:ss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                if (dateFormat.parse(dateString[3]).before(dateFormat.parse(noon))) {
                    editor.putBoolean("bPVT", false);
                    editor.putBoolean("PVTDone", true);
                    editor.putBoolean("WakeTimeDone", true);
                    editor.commit();
                } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(noon)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(evening))) {
                    editor.putBoolean("b2PVT", false);
                    editor.putBoolean("PVT2Done", true);
                    editor.putBoolean("DayTime1Done", true);
                    editor.commit();
                } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(evening)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(bedtime))) {
                    editor.putBoolean("b3PVT", false);
                    editor.putBoolean("PVT3Done", true);
                    editor.putBoolean("DayTime2Done", true);
                    editor.commit();
                } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(bedtime))) {
                    editor.putBoolean("b4PVT", false);
                    editor.putBoolean("PVT4Done", true);
                    editor.putBoolean("SleepTimeDone", true);
                    editor.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intent);
        }
}
