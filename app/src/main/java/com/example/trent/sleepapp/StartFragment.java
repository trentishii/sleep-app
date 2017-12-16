package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.trent.sleepapp.pvt.PVTHome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import static com.example.trent.sleepapp.pvt.RunTest.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class StartFragment extends Fragment implements View.OnClickListener {
//public class StartFragment extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean myParam;
    private OnFragmentInteractionListener mListener;

    private ArrayList<ImageView> check;
    private ArrayList<Button> buttons;
    private ArrayList<String> seqOrder;
    private ArrayList<Integer> randSeq;
    private ArrayList<String> testsDone;

    SharedPreferences buttonPrefs;
    public static final String BUTTONPREFNAME = "btnPrefs";


    public StartFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        buttonPrefs = getActivity().getSharedPreferences(BUTTONPREFNAME, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            myParam = getArguments().getBoolean("isEnabled");
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = buttonPrefs.edit();
        if (buttonPrefs.getBoolean("StartOfDay",false)) {
            randSeq = new ArrayList<>();
            randSeq.add(1);
            randSeq.add(1);
            randSeq.add(0);
            randSeq.add(0);
            Collections.shuffle(randSeq);
            for (int i=0; i<=3; i++) {
                editor.putInt("list"+i,randSeq.get(i));
                editor.commit();
                System.out.println("RAND SEQ ========= "+randSeq.get(i));
                System.out.println("SHARED PREFS =========== "+buttonPrefs.getInt("list"+i,0));
            }
            editor.putBoolean("StartOfDay",false);
            editor.commit();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!Start of day:" + buttonPrefs.getBoolean("StartOfDay",false));
        }
        for (int j=0; j<=3; j++) {
            System.out.println("\nShuffled List : ****************" + buttonPrefs.getInt("list"+j,0));
        }
        switch(v.getId()) {
            case R.id.bPAM:
            case R.id.b2PAM:
            case R.id.b3PAM:
            case R.id.b4PAM:
                Log.e("Start", "PAM Clicked");
                Intent intent = new Intent(getActivity(), PAMActivity.class);
                startActivity(intent);
                break;
            case R.id.bSSS:
            case R.id.b2SSS:
            case R.id.b3SSS:
            case R.id.b4SSS:
                Log.e("Start", "SSS Clicked");
                Intent intent1 = new Intent(getActivity(), SSSActivity.class);
                startActivity(intent1);
                break;
            case R.id.bPVT:
                Log.e("Start", "PVT Clicked");
                if (buttonPrefs.getInt("list0",0) == 0){
//                if (randSeq.get(0) == 0){
                    Intent intent2 = new Intent(getActivity(), PVTHome.class);
                    startActivity(intent2);}
                else {
                    Intent intent2 = new Intent(getActivity(), NBackStart.class);
                    startActivity(intent2);}
                break;
            case R.id.b2PVT:
                Log.e("Start", "PVT Clicked");
                if (buttonPrefs.getInt("list1",0) == 0){
//                if (randSeq.get(1) == 0){
                    Intent intent2 = new Intent(getActivity(), PVTHome.class);
                    startActivity(intent2);}
                else {
                    Intent intent2 = new Intent(getActivity(), NBackStart.class);
                    startActivity(intent2);}
                break;
            case R.id.b3PVT:
                Log.e("Start", "PVT Clicked");
                if (buttonPrefs.getInt("list2",0) == 0){
//                if (randSeq.get(2) == 0){
                    Intent intent2 = new Intent(getActivity(), PVTHome.class);
                    startActivity(intent2);}
                else {
                    Intent intent2 = new Intent(getActivity(), NBackStart.class);
                    startActivity(intent2);}
                break;
            case R.id.b4PVT:
                Log.e("Start", "PVT Clicked");
                if (buttonPrefs.getInt("list3",0) == 0){
//                if (randSeq.get(3) == 0){
                    Intent intent2 = new Intent(getActivity(), PVTHome.class);
                    startActivity(intent2);}
                else{
                    Intent intent2 = new Intent(getActivity(), NBackStart.class);
                    startActivity(intent2);}
                break;
            case R.id.bSleepLog:
                Log.e("Start", "Sleep Log Clicked");
                Intent intent3 = new Intent(getActivity(), SleepLogActivity.class);
                startActivity(intent3);
                break;
            case R.id.bLEEDS:
                Log.e("Start", "Leeds Clicked");
                Intent intent4 = new Intent(getActivity(), LeedsActivity.class);
                startActivity(intent4);
                break;
            case R.id.bPANAS:
                Log.e("Start", "Panas Clicked");
                Intent intent5 = new Intent(getActivity(), PanasActivity.class);
                startActivity(intent5);
                break;
            case R.id.bJournal:
                Log.e("Start", "Journal Clicked");
                Intent intent6 = new Intent(getActivity(), JournalActivity.class);
                startActivity(intent6);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        check = new ArrayList<>();
        check.add((ImageView) v.findViewById(R.id.checkPAM));
        check.add((ImageView) v.findViewById(R.id.check2PAM));
        check.add((ImageView) v.findViewById(R.id.check3PAM));
        check.add((ImageView) v.findViewById(R.id.check4PAM));
        check.add((ImageView) v.findViewById(R.id.checkSSS));
        check.add((ImageView) v.findViewById(R.id.check2SSS));
        check.add((ImageView) v.findViewById(R.id.check3SSS));
        check.add((ImageView) v.findViewById(R.id.check4SSS));
        check.add((ImageView) v.findViewById(R.id.checkPVT));
        check.add((ImageView) v.findViewById(R.id.check2PVT));
        check.add((ImageView) v.findViewById(R.id.check3PVT));
        check.add((ImageView) v.findViewById(R.id.check4PVT));
        check.add((ImageView) v.findViewById(R.id.checkJournal));
        check.add((ImageView) v.findViewById(R.id.checkSleepLog));
        check.add((ImageView) v.findViewById(R.id.checkPANAS));
        check.add((ImageView) v.findViewById(R.id.checkLEEDS));

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


        Button b = (Button) v.findViewById(R.id.bPAM);
        b.setEnabled(buttonPrefs.getBoolean("bPAM", true));
        b.setOnClickListener(this);
        Button b1 = (Button) v.findViewById(R.id.b2PAM);
        b1.setEnabled(buttonPrefs.getBoolean("b2PAM", true));
        b1.setOnClickListener(this);
        Button b2 = (Button) v.findViewById(R.id.b3PAM);
        b2.setEnabled(buttonPrefs.getBoolean("b3PAM", true));
        b2.setOnClickListener(this);
        Button b3 = (Button) v.findViewById(R.id.b4PAM);
        b3.setEnabled(buttonPrefs.getBoolean("b4PAM", true));
        b3.setOnClickListener(this);
        Button b4 = (Button) v.findViewById(R.id.bSSS);
        b4.setEnabled(buttonPrefs.getBoolean("bSSS", true));
        b4.setOnClickListener(this);
        Button b5 = (Button) v.findViewById(R.id.b2SSS);
        b5.setEnabled(buttonPrefs.getBoolean("b2SSS", true));
        b5.setOnClickListener(this);
        Button b6 = (Button) v.findViewById(R.id.b3SSS);
        b6.setEnabled(buttonPrefs.getBoolean("b3SSS", true));
        b6.setOnClickListener(this);
        Button b7 = (Button) v.findViewById(R.id.b4SSS);
        b7.setEnabled(buttonPrefs.getBoolean("b4SSS", true));
        b7.setOnClickListener(this);
        Button b8 = (Button) v.findViewById(R.id.bPVT);
        b8.setEnabled(buttonPrefs.getBoolean("bPVT", true));
        b8.setOnClickListener(this);
        Button b9 = (Button) v.findViewById(R.id.b2PVT);
        b9.setEnabled(buttonPrefs.getBoolean("b2PVT", true));
        b9.setOnClickListener(this);
        Button b10 = (Button) v.findViewById(R.id.b3PVT);
        b10.setEnabled(buttonPrefs.getBoolean("b3PVT", true));
        b10.setOnClickListener(this);
        Button b11 = (Button) v.findViewById(R.id.b4PVT);
        b11.setEnabled(buttonPrefs.getBoolean("b4PVT", true));
        b11.setOnClickListener(this);
        Button b12 = (Button) v.findViewById(R.id.bSleepLog);
        b12.setEnabled(buttonPrefs.getBoolean("bSleepLog", true));
        b12.setOnClickListener(this);
        Button b13 = (Button) v.findViewById(R.id.bLEEDS);
        b13.setEnabled(buttonPrefs.getBoolean("bLEEDS", true));
        b13.setOnClickListener(this);
        Button b14 = (Button) v.findViewById(R.id.bPANAS);
        b14.setEnabled(buttonPrefs.getBoolean("bPANAS", true));
        b14.setOnClickListener(this);
        Button b15 = (Button) v.findViewById(R.id.bJournal);
        b15.setEnabled(buttonPrefs.getBoolean("bJournal", true));
        b15.setOnClickListener(this);
        addCheck();
        return v;
    }

    public void addCheck(){

        String window = "null";
        for (int i=0; i<=15; i++) {
            if (i >= 0 && i <= 4) {
                window = "WakeTimeDone";
            }
            if (i >= 5 && i <= 7) {
                window = "DayTime1Done";
            }
            if (i >= 8 && i <= 10) {
                window = "DayTime2Done";
            }
            if (i >= 11 && i <= 15) {
                window = "SleepTimeDone";
            }
            if (buttonPrefs.getBoolean(testsDone.get(i),false)){
                check.get(i).setVisibility(View.VISIBLE);
            }
            else if (buttonPrefs.getBoolean(window, false)) {
                check.get(i).setVisibility(View.INVISIBLE);
            }
            else {
                check.get(i).setVisibility(View.INVISIBLE);
            }
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
}
//}