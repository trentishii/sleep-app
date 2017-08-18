package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trent.sleepapp.pvt.PVTHome;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bPAM:
            case R.id.b2PAM:
            case R.id.b3PAM:
                Log.e("Start", "PAM Clicked");
                Intent intent = new Intent(getActivity(), PAMActivity.class);
                startActivity(intent);
                break;
            case R.id.bSSS:
            case R.id.b2SSS:
            case R.id.b3SSS:
                Log.e("Start", "SSS Clicked");
                Intent intent1 = new Intent(getActivity(), SSSActivity.class);
                startActivity(intent1);
                break;
            case R.id.bPVT:
            case R.id.b2PVT:
            case R.id.b3PVT:
                Log.e("Start", "PVT Clicked");
                Intent intent2 = new Intent(getActivity(), PVTHome.class);
                startActivity(intent2);
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
        Button b = (Button) v.findViewById(R.id.bPAM);
        b.setOnClickListener(this);
        Button b1 = (Button) v.findViewById(R.id.b2PAM);
        b1.setOnClickListener(this);
        Button b2 = (Button) v.findViewById(R.id.b3PAM);
        b2.setOnClickListener(this);
        Button b3 = (Button) v.findViewById(R.id.bSSS);
        b3.setOnClickListener(this);
        Button b4 = (Button) v.findViewById(R.id.b2SSS);
        b4.setOnClickListener(this);
        Button b5 = (Button) v.findViewById(R.id.b3SSS);
        b5.setOnClickListener(this);
        Button b6 = (Button) v.findViewById(R.id.bPVT);
        b6.setOnClickListener(this);
        Button b7 = (Button) v.findViewById(R.id.b2PVT);
        b7.setOnClickListener(this);
        Button b8 = (Button) v.findViewById(R.id.b3PVT);
        b8.setOnClickListener(this);
        Button b9 = (Button) v.findViewById(R.id.bSleepLog);
        b9.setOnClickListener(this);
        Button b10 = (Button) v.findViewById(R.id.bLEEDS);
        b10.setOnClickListener(this);
        Button b11 = (Button) v.findViewById(R.id.bPANAS);
        b11.setOnClickListener(this);
        Button b12 = (Button) v.findViewById(R.id.bJournal);
        b12.setOnClickListener(this);
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
