package com.example.trent.sleepapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.trent.sleepapp.pvt.PVTHome;

/**
 * Created by vinisha on 8/18/17.
 */

public class StartActivity extends AppCompatActivity {
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bPAM:
            case R.id.b2PAM:
            case R.id.b3PAM:
                Log.e("Start", "PAM Clicked");
                Intent intent = new Intent(this, PAMActivity.class);
                startActivity(intent);
                break;
            case R.id.bSSS:
            case R.id.b2SSS:
            case R.id.b3SSS:
                Log.e("Start", "SSS Clicked");
                Intent intent1 = new Intent(this, SSSActivity.class);
                startActivity(intent1);
                break;
            case R.id.bPVT:
            case R.id.b2PVT:
            case R.id.b3PVT:
                Log.e("Start", "PVT Clicked");
                Intent intent2 = new Intent(this, PVTHome.class);
                startActivity(intent2);
                break;
            case R.id.bSleepLog:
                Log.e("Start", "Sleep Log Clicked");
                Intent intent3 = new Intent(this, SleepLogActivity.class);
                startActivity(intent3);
                break;
            case R.id.bLEEDS:
                Log.e("Start", "Leeds Clicked");
                Intent intent4 = new Intent(this, LeedsActivity.class);
                startActivity(intent4);
                break;
            case R.id.bPANAS:
                Log.e("Start", "Panas Clicked");
                Intent intent5 = new Intent(this, PanasActivity.class);
                startActivity(intent5);
                break;
            case R.id.bJournal:
                Log.e("Start", "Journal Clicked");
                Intent intent6 = new Intent(this, JournalActivity.class);
                startActivity(intent6);
                break;
        }
    }


}
