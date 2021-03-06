package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.trent.sleepapp.pvt.DataStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.id;
import static android.R.attr.lines;
import static android.provider.SyncStateContract.Columns.DATA;
import static com.example.trent.sleepapp.R.id.container;
import static java.lang.System.out;

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
        private ArrayList<ImageView> memoryImageViews;
        private static final String ANDROID = "Android";
        private static final String DATA = "data";
        private static final String FILES = "files";
        public static final String PREFNAME = "userPrefs";
        private final int BUFFER_SIZE = 8*1024;
        private BufferedWriter out = null;
        private long ts = 0;
//        private int test_duration = 10*1000;
        private int test_duration = 2*60*1000;
         private int shape_number = 0;
        private FirebaseUser user;
        private String android_id;
        private SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
        long now = 0;
        private StorageReference mStorageRef;
        private File file = null;

//        protected void onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            now = System.currentTimeMillis();
            setContentView(R.layout.activity_nback);

            memoryImageViews = new ArrayList<>();
            memoryImageViews.add((ImageView) findViewById(R.id.imageStar));
            memoryImageViews.add((ImageView) findViewById(R.id.imageCircle));
            memoryImageViews.add((ImageView) findViewById(R.id.imageSquare));
            memoryImageViews.add((ImageView) findViewById(R.id.imageTriangle));
            memoryImageViews.add((ImageView) findViewById(R.id.imageHexagon));
            memoryImageViews.add((ImageView) findViewById(R.id.imageClick));


            setStar = (ImageView)findViewById(R.id.imageStar);
            setCircle = (ImageView)findViewById(R.id.imageCircle);
            setSquare = (ImageView)findViewById(R.id.imageSquare);
            setTriangle = (ImageView)findViewById(R.id.imageTriangle);
            setHexagon = (ImageView)findViewById(R.id.imageHexagon);
            setCircle.setClickable(true);
            setHexagon.setClickable(true);
            setSquare.setClickable(true);
            setTriangle.setClickable(true);
            setStar.setClickable(true);
            timer = new Timer();
            timeElapsed = System.currentTimeMillis();
            timer.scheduleAtFixedRate(new pickShape(), 0, 1500);
            timer.scheduleAtFixedRate(new flash(), 1400, 1500);
            try {
                file = getFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

//        class runTest extends TimerTask{
//            public void run(){
//                Log.e(TAG, "**************In timer***************");
//                runOnUiThread(new Thread(new Runnable() {
//                    public void run() {
//                        timer.scheduleAtFixedRate(new pickShape(), 0, 1500);
//                        timer.scheduleAtFixedRate(new flash(), 1400, 1500);
//                    }
//            }
//        }

        class pickShape extends TimerTask {
            public void run() {
                if (System.currentTimeMillis() - timeElapsed <= test_duration) {
                    final Random r = new Random();
                    randIdx = r.nextInt(5) ;
//                    randIdx = 0;
                    shape_number = shape_number+1;
//                    Log.d(TAG, "!!!!!!!!!!!!!!!"+Long.toString(System.currentTimeMillis() - timeElapsed));
                    runOnUiThread(new Thread(new Runnable() {
                        public void run() {
                            memoryImageViews.get(randIdx).setVisibility(View.VISIBLE);
                            now = System.currentTimeMillis();
                            try{
                                // "111111111111" implies displayed shape
                                out.write(df.format(new Date(now)) + "," + now + "," + "11111,"+ shape_number+ "," +randIdx );
                                out.newLine();
                            }catch(IOException e){
                                Log.e(TAG, ""+e.getMessage());
                            }
                            for (int i=0; i<5; i++) {
                                final int idx = i;
                                memoryImageViews.get(idx).setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                Log.d(TAG, "*************CLICK EVENT***********");
                                                memoryImageViews.get(5).setVisibility(View.VISIBLE);
                                                break;
                                            case MotionEvent.ACTION_UP:
                                                Log.d(TAG, "*************CLICK release***********");
                                                memoryImageViews.get(5).setVisibility(View.INVISIBLE);
                                                try{
                                                    // "00000" implies shape clicked
                                                out.write(df.format(new Date(now)) + "," + now + ","+ "00000,"+  shape_number+ "," + randIdx);
                                                out.newLine();
                                                }catch(IOException e){
                                                    Log.e(TAG, ""+e.getMessage());
                                                }
                                                break;
                                        }
                                        return true;
                                    }
                                });
                            }
                        }
                        }));
                    }else {
                        timer.cancel();
                        timer.purge();
                        timer = null;
                        Log.e(TAG, "**************Timer Task Ended***************");
                        stopTask();
                    }
                }
            }

        class flash extends TimerTask {
            public void run() {
                if (System.currentTimeMillis() - timeElapsed <= test_duration) {
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
                    timer.purge();
                    timer = null;
                    Log.e(TAG, "**************Flash Ended***************");
                    stopTask();
                    return;
                }

            }
        }


        private File getFile() throws IOException {
            File externalPath = Environment.getExternalStorageDirectory();
            String packageName = getApplicationContext().getPackageName();
            File f = new File(new File(new File(new File(
                    externalPath, ANDROID), DATA), packageName), FILES);
            File file = new File(f, "N-Back");
            if(!file.exists())  file.mkdirs();
            ts = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_a");
            String date = sdf.format(ts);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            String name = user.getEmail();
            String[] newName = name.split("@");
            DatabaseReference myRef = database.getReference(newName[0]);
            file = new File(file.getAbsolutePath() + "/" + "Nback_"+ date + "_" + newName[0]  + ".csv");
//            file = new File(file.getAbsolutePath() + "/" +  android_id  + "_" + date + "_" + location + "_" + activity + "_" + user + "_session" + id + ".csv");

            try{
                out = new BufferedWriter(new FileWriter(file),BUFFER_SIZE);
                out.write("ts,type,val0,val1,val2,val3\n");
                out.newLine();
                //write a dummy line for matlab. Discard this padding line when reading.
//                out.write("-1,-1,-1,-1,-1,-1\n");
            }catch(IOException e){
                Log.e(TAG, ""+e.getMessage());
            }
            return file;
        }


        public void stopTask(){
            SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = buttonPrefs.edit();

            Date d = Calendar.getInstance().getTime();
            String[] dateString = d.toString().split(" ");
            String pattern = "HH:mm:ss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                if (dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("noon",null)))) {
//                    editor.putBoolean("bPVT", false);
                    editor.putBoolean("PVTDone", true);
//                    editor.putBoolean("WakeTimeDone", true);
                    editor.commit();
                } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("noon",null))) && dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("evening",null)))) {
//                    editor.putBoolean("b2PVT", false);
                    editor.putBoolean("PVT2Done", true);
//                    editor.putBoolean("DayTime1Done", true);
                    editor.commit();
                } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("evening",null))) && dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("bedtime",null)))) {
//                    editor.putBoolean("b3PVT", false);
                    editor.putBoolean("PVT3Done", true);
//                    editor.putBoolean("DayTime2Done", true);
                    editor.commit();
                } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("bedtime",null)))) {
//                    editor.putBoolean("b4PVT", false);
                    editor.putBoolean("PVT4Done", true);
//                    editor.putBoolean("SleepTimeDone", true);
                    editor.commit();
                }
                out.flush();
                out.close();

                mStorageRef = FirebaseStorage.getInstance().getReference();
                Uri nFile = Uri.fromFile(file);
                StorageReference riversRef = mStorageRef.child(file.getName());
                riversRef.putFile(nFile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
//
            } catch (Exception e) {
                e.printStackTrace();
            }

            file = null;
            out = null;

            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            finish();
            startActivity(intent);
        }

    @Override
    public void onPause() {
        super.onPause();

        if(timer != null)
        {
            timer.cancel();
            timer.purge();
            timer = null;
        }

        Log.e(TAG, "**************Activity Ended***************");
        finish();
    }
}
