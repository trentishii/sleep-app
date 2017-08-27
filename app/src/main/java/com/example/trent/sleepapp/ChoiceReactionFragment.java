//package com.example.trent.sleepapp;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.main.inebrirate.MainActivity;
//import com.main.inebrirate.R;
//import com.main.utils.FileStreamManager;
//
//import java.util.ArrayList;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class ChoiceReactionFragment extends Fragment {
//
//    private Button startButton;
//    private ImageView recordingLight;
//    private ArrayList<ImageView> reactionImageViews;
//    private ArrayList<Integer> greenTimes, redTimes, imageViewChoices;
//
//    private FileStreamManager targetFile, touchFile, sensorFile;
//
//    private long startTime;
//
//    public ChoiceReactionFragment() {}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.choice_reaction, container, false);
//        reactionImageViews = new ArrayList<>();
//
//        startButton = (Button) rootView.findViewById(R.id.choiceReactionStartButton);
//        recordingLight = (ImageView) rootView.findViewById(R.id.choiceReactionRecordingLight);
//        reactionImageViews.add((ImageView) rootView.findViewById(R.id.choiceReactionImageView1));
//        reactionImageViews.add((ImageView) rootView.findViewById(R.id.choiceReactionImageView2));
//        reactionImageViews.add((ImageView) rootView.findViewById(R.id.choiceReactionImageView3));
//        reactionImageViews.add((ImageView) rootView.findViewById(R.id.choiceReactionImageView4));
//
//        startButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (startButton.getText().toString().equals("Start")) {
//                    startButton.setText("");
//                    recordingLight.setVisibility(View.VISIBLE);
//                    start();
//                }
//            }
//        });
//
//        return rootView;
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean visible) {
//        super.setUserVisibleHint(visible);
//        if (visible) prepare();
//    }
//
//    private void prepare() {
//        // UI
//        Random r = new Random();
//        greenTimes = new ArrayList<>();
//        redTimes = new ArrayList<>();
//        imageViewChoices = new ArrayList<>();
//        int min, max;
//        for (int i=0; i<10; i++) {
//            min = (3+8*i)*1000; max = min+3*1000;
//            greenTimes.add(r.nextInt((max - min) + 1) + min);
//            min += 4*1000; max = min+3*1000;
//            redTimes.add(r.nextInt((max - min) + 1) + min);
//            imageViewChoices.add(r.nextInt(reactionImageViews.size()));
//        }
//        startButton.setText("Start");
//        for (ImageView iv: reactionImageViews)
//            iv.setBackgroundColor(Color.RED);
//        recordingLight.setImageResource(R.drawable.recorder_off);
//
//        // Sensors
//        MainActivity.startSensorService();
//
//        // Files
//        sensorFile = new FileStreamManager(MainActivity.folderName + "user" + MainActivity.userId + "/" + MainActivity.sessionFolderName + "/choice_reaction_sensors.csv");
//        targetFile = new FileStreamManager(MainActivity.folderName + "user" + MainActivity.userId + "/" + MainActivity.sessionFolderName + "/choice_reaction_target.csv");
//        touchFile = new FileStreamManager(MainActivity.folderName + "user" + MainActivity.userId + "/" + MainActivity.sessionFolderName + "/choice_reaction_touch.csv");
//        for (int i=0; i<greenTimes.size(); i++)
//            targetFile.output((double)greenTimes.get(i)/1000 + "," + (double)redTimes.get(i)/1000 + "," + imageViewChoices.get(i));
//    }
//
//    public void start() {
//        Log.d("ALEX", "beginning choice_reaction test " + MainActivity.testId);
//
//        // UI
//        for (int i=0; i<4; i++) {
//            final int idx = i;
//            reactionImageViews.get(idx).setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    double time = ((double) (System.currentTimeMillis() - startTime) / 1000);
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            touchFile.output("down, " + time + "," + idx);
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            touchFile.output("up, " + time + "," + idx);
//                            break;
//                    }
//
//                    return true;
//                }
//            });
//        }
//
//        for (int i=0; i<greenTimes.size(); i++) {
//            final int idx = imageViewChoices.get(i);
//            Timer timer = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            reactionImageViews.get(idx).setBackgroundColor(Color.GREEN);
//                        }
//                    });
//                }
//            };
//            timer.schedule(task, greenTimes.get(i));
//        }
//        for (int i=0; i<redTimes.size(); i++) {
//            final int idx = imageViewChoices.get(i);
//            Timer timer = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            reactionImageViews.get(idx).setBackgroundColor(Color.RED);
//                        }
//                    });
//                }
//            };
//            timer.schedule(task, redTimes.get(i));
//        }
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        end();
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
//                });
//            }
//        };
//        timer.schedule(task, redTimes.get(redTimes.size()-1)+3000);
//        recordingLight.setImageResource(R.drawable.recorder_on);
//
//        // Sensors
//        MainActivity.startRecordingSensors();
//        startTime = System.currentTimeMillis();
//    }
//
//    public void end() {
//        Log.d("ALEX", "ending choice_reaction test " + MainActivity.testId);
//
//        // Sensors
//        MainActivity.stopSensorService(sensorFile);
//
//        // UI
//        recordingLight.setImageResource(R.drawable.recorder_off);
//    }
//
//}