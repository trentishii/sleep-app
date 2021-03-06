package com.example.trent.sleepapp.pvt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.trent.sleepapp.R;
import com.example.trent.sleepapp.UserActivity;
import com.example.trent.sleepapp.pvt.*;

import static java.lang.System.out;

//import com.pvt.R;

/**
 * Runs a set of tests and records results to the DataManager
 *
 * @author Jonathan Schooler
 *
 */
public class PVT extends Activity {

	/** Strings allocated in beginning to prevent garbage collection. */
	public static final String TAG = "PVT";
	public static final String PREFS_NAME = "pvt_settings";
	private static final String TEST = "test";
	private static final String STUDY_ID = "study_id";
	private static final String DEFAULT_STUDY = "PVT";
	private static final String DATE_FORMAT = "yyyy-MM-dd_kk.mm.ss";		//modified ISO date format
	private static final String UTEST = "_test";

	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.pvt);

		prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * Start a test
	 * @param v Button that was clicked
	 */
	public void begin(View v) {    // called by readyButton.onClick()
		Intent run = new Intent(this, RunTest.class);
		startActivityForResult(run, 0);
	}

	/**
	 * When we are finished with our PVT test, this is called
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {

			Test test = (Test) data.getSerializableExtra(TEST);
			test.finishTimestamp = System.currentTimeMillis();

			String studyId = prefs.getString(STUDY_ID, DEFAULT_STUDY);
			DataStorage store = new DataStorage(this, studyId);
			List<String> dataLines = new ArrayList<String>();

			for (int i = 0; i < test.trials.size(); i++) {
				Trial t = test.trials.get(i);
				t.trialNum = i;
				t.testNum = test.testNum;
				dataLines.add(t.toCSVLine());
			}
			

			if (test.trials.size() > 0) {
				long time = test.trials.get(0).timeStamp;
				// example: 06-03-2011_2359 for 11:59pm on June 3, 2011.
				CharSequence date = DateFormat.format(DATE_FORMAT, time);
				String filename = date + UTEST + test.testNum;
				store.writeToCSVs(dataLines, filename);
//				store.writeToCSVs(test.eventLog, filename + "_event_log");
				store.writeSummaryCSVs(new SummaryCalculator(test).calculateSummaryStats());
			}

			
			//Return the result of this PVT test as a CSV
			StringBuilder csvValue = new StringBuilder();
			csvValue.append(Trial.headerCSV());
			csvValue.append('\n');
			for (String line: dataLines) {
				csvValue.append(line);
				csvValue.append('\n');
			}
			Intent intent = new Intent();
			intent.putExtra("value", csvValue.toString());
			setResult(RESULT_OK, intent);
			finish();

			SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
		  SharedPreferences.Editor editor = buttonPrefs.edit();

		  Date d = Calendar.getInstance().getTime();
		  String[] dateString = d.toString().split(" ");
		  String pattern = "HH:mm:ss";
		  SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				if (dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("noon",null)))) {
//					editor.putBoolean("bPVT", false);
					editor.putBoolean("PVTDone", true);
//					editor.putBoolean("WakeTimeDone", true);
					editor.commit();
				} else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("noon",null))) && dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("evening",null)))) {
//					editor.putBoolean("b2PVT", false);
					editor.putBoolean("PVT2Done", true);
//					editor.putBoolean("DayTime1Done", true);
					editor.commit();
				} else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("evening",null))) && dateFormat.parse(dateString[3]).before(dateFormat.parse(buttonPrefs.getString("bedtime",null)))) {
//					editor.putBoolean("b3PVT", false);
					editor.putBoolean("PVT3Done", true);
//					editor.putBoolean("DayTime2Done", true);
					editor.commit();
				} else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(buttonPrefs.getString("bedtime",null)))) {
//					editor.putBoolean("b4PVT", false);
					editor.putBoolean("PVT4Done", true);
//					editor.putBoolean("SleepTimeDone", true);
					editor.commit();
				}
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Intent in = new Intent(this, UserActivity.class);
			finish();
			startActivity(in);

		}
	}
}
