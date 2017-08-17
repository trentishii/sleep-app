package com.example.trent.sleepapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.wearable.MessageEvent;
//import com.google.android.gms.wearable.Node;
//import com.google.android.gms.wearable.NodeApi;
//import com.google.android.gms.wearable.Wearable;
//import com.google.android.gms.wearable.WearableListenerService;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
//import android.app.*;

//public class AccLogService extends WearableListenerService implements SensorEventListener{
public class LightLogService extends Service implements SensorEventListener{
    private static final String ANDROID = "Android";
    private static final String DATA = "data";
    private static final String FILES = "files";
    public static final String PREFNAME = "userPrefs";

    private static final String TAG = "LightLogService";
    private BroadcastReceiver unlockReceiver;
    private static boolean running  = false;
    private SensorManager sensorManager;
    private Sensor accelerometer; //heartRate; //pressure /*gravity, linearAcc, pressure, light, proximity*/;
    //private Sensor heartRate; //REMOVE - UCSF test
    private Sensor light;
    //private Sensor mag;
    //private Sensor proximity;
    private PowerManager.WakeLock wl;
    private File file = null;
    private BufferedWriter out = null;
    private final int ONGOING_NOTIFICATION = 1;
    private long ts = 0;
    private final int BUFFER_SIZE = 8*1024;
    private final int SESSION_LENGTH = 20*60*1000;
    private Context mContext;
    public BroadcastReceiver mReceiver;
    //private int count_0 = 0;
    //private int count_1 = 0;
    //private int count_2 = 0;
    private String location, activity, id;
    private double gx,gy,gz;
    private float mbar;
    private ToneGenerator tg;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");

    private static final long CONNECTION_TIME_OUT_MS = 500;
    private static final String MESSAGE = "Watch is collecting data!";

    //private static GoogleApiClient client;
    private String nodeId;
    //private final int  TEMP_HEARTRATE_SENSOR = Sensor.TYPE_HEART_RATE;//65562; //REMOVE - UCSF test
    private String android_id;
    private boolean HR_SENSING = true;
    private boolean hr_working = false;
    private final int HR_SAMPLING_INTERVAL = 1000*30*9;
    private final int HR_SAMPLING_DURATION = 1000*30;
    //private final int ACC_SAMPLING_INTERVAL = 10000; //Default 100Hz
    private final int ACC_SAMPLING_INTERVAL = 1000000; //50Hz
    private final int OTHER_SAMPLING_INTERVAL = 10000; //Default 100Hz
    //private final int OTHER_SAMPLING_INTERVAL = 20000; //50Hz

    private boolean isReportingToHost = false; //true;
    private boolean dutycyclingHR = true;//false;  //true;

    //private long count = 0;
    private static ArrayList<String> nodeIds;

    private static int accelSampleCount = 0;
    private static int gyroSampleCount = 0;
    private static int magSampleCount = 0;
    //private int compassSampleCount = 0;
    //private Timer timer;
    //private boolean timerStarted = false;

    //For Doze Test
    //private Timer dozeTestTimer;
    //private String WATCH_MODEL = "SamsungGearLive";
    //private String WATCH_MODEL = "AsusZenwatch";
    private String WATCH_MODEL = "Moto360";

    //0 to use callback timestamp. 1 to use sample event timestamp
    private int USE_SAMPLE_TIMESTAMP = 1;
    private boolean firstSample = false;
    private long sensorTimeReference = 0;
    private long myTimeReference = 0;
    private StorageReference mStorageRef;
    private FirebaseUser user;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
        tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //heartRate = sensorManager.getDefaultSensor(TEMP_HEARTRATE_SENSOR);
        //Log.d(TAG,heartRate.getName());
        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        //heartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE); //REMOVE - UCSF test
        //gyro          = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //mag           = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //gravity       = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //linearAcc     = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //pressure     = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        light        = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //proximity    = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        //Acquire a partial wake lock, such that the acc continue to work after the phone is locked.
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MY LOCK");
        //wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MY LOCK");


        //List all available sensors on startup
        final List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor type : deviceSensors){
            Log.w(TAG ,type.getStringType());
        }

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        if(running) {
            running = false;
            stopSensing();
            stopForeground(true);
        }

        //timer.cancel();
        //timer = null;

        /*
        if(client != null){
            if(client.isConnected()){
                client.disconnect();
                Log.v(TAG, "GoogleApiClient disconnected");
            }
        }
        */
        //unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        location = intent.getCharSequenceExtra("location").toString();
//        activity = intent.getCharSequenceExtra("activity").toString();
//        user     = intent.getCharSequenceExtra("user").toString();
//        id     = intent.getCharSequenceExtra("id").toString();
        writeLabel();
//        Log.d(TAG, "onStartCommand " + location + " " + activity);

        /*
		if(running){
			Log.d(TAG, "already running re-reg listener");
			Toast.makeText(this, "Service is turning off", Toast.LENGTH_SHORT).show();
            running = false;
            stopSensing();
            stopForeground(true);
            stopSelf();

            //disconnect();

			return START_STICKY;
		}else{
			running = true;
			Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();

            //retrieveDeviceNode();
            //connect();

			startSensing();
			return START_STICKY;
		}
		*/

        if(!running){
            running = true;
            Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
            startSensing();
        }

        return START_STICKY;
    }

    private void writeLabel(){
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = getApplicationContext().getPackageName();
        File f = new File(new File(new File(new File(
                externalPath, ANDROID), DATA), packageName), FILES);
        File file = new File(f, "light_logs");
        if(!file.exists())  file.mkdirs();
        file = new File(file.getAbsolutePath() + "/" + "label.txt");
        if(file.exists())  file.delete();
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file),BUFFER_SIZE);
//            bw.write(location);
//            bw.newLine();
//            bw.write(activity);
//            bw.newLine();
//            bw.write(user);
//            bw.newLine();
//            bw.write(id);
//            bw.newLine();
            bw.close();
        }catch(IOException e){
            Log.e(TAG, "IO ERROR" + e.getMessage());
        }
    }

    private File getFile(){
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = getApplicationContext().getPackageName();
        File f = new File(new File(new File(new File(
                externalPath, ANDROID), DATA), packageName), FILES);
        File file = new File(f, "light_logs");
        if(!file.exists())  file.mkdirs();
        ts = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_a");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();
        String[] newName = name.split("@");
        DatabaseReference myRef = database.getReference(newName[0]);
        String date = sdf.format(ts);
        file = new File(file.getAbsolutePath() + "/" +  android_id  + "_" + date + "_" + newName[0]  + ".csv");
//        file = new File(file.getAbsolutePath() + "/" +  android_id  + "_" + date + "_" + location + "_" + activity + "_" + user + "_session" + id + ".csv");
        try{
            out = new BufferedWriter(new FileWriter(file),BUFFER_SIZE);
            out.write("ts,type,val0,val1,val2,val3\n");
            //write a dummy line for matlab. Discard this padding line when reading.
            out.write("-1,-1,-1,-1,-1,-1\n");
        }catch(IOException e){
            Log.e(TAG, ""+e.getMessage());
        }
        return file;
    }

    /*
    private class myTask extends TimerTask
    {
        public void run()
        {
            toggleSensors();
        }
    }
    */

    /*
    private void toggleSensors(){
        timer = new Timer();
        if(hr_working){
            //sensorManager.unregisterListener(this,heartRate);
            timer.schedule(new myTask(),this.HR_SAMPLING_INTERVAL);
        }else{
            //sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_FASTEST);
            timer.schedule(new myTask(),this.HR_SAMPLING_DURATION);
        }
        hr_working=!hr_working;
    }
    */

    private void startSensing(){
        firstSample = true;
        wl.acquire();
        file = getFile();
        //if(dutycyclingHR)toggleSensors();
        //if (!file.exists()) file.createNewFile();
        //out = new BufferedWriter(new FileWriter(file),BUFFER_SIZE);
        //sensorManager.registerListener(this, accelerometer, ACC_SAMPLING_INTERVAL);
        //sensorManager.registerListener(this, heartRate, OTHER_SAMPLING_INTERVAL); //REMOVE - UCSF test
        //if(!dutycyclingHR&&HR_SENSING)sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL);
        //if(!dutycyclingHR&&HR_SENSING)sensorManager.registerListener(AccLogService.this, heartRate, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(this, gyro, OTHER_SAMPLING_INTERVAL);
        //sensorManager.registerListener(this, mag, OTHER_SAMPLING_INTERVAL);
        //sensorManager.registerListener(AccLogService.this, gravity, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(AccLogService.this, linearAcc, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(AccLogService.this, pressure, ACC_SAMPLING_INTERVAL);
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(AccLogService.this, proximity, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void stopSensing(){
        //timer.cancel();
        sensorManager.unregisterListener(this);
        try{
            mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri nFile = Uri.fromFile(file);
            StorageReference riversRef = mStorageRef.child("light" + file.getName());

            riversRef.putFile(nFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
            file.delete();
            out.flush();
            out.close();
        }catch(IOException e){
            Log.e(TAG, ""+e.getMessage());
        }
        file = null;
        out = null;
        wl.release();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        long now = 0;
        if(USE_SAMPLE_TIMESTAMP == 0){
            now = System.currentTimeMillis(); //Gives unix timestamp
        }
        else if(USE_SAMPLE_TIMESTAMP == 1){ //event.timestamp gives nanoseconds from bootup. Need to convert to unix timestamp

            //Problem
            /*
            now = (new Date()).getTime()
                    + (event.timestamp - System.nanoTime()) / 1000000L;
            //Debugging
            Log.w(TAG, "new Date()).getTime(): "+(new Date()).getTime()); //Fine
            Log.w(TAG, "event.timestamp: "+event.timestamp); //Fine
            Log.w(TAG, "System.nanoTime(): "+System.nanoTime()); //Eventually starts giving a lot smaller value than expected. Resets?
            */

            //Looks good so far
            if(firstSample){
                sensorTimeReference = event.timestamp;
                myTimeReference = System.currentTimeMillis();
                firstSample = false;
            }

            now = myTimeReference + Math.round((event.timestamp - sensorTimeReference) / 1000000.0);

        }

        //Log.d(TAG, "sensor event: " + event.sensor + ":" + event.accuracy + " = " + event.values[0]);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x, y, z;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            Log.d("Accelerometer Values", x + " " + y + " " + z);
            //count_0++;
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    //if(isReportingToHost&&count_0%2000==1) sendToast();

                    //Create new file every SESSION_LENGTH

                    //if( now - ts> SESSION_LENGTH){
                    if( System.currentTimeMillis() - ts> SESSION_LENGTH){
                        //Log.w(TAG, "CREATING A NEW FILE - now: "+now+" | ts: "+ts);

                        out.flush();
                        out.close();
                        file = getFile();
                        //count_0 = 0;
                        //if (!file.exists()) file.createNewFile();
                    }


                    //out.write(now + "," + "0" + "," + x*1000/9.81 + "," + y*1000/9.81 + ","+ z*1000/9.81 + "," + mbar);
                    out.write(df.format(new Date(now)) + "," + now + "," + "0" + "," + x*1000/9.81 + "," + y*1000/9.81 + ","+ z*1000/9.81);
                    out.newLine();
                    accelSampleCount++;
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
            stopSelf();
            //if(count_0%2000 == 0) tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        }

        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x, y, z;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            //count_1++;
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    //if(count_1%100==1) Log.e(TAG, "gyro data" + ""+ x + "," + y + ","+ z);
                    out.write(df.format(new Date(now)) + "," + now + "," + "1" + "," + x + "," + y + ","+ z);
                    out.newLine();
                    gyroSampleCount++;
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
        }

        //Add TYPE_MAGNETIC_FIELD?

        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x, y, z;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            //count_2++;
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    //if(count_2%100==1) Log.e(TAG, "mag data" + ""+ x + "," + y + ","+ z);
                    out.write(df.format(new Date(now)) + "," + now + "," + "2" + "," + x + "," + y + ","+ z);
                    out.newLine();
                    magSampleCount++;
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
        }

        else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x, y, z;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            double mag = Math.sqrt(x*x + y*y + z*z);
            gx = x/mag;
            gy = y/mag;
            gz = z/mag;
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    //if(count_2%100==1) Log.e(TAG, "mag data" + ""+ x + "," + y + ","+ z);
                    out.write(df.format(new Date(now)) + "," + now + "," + "3" + "," + x + "," + y + ","+ z);
                    out.newLine();
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
            //Log.e(TAG, "" + x + " " + y + " " + z);
        } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            float x, y, z;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            double dot = gx*x + gy*y + gz*z;
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    //if(count_2%100==1) Log.e(TAG, "mag data" + ""+ x + "," + y + ","+ z);
                    out.write(df.format(new Date(now)) + "," + now + "," + "4" + "," + x + "," + y + ","+ z);
                    out.newLine();
                    out.write(df.format(new Date(now)) + "," + now + "," + "5" + "," + dot);
                    out.newLine();
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
        } else if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
            mbar = event.values[0];
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    out.write(df.format(new Date(now)) + "," + now + "," + "6" + "," + mbar);
                    out.newLine();
                    //Log.e(TAG, df.format(new Date(now)) + "," + now + ", pressure:"+ mbar);
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
        }else if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            float lux = event.values[0];
            Log.d("Accelerometer Values", lux + " lux");
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    out.write(df.format(new Date(now)) + "," + now + "," + "7" + "," + lux);
                    out.newLine();
                    //Log.e(TAG, df.format(new Date(now)) + "," + now + ",light:"+ lux);
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
        }else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            float cm = event.values[0];
            if(out == null){
                Log.e(TAG, ""+"called after off");
            }else{
                try{
                    out.write(df.format(new Date(now)) + "," + now + "," + "8" + "," + cm);
                    out.newLine();
                    //Log.e(TAG, df.format(new Date(now)) + "," + now + ",proximity:"+ cm);
                }catch(IOException e){
                    Log.e(TAG, ""+e.getMessage());
                }
            }
        }
        //else if (event.sensor.getType() == Sensor.TYPE_HEART_RATE){

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

}