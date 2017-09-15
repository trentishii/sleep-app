package com.example.trent.sleepapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.trent.sleepapp.StartFragment.BUTTONPREFNAME;

public class UserActivity extends AppCompatActivity implements StartFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, JournalFragment.OnFragmentInteractionListener
{
    private Toolbar myToolbar;
    private boolean buttonStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StartFragment fragment = new StartFragment();


        Date d = Calendar.getInstance().getTime();
        String[] dateString = d.toString().split(" ");
        String noon = "12:00:00";
        String evening = "18:00:00";
        String bedtime = "21:00:00";
        String pattern = "HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = buttonPrefs.edit();
        try {
            int count = 0;
            if(count == 0) {
                editor.putBoolean("DayTime1Done",false);
                editor.putBoolean("DayTime2Done",false);
                editor.putBoolean("WakeTimeDone",false);
                editor.putBoolean("SleepTimeDone",false);
                editor.commit();
                count = count + 1;
            }

            if (dateFormat.parse(dateString[3]).before(dateFormat.parse(noon))) {
                if (!buttonPrefs.getBoolean("WakeTimeDone",false)){
                    editor.putBoolean("bSleepLog", true) ;
                    editor.putBoolean("bLEEDS", true);
                    editor.putBoolean("bPAM", true);
                    editor.putBoolean("bSSS", true);
                    editor.putBoolean("bPVT", true);
                    editor.putBoolean("SleepLogDone", false);
                    editor.putBoolean("LEEDSDone", false);
                    editor.putBoolean("PAMDone", false);
                    editor.putBoolean("SSSDone", false);
                    editor.putBoolean("PVTDone", false);
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
                editor.commit();

            }
            else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(noon)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(evening))) {
                if (!buttonPrefs.getBoolean("DayTime1Done",false)){
                    editor.putBoolean("b2PAM", true);
                    editor.putBoolean("b2PVT", true);
                    editor.putBoolean("b2SSS", true);
                    editor.putBoolean("PAM2Done", false);
                    editor.putBoolean("SSS2Done", false);
                    editor.putBoolean("PVT2Done", false);
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
                editor.commit();
            }
            else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(evening)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(bedtime))) {
                if (!buttonPrefs.getBoolean("DayTime2Done",false)) {
                    editor.putBoolean("b3PAM", true);
                    editor.putBoolean("b3SSS", true);
                    editor.putBoolean("b3PVT", true);
                    editor.putBoolean("PAM3Done", false);
                    editor.putBoolean("SSS3Done", false);
                    editor.putBoolean("PVT3Done", false);
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
                editor.putBoolean("DayTime1Done", false);
                editor.commit();

            }
            else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(bedtime))) {
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
