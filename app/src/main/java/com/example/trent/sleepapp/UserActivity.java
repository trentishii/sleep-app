package com.example.trent.sleepapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import android.content.Context;

import static com.example.trent.sleepapp.StartFragment.BUTTONPREFNAME;



public class UserActivity extends AppCompatActivity implements StartFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, JournalFragment.OnFragmentInteractionListener
{
    private Toolbar myToolbar;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean buttonStatus;
    private static final String TAG = "UserActivity";
    private ArrayList<String> testsDone;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        Intent notifyIntent = new Intent(this,MyReceiver.class);
//        Intent notifyIntent = new Intent(this,MyNewIntentService.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast
//                (context, NOTIFICATION_REMINDER_NIGHT, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast
//                (this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        try {
//             Perform the operation associated with our pendingIntent
//            pendingIntent.send();
//            System.out.println("Pending Intent set");
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
//                1000 * 60 * 60 * 24, pendingIntent);


        if (checkPermission()) {
        //If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//
            Log.e("permission", "Permission already granted.");
        } else {
        //If your app doesn’t have permission to access external storage, then call requestPermission//
            requestPermission();
        }

        testsDone = new ArrayList<>();
        testsDone.add(0, "PAMDone");
        testsDone.add(1, "PAM2Done");
        testsDone.add(2, "PAM3Done");
        testsDone.add(3, "PAM4Done");
        testsDone.add(4, "SSSDone");
        testsDone.add(5, "SSS2Done");
        testsDone.add(6, "SSS3Done");
        testsDone.add(7, "SSS4Done");
        testsDone.add(8, "PVTDone");
        testsDone.add(9, "PVT2Done");
        testsDone.add(10, "PVT3Done");
        testsDone.add(11, "PVT4Done");
        testsDone.add(12, "JournalDone");
        testsDone.add(13, "SleepLogDone");
        testsDone.add(14, "PANASDone");
        testsDone.add(15, "LEEDSDone");

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StartFragment fragment = new StartFragment();


        Date d = Calendar.getInstance().getTime();
        String[] dateString = d.toString().split(" ");
        String pattern = "HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = buttonPrefs.edit();
        editor.putString("noon", "12:00:00");
        editor.putString("evening", "16:00:00" );
        editor.putString("bedtime","20:00:00");
        editor.commit();
        try {
            int count = 0;
            if(count == 0) {
//                editor.putBoolean("DayTime1Done",false);
//                editor.putBoolean("DayTime2Done",false);
//                editor.putBoolean("WakeTimeDone",false);
//                editor.putBoolean("SleepTimeDone",false);
                editor.commit();
            }

            if (dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("noon",null)))) {
                if (!buttonPrefs.getBoolean("WakeTimeDone",false)){
                    editor.putBoolean("bSleepLog", true) ;
                    editor.putBoolean("bLEEDS", true);
                    editor.putBoolean("bPAM", true);
                    editor.putBoolean("bSSS", true);
                    editor.putBoolean("bPVT", true);
                    editor.putBoolean("StartOfDay",true);
                    editor.putBoolean("WakeTimeDone", true);
                    for (int i=0; i<=15; i++) {
                        editor.putBoolean(testsDone.get(i),false);
                    }
                    editor.commit();
                    }
                editor.putBoolean("b2PAM", false);
                editor.putBoolean("b3PAM", false);
                editor.putBoolean("b4PAM", false);
                editor.putBoolean("b2SSS", false);
                editor.putBoolean("b3SSS", false);
                editor.putBoolean("b4SSS", false);
                editor.putBoolean("b2PVT", false);
                editor.putBoolean("b3PVT", false);
                editor.putBoolean("b4PVT", false);
                editor.putBoolean("bPANAS", false);
                editor.putBoolean("bJournal", false);
                editor.putBoolean("SleepTimeDone", false);
                editor.putBoolean("DayTime1Done", false);
                editor.putBoolean("DayTime2Done", false);
                editor.commit();

            }
            else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("noon", null))) && dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("evening", null)))) {
                if (!buttonPrefs.getBoolean("DayTime1Done",false)){
                    editor.putBoolean("b2PAM", true);
                    editor.putBoolean("b2PVT", true);
                    editor.putBoolean("b2SSS", true);
                    editor.putBoolean("PAM2Done", false);
                    editor.putBoolean("SSS2Done", false);
                    editor.putBoolean("PVT2Done", false);
                    editor.putBoolean("DayTime1Done", true);
                    editor.commit();
                }
                editor.putBoolean("bSleepLog", false);
                editor.putBoolean("bLEEDS", false);
                editor.putBoolean("bPAM", false);
                editor.putBoolean("b3PAM", false);
                editor.putBoolean("b4PAM", false);
                editor.putBoolean("bSSS", false);
                editor.putBoolean("b3SSS", false);
                editor.putBoolean("b4SSS", false);
                editor.putBoolean("bPVT", false);
                editor.putBoolean("b3PVT", false);
                editor.putBoolean("b4PVT", false);
                editor.putBoolean("bPANAS", false);
                editor.putBoolean("bJournal", false);
                editor.putBoolean("WakeTimeDone", false);
                editor.putBoolean("DayTime2Done", false);
                editor.putBoolean("SleepTimeDone", false);
                editor.commit();
            }
            else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("evening", null))) && dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("bedtime", null)))) {
                if (!buttonPrefs.getBoolean("DayTime2Done",false)) {
                    editor.putBoolean("b3PAM", true);
                    editor.putBoolean("b3SSS", true);
                    editor.putBoolean("b3PVT", true);
                    editor.putBoolean("PAM3Done", false);
                    editor.putBoolean("SSS3Done", false);
                    editor.putBoolean("PVT3Done", false);
                    editor.putBoolean("DayTime2Done", true);
                    editor.commit();
                }
                editor.putBoolean("bPAM", false);
                editor.putBoolean("b2PAM", false);
                editor.putBoolean("b4PAM", false);
                editor.putBoolean("bSSS", false);
                editor.putBoolean("b2SSS", false);
                editor.putBoolean("b4SSS", false);
                editor.putBoolean("bPVT", false);
                editor.putBoolean("b2PVT", false);
                editor.putBoolean("b4PVT", false);
                editor.putBoolean("bPANAS", false);
                editor.putBoolean("bJournal", false);
                editor.putBoolean("bSleepLog", false);
                editor.putBoolean("bLEEDS", false);
                editor.putBoolean("WakeTimeDone", false);
                editor.putBoolean("DayTime1Done", false);
                editor.putBoolean("SleepTimeDone", false);
                editor.commit();

            }
            else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("bedtime", null)))) {
                if (!buttonPrefs.getBoolean("SleepTimeDone",false)) {
                    editor.putBoolean("b4PAM", true);
                    editor.putBoolean("b4SSS", true);
                    editor.putBoolean("b4PVT", true);
                    editor.putBoolean("bPANAS", true);
                    editor.putBoolean("bJournal", true);
                    editor.putBoolean("JournalDone", false);
                    editor.putBoolean("PANASDone", false);
                    editor.putBoolean("PAM4Done", false);
                    editor.putBoolean("SSS4Done", false);
                    editor.putBoolean("PVT4Done", false);
                    editor.putBoolean("SleepTimeDone", true);
                    editor.commit();
                }
                editor.putBoolean("bPAM", false);
                editor.putBoolean("b2PAM", false);
                editor.putBoolean("b3PAM", false);
                editor.putBoolean("bSSS", false);
                editor.putBoolean("b2SSS", false);
                editor.putBoolean("b3SSS", false);
                editor.putBoolean("bPVT", false);
                editor.putBoolean("b2PVT", false);
                editor.putBoolean("b3PVT", false);
                editor.putBoolean("bSleepLog", false);
                editor.putBoolean("bLEEDS", false);
                editor.putBoolean("WakeTimeDone", false);
                editor.putBoolean("DayTime1Done", false);
                editor.putBoolean("DayTime2Done", false);
                editor.commit();
            }

        }catch (Exception e) {
            e.printStackTrace();        }

            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            myToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "Toolbar title clicked", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private boolean checkPermission() {

    //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

    //If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
        return true;
        } else {

    //If the app doesn’t have this permission, then return false//

        return false;
        }
        }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(UserActivity.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserActivity.this,
                            "Permission denied", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("UserActivity", "onOpt");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("UserActivity", "Settings Button Change");
                SettingsFragment setFrag = new SettingsFragment();
                transaction.replace(R.id.fragment_container, setFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_home:
                Log.d("UserActivity", "Home Button Change");
                StartFragment startFrag = new StartFragment();
                transaction.replace(R.id.fragment_container, startFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
//            case R.id.action_journal:
//                Log.d("UserActivity", "Journal Button Change");
//                JournalFragment journalFrag = new JournalFragment();
//                transaction.replace(R.id.fragment_container, journalFrag);
//                transaction.addToBackStack(null);
//                transaction.commit();
//                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
