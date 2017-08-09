package com.example.trent.sleepapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.Time;
import java.util.Calendar;
import java.util.Set;

import static com.example.trent.sleepapp.LoginActivity.PREFNAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CheckBox tobacco;
    private CheckBox exercise;
    private CheckBox alcohol;
    private CheckBox food;
    private CheckBox medicine;
    private CheckBox coffee;
    private Set<String> journals;
    private TimePicker wakeup;
    private TimePicker sleep;
    private FirebaseUser user;

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private PendingIntent pendingLight;
    private PendingIntent cancellationPendingIntent;


    private OnFragmentInteractionListener mListener;
    SharedPreferences sharedPrefs;
//    public DatabaseReference myRef;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPrefs = this.getActivity().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        journals = sharedPrefs.getStringSet("journals", null);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Button b = (Button) v.findViewById(R.id.bSettings);
        b.setOnClickListener(this);

        // Check Buttons
        tobacco = (CheckBox) v.findViewById(R.id.cbTobacco);
        exercise = (CheckBox) v.findViewById(R.id.cbExercise);
        alcohol = (CheckBox) v.findViewById(R.id.cbAlcohol);
        food = (CheckBox) v.findViewById(R.id.cbFood);
        medicine = (CheckBox) v.findViewById(R.id.cbMedicine);
        coffee = (CheckBox) v.findViewById(R.id.cbCoffee);
        wakeup = (TimePicker) v.findViewById(R.id.timePicker1);
        sleep = (TimePicker) v.findViewById(R.id.timePicker2);
//        for (String type: journals) {
//            Log.d("Settings", journals.toString());
//            if (type.equals("Tobacco")) {
//                Log.d("Settings", "Here");
//                tobacco.setChecked(true);
//            } else if (type.equals("Exercise")) {
//                exercise.setChecked(true);
//            } else if (type.equals("Alcohol")) {
//                alcohol.setChecked(true);
//            } else if (type.equals("Food")) {
//                food.setChecked(true);
//            } else if (type.equals("Medicine")) {
//                medicine.setChecked(true);
//            } else if (type.equals("Coffee")) {
//                coffee.setChecked(true);
//            }
//        }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bSettings:
                // Store Time Settings
                String name = user.getEmail();
                String[] newName = name.split("@");

                int wakeHour,wakeMin, sleepHour, sleepMin;
                if (Build.VERSION.SDK_INT >= 23 ) {
                    wakeHour = wakeup.getHour();
                    wakeMin = wakeup.getMinute();
                    sleepHour = sleep.getHour();
                    sleepMin = sleep.getMinute();
                }else {
                    wakeHour = wakeup.getCurrentHour();
                    wakeMin = wakeup.getCurrentMinute();
                    sleepHour = sleep.getCurrentHour();
                    sleepMin = sleep.getCurrentMinute();
                }
                UserSchedule entry = new UserSchedule(wakeHour, wakeMin, sleepHour, sleepMin);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(newName[0]);

                myRef.child("Schedule").setValue(entry);

                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                FirebaseToken entry_token = new FirebaseToken(refreshedToken);
                myRef.child("Firebase Token").setValue(entry_token);

                // Calculate dates
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, sleepHour);
                calSet.set(Calendar.MINUTE, sleepMin);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);
                if(calSet.compareTo(calNow) <= 0){
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }

                Calendar calStop = (Calendar) calNow.clone();
                calStop.set(Calendar.HOUR_OF_DAY, wakeHour);
                calStop.set(Calendar.MINUTE, wakeMin);
                calStop.set(Calendar.SECOND, 0);
                calStop.set(Calendar.MILLISECOND, 0);
                if(calStop.compareTo(calNow) <= 0){
                    //Today Set time passed, count to tomorrow
                    calStop.add(Calendar.DATE, 1);
                }

                Log.d("TEST", "" + SystemClock.elapsedRealtime());
                Log.d("Calendar Date", "" + calSet.getTimeInMillis());
                // Start data logging services
                alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                Intent intent = new Intent(getActivity().getApplicationContext(), AccLogService.class);
                pendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent lightInt = new Intent(getActivity().getApplicationContext(), LightLogService.class);
                pendingLight = PendingIntent.getService(getActivity().getApplicationContext(), 0, lightInt, PendingIntent.FLAG_UPDATE_CURRENT);
                //

                //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pendingLight);
                //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingLight);

                Intent cancellationIntent = new Intent(getActivity().getApplicationContext(), CancelAlarmService.class);
                //cancellationIntent.putExtra("key", pendingIntent);
                cancellationIntent.putExtra("light", pendingLight);
                cancellationPendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                alarmManager.cancel(pendingLight);
                alarmManager.cancel(cancellationPendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingLight);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calStop.getTimeInMillis(), cancellationPendingIntent);

//
//                if (!tobacco.isChecked() && journals.contains("Tobacco")) {
//                    journals.remove("Tobacco");
//                }
//                if (tobacco.isChecked() && !journals.contains("Tobacco")) {
//                    journals.add("Tobacco");
//                }
//
//                if (!exercise.isChecked() && journals.contains("Exercise")) {
//                    journals.remove("Exercise");
//                }
//                if (exercise.isChecked() && !journals.contains("Exercise")) {
//                    journals.add("Exercise");
//                }
//
//
//                if (!alcohol.isChecked() && journals.contains("Alcohol")) {
//                    journals.remove("Alcohol");
//                }
//                if (alcohol.isChecked() && !journals.contains("Alcohol")) {
//                    journals.add("Alcohol");
//                }
//
//
//                if (!food.isChecked() && journals.contains("Food")) {
//                    journals.remove("Food");
//                }
//                if (food.isChecked() && !journals.contains("Food")) {
//                    journals.add("Food");
//                }
//
//
//                if (!medicine.isChecked() && journals.contains("Medicine")) {
//                    journals.remove("Medicine");
//                }
//                if (medicine.isChecked() && !journals.contains("Medicine")) {
//                    journals.add("Medicine");
//                }
//
//                if (!coffee.isChecked() && journals.contains("Coffee")) {
//                    journals.remove("Coffee");
//                }
//                if (coffee.isChecked() && !journals.contains("Coffee")) {
//                    journals.add("Coffee");
//                }


                Toast.makeText(getActivity(),"Settings Submitted!",Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    public String getName() {
//
//    }
}
