package com.example.trent.sleepapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trent.sleepapp.pvt.PVTHome;

public class LeedsActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView pbText;
    private RelativeLayout pbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leeds);
        pbView = (RelativeLayout) findViewById(R.id.leedsProg);
        pb = (ProgressBar) pbView.findViewById(R.id.progressBar);
        pbText = (TextView) pbView.findViewById(R.id.tvProgress);
        pb.setProgress(2);
        pbText.setText("You have completed 2 of 7 tests");
        Button btn2 = (Button)findViewById(R.id.bSubmit);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SSSActivity.class);
                startActivity(intent);
            }
        });
    }
}
