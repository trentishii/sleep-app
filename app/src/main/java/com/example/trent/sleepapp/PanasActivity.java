package com.example.trent.sleepapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PanasActivity extends AppCompatActivity {
    private ProgressBar pb;
    private TextView pbText;
    private RelativeLayout pbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panas);

//        pbView = (RelativeLayout) findViewById(R.id.panasProg);
//        pb = (ProgressBar) pbView.findViewById(R.id.progressBar);
//        pbText = (TextView) pbView.findViewById(R.id.tvProgress);
//        pb.setProgress(4);
//        pbText.setText("You have completed 4 of 7 tests");
        Button btn3 = (Button)findViewById(R.id.button2);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                startActivity(intent);
            }
        });
    }
}
