package com.example.trent.sleepapp;

import android.app.ActionBar;
import android.content.ClipData;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static com.example.trent.sleepapp.LoginActivity.BUTTONPREFNAME;
import static com.example.trent.sleepapp.LoginActivity.PREFNAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JournalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Set<String> journals;
    private GridView gridview;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPrefs;
    SharedPreferences buttonPrefs;
    public static final String BUTTONPREFNAME = "btnPrefs";
    public static final String PREFNAME = "userPrefs";
    private Context cont;
    public JournalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JournalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JournalFragment newInstance(String param1, String param2) {
        JournalFragment fragment = new JournalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        SharedPreferences sharedPrefs;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        buttonPrefs = getActivity().getSharedPreferences(BUTTONPREFNAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = buttonPrefs.edit();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPrefs = JournalFragment.this.getActivity().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        journals = sharedPrefs.getStringSet("journals", null);
        cont = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_journal, container, false);

        LinearLayout linear1 = (LinearLayout) v.findViewById(R.id.linear1);
        LinearLayout linear2 = (LinearLayout) v.findViewById(R.id.linear2);
        LinearLayout linear3 = (LinearLayout) v.findViewById(R.id.linear3);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int count = 0;
        sharedPrefs = this.getActivity().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = buttonPrefs.edit();

        for (String act : journals) {
            Log.d("JournalFragment", act + " " + count);
            if (act.equals("Tobacco")) {
                final ImageButton ib = new ImageButton(this.getActivity());
                ib.setImageResource(R.drawable.tobacco);
                ib.setEnabled(buttonPrefs.getBoolean("bSmoke", true));
                if (! buttonPrefs.getBoolean("bSmoke", true)){
                    ib.setBackgroundResource(R.drawable.check);}
                ib.setLayoutParams(params);
                if (count == 0 || count == 1) {
                    linear1.addView(ib);
                } else if (count == 2 || count == 3) {
                    linear2.addView(ib);
                } else if (count == 4 || count == 5) {
                    linear3.addView(ib);
                } ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("JournalFragment", "tobacco Clicked");
                        JournalFragment.this.setPreferences(sharedPrefs, 0);
                        Intent intent = new Intent (getActivity().getApplicationContext(), EventLog.class);
                        startActivity(intent);
                        editor.putBoolean("bSmoke", false);
                        editor.commit();
                    }
                });
            }
            if (act.equals("Medicine")) {
                final ImageButton ib = new ImageButton(this.getActivity());
                ib.setImageResource(R.drawable.pill);
                ib.setEnabled(buttonPrefs.getBoolean("bMedicine", true));
                if (! buttonPrefs.getBoolean("bMedicine", true)){
                    ib.setBackgroundResource(R.drawable.check);}
                ib.setLayoutParams(params);
                if (count == 0 || count == 1) {
                    linear1.addView(ib);
                } else if (count == 2 || count == 3) {
                    linear2.addView(ib);
                } else if (count == 4 || count == 5) {
                    linear3.addView(ib);
                }                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("JournalFragment", "pill Clicked");
                        setPreferences(sharedPrefs, 5);
                        Intent intent = new Intent (JournalFragment.this.getActivity(), EventLog.class);
                        startActivity(intent);
                        editor.putBoolean("bMedicine", false);
                        editor.commit();
                    }
                });
            }
            if (act.equals("Food")) {
                final ImageButton ib = new ImageButton(this.getActivity());
                ib.setImageResource(R.drawable.meal);
                ib.setEnabled(buttonPrefs.getBoolean("bMeal", true));
                if (! buttonPrefs.getBoolean("bMeal", true)){
                    ib.setBackgroundResource(R.drawable.check);}
                ib.setLayoutParams(params);
                if (count == 0 || count == 1) {
                    linear1.addView(ib);
                } else if (count == 2 || count == 3) {
                    linear2.addView(ib);
                } else if (count == 4 || count == 5) {
                    linear3.addView(ib);
                }                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("JournalFragment", "meal Clicked");
                        setPreferences(sharedPrefs, 3);
                        editor.putBoolean("bMeal", false);
                        editor.commit();
                        Intent intent = new Intent (JournalFragment.this.getActivity(), EventLog.class);
                        startActivity(intent);

                    }
                });
            }
            if (act.equals("Alcohol")) {
                final ImageButton ib = new ImageButton(this.getActivity());
                ib.setImageResource(R.drawable.alcohol);
                ib.setEnabled(buttonPrefs.getBoolean("bAlcohol", true));
                if (! buttonPrefs.getBoolean("bAlcohol", true)){
                    ib.setBackgroundResource(R.drawable.check);}
                ib.setLayoutParams(params);
                if (count == 0 || count == 1) {
                    linear1.addView(ib);
                } else if (count == 2 || count == 3) {
                    linear2.addView(ib);
                } else if (count == 4 || count == 5) {
                    linear3.addView(ib);
                }                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("JournalFragment", "alcohol Clicked");
                        setPreferences(sharedPrefs, 4);
                        Intent intent = new Intent (JournalFragment.this.getActivity(), EventLog.class);
                        startActivity(intent);
                        editor.putBoolean("bAlcohol", false);
                        editor.commit();
                    }
                });
            }
            if (act.equals("Exercise")) {
                final ImageButton ib = new ImageButton(this.getActivity());
                ib.setImageResource(R.drawable.exercise);
                ib.setEnabled(buttonPrefs.getBoolean("bExercise", true));
                if (! buttonPrefs.getBoolean("bExercise", true)){
                    ib.setBackgroundResource(R.drawable.check);}
                ib.setLayoutParams(params);
                if (count == 0 || count == 1) {
                    linear1.addView(ib);
                } else if (count == 2 || count == 3) {
                    linear2.addView(ib);
                } else if (count == 4 || count == 5) {
                    linear3.addView(ib);
                }                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("JournalFragment", "exercise Clicked");
                        setPreferences(sharedPrefs, 2);
                        Intent intent = new Intent (JournalFragment.this.getActivity(), EventLog.class);
                        startActivity(intent);
                        editor.putBoolean("bExercise", false);
                        editor.commit();
                    }
                });
            }
            if (act.equals("Coffee")) {
                final ImageButton ib = new ImageButton(this.getActivity());
                ib.setImageResource(R.drawable.coffee);
                ib.setEnabled(buttonPrefs.getBoolean("bCoffee", true));
                if (! buttonPrefs.getBoolean("bCoffee", true)){
                ib.setBackgroundResource(R.drawable.check);}
                ib.setLayoutParams(params);
                if (count == 0 || count == 1) {
                    linear1.addView(ib);
                } else if (count == 2 || count == 3) {
                    linear2.addView(ib);
                } else if (count == 4 || count == 5) {
                    linear3.addView(ib);
                }                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("JournalFragment", "coffee Clicked");
                        setPreferences(sharedPrefs, 1);
                        Intent intent = new Intent (JournalFragment.this.getActivity(), EventLog.class);
                        startActivity(intent);
                        editor.putBoolean("bCoffee", false);
                        editor.commit();
                    }
                });
            }
            count++;
        }
        Button btn3 = (Button)v.findViewById(R.id.buttonDone);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                String[] dateString = d.toString().split(" ");
                String pattern = "HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                try {
                    if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("bedtime",null)))) {
                        editor.putBoolean("bJournal", false);
                        editor.putBoolean("JournalDone" , true);
                        editor.putBoolean("SleepTimeDone", true);
                        editor.putBoolean("bMedicine", true);
                        editor.putBoolean("bMeal",true);
                        editor.putBoolean("bCoffee",true);
                        editor.putBoolean("bExercise",true);
                        editor.putBoolean("bSmoke",true);
                        editor.putBoolean("bAlcohol",true);
                        editor.commit();
                    }
                }catch (Exception e) {
                    e.printStackTrace();        }

                Intent intent = new Intent(getActivity().getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
        });
        return v;
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

    public void setPreferences(SharedPreferences sp, int value) {
        SharedPreferences.Editor editorS = sp.edit();
        editorS.putInt("EventType", value);
        editorS.commit();
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
