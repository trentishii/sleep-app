package com.example.trent.sleepapp;

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

public class UserActivity extends AppCompatActivity implements StartFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, JournalFragment.OnFragmentInteractionListener {
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        StartFragment fragment = new StartFragment();
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
