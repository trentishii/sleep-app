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

import static com.example.trent.sleepapp.StartFragment.BUTTONPREFNAME;

public class UserActivity extends AppCompatActivity implements StartFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, JournalFragment.OnFragmentInteractionListener
{
    private Toolbar myToolbar;
    private boolean buttonStatus;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user);

            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            StartFragment fragment = new StartFragment();

            String reset_time = "24:00:00";
            String pattern = "HH:mm:ss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date d = Calendar.getInstance().getTime();
            String[] dateString = d.toString().split(" ");
            SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = buttonPrefs.edit();

            if (dateFormat.parse(dateString[3]).after(dateFormat.parse(reset_time))) {
                editor.putBoolean("bPAM", true);
                editor.putBoolean("b2PAM", true);
                editor.putBoolean("b3PAM", true);
                editor.putBoolean("b4PAM", true);
                editor.putBoolean("bSSS", true);
                editor.putBoolean("b2SSS", true);
                editor.putBoolean("b3SSS", true);
                editor.putBoolean("b4SSS", true);
                editor.putBoolean("bPVT", true);
                editor.putBoolean("b2PVT", true);
                editor.putBoolean("b3PVT", true);
                editor.putBoolean("b4PVT", true);
                editor.putBoolean("bSleepLog", true);
                editor.putBoolean("bLEEDS", true);
                editor.putBoolean("bPANAS", true);
                editor.putBoolean("bJournal", true);
                editor.commit();
            }

//        if(count == 0) {
//            editor.putBoolean("bPAM", true);
//            editor.putBoolean("b2PAM", true);
//            editor.commit();
//            count = count + 1;
//        }


            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            myToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "Toolbar title clicked", Toast.LENGTH_SHORT).show();
                }
            });


//        Button btn2 = (Button)findViewById(R.id.bTests);
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PVTHome.class);
//                startActivity(intent);
//            }
//        });
//
//        Button btn3 = (Button)findViewById(R.id.bSleep);
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Button btn4 = (Button)findViewById(R.id.bAlarm);
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
//                startActivity(intent);
//            }
//        });
        }catch (Exception e) {
            e.printStackTrace();        }
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
            case R.id.action_journal:
                Log.d("UserActivity", "Journal Button Change");
                JournalFragment journalFrag = new JournalFragment();
                transaction.replace(R.id.fragment_container, journalFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
