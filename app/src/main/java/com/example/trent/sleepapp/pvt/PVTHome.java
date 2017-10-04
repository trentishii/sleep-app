package com.example.trent.sleepapp.pvt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.trent.sleepapp.LoginActivity;
import com.example.trent.sleepapp.UserActivity;
import com.example.trent.sleepapp.pvt.PVTConfig.PVTConfigTest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.trent.sleepapp.R;

/**
 * Home screen for PVT
 *
 * @author Jonathan Schooler
 */

// Test tag: test
// Participant #: 1
// Test duration: 1 minute
// Foreperiod 1 to 2 seconds
// reminder at 9999 sec
// deadline 9999 sec
// rt feedback: off
// input type: touch down
public class PVTHome extends Activity {
    public final String TAG = getClass().getSimpleName();

    //fields
//	private EditText testTagInput;
//	private EditText subjectIdInput;
//  private EditText durationInput;
//	private EditText reminderInput;
//	private EditText deadlineInput;
//	private ToggleButton feedbackInput;
//	private Spinner inputTechniqueInput;
//	private EditText minForeperiodInput;
//	private EditText maxForeperiodInput;

    private PVTConfigParamsMap params;

    private PVTConfig config;

    private PVTConfigGetter configGetter;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.pvt_home);
//        setContentView(R.layout.fragment_start);
//      Intent in = new Intent(this, UserActivity.class);
//      startActivity(in);

// get params from the pvt_config.xml file
        configGetter = new PVTConfigGetter(this);
        config = configGetter.get();
        params = null;
        for (int i = 0; i < config.tests.size(); i++) {
            PVTConfigTest test = config.tests.get(i);
            if (test.type.equals("spot")) {
                params = test.testParams;
                break;
            }
        }
        if (params == null) {
            throw new RuntimeException("could not load configuration");
        }
        try {
            configGetter.saveConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //start test
        Intent i = new Intent(this, PVT.class);
        startActivity(i);
    }

}
    //get views 
//    testTagInput = (EditText) findViewById(R.id.testTagInput);
//    subjectIdInput = (EditText) findViewById(R.id.subjectIdInput);
//    durationInput = (EditText) findViewById(R.id.durationInput);
//    minForeperiodInput = (EditText) findViewById(R.id.minForeperiodInput);
//    maxForeperiodInput = (EditText) findViewById(R.id.maxForeperiodInput);
//    reminderInput = (EditText) findViewById(R.id.reminderInput);
//    deadlineInput = (EditText) findViewById(R.id.deadlineInput);
//    feedbackInput = (ToggleButton) findViewById(R.id.feedbackInput);
//    inputTechniqueInput = (Spinner) findViewById(R.id.inputTechniqueInput);

    //set up input techniques spinner
//    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//        this, R.array.input_techniques, android.R.layout.simple_spinner_item);
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    inputTechniqueInput.setAdapter(adapter);
//    Resources res = getResources();
//    final String[] inputTechniqueParams = res.getStringArray(R.array.input_techniques_values);	//names of the config params for turning on each input technique
//



    //populate views with current configuration
//    testTagInput.setText(params.getStringValueOrDefault(PVTStringParam.test_tag));
//    subjectIdInput.setText(Integer.toString(params.getIntValueOrDefault(PVTIntParam.subject_id)));
//    durationInput.setText(Integer.toString(params.getIntValueOrDefault(PVTIntParam.test_duration) / 60000));
//    minForeperiodInput.setText(Integer.toString(params.getIntValueOrDefault(PVTIntParam.min_delay) / 1000));
//    maxForeperiodInput.setText(Integer.toString(params.getIntValueOrDefault(PVTIntParam.max_delay) / 1000));
//    reminderInput.setText(Integer.toString(params.getIntValueOrDefault(PVTIntParam.reminder_delay) / 1000));
//    deadlineInput.setText(Integer.toString(params.getIntValueOrDefault(PVTIntParam.deadline) / 1000));
//    feedbackInput.setChecked(params.getBooleanValueOrDefault(PVTBooleanParam.visible_response_time_enabled));
//    for (int i = 0; i < inputTechniqueParams.length; i++) {
//    	String paramName = inputTechniqueParams[i];
//    	PVTBooleanParam param = PVTBooleanParam.valueOf(paramName);
//    	if (params.getBooleanValueOrDefault(param)) {
//    		inputTechniqueInput.setSelection(i);
//    	}
//    }
//
//    //set up views to change settings
//    testTagInput.addTextChangedListener(new StringInputWatcher(PVTStringParam.test_tag));
//    setupNumericInput(subjectIdInput, PVTIntParam.subject_id, 1);
////    setupNumericInput(durationInput, PVTIntParam.test_duration, 60000);
//    setupNumericInput(minForeperiodInput, PVTIntParam.min_delay, 1000);
//    setupNumericInput(maxForeperiodInput, PVTIntParam.max_delay, 1000);
//    setupNumericInput(reminderInput, PVTIntParam.reminder_delay, 1000);
//    setupNumericInput(deadlineInput, PVTIntParam.deadline, 1000);
//    feedbackInput.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				params.put(PVTBooleanParam.visible_response_time_enabled, Boolean.toString(isChecked));
//			}
//		});
//    inputTechniqueInput.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		    for (int i = 0; i < inputTechniqueParams.length; i++) {
//		    	String paramName = inputTechniqueParams[i];
//		    	PVTBooleanParam param = PVTBooleanParam.valueOf(paramName);
//		    	params.put(param, Boolean.toString(i == inputTechniqueInput.getSelectedItemPosition()));
//		    }
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//		    for (int i = 0; i < inputTechniqueParams.length; i++) {
//		    	String paramName = inputTechniqueParams[i];
//		    	PVTBooleanParam param = PVTBooleanParam.valueOf(paramName);
//		    	params.put(param, "false");
//		    }
//			}
//		});

//  }

//  private void setupNumericInput(final EditText input, PVTIntParam param, int multiplier) {
//    input.addTextChangedListener(new NumericInputWatcher(param, multiplier));
//    input.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				Editable e = input.getText();
//				if (!hasFocus && e.toString().equals("")) {
//					e.clear();
//					e.append("1");
//				}
//			}
//		});
//  }
  
//  public void takeTest(View v) throws IOException {
//  	//save settings
//  	configGetter.saveConfigFile();
//
//  	//start test
//    Intent i = new Intent(this, PVT.class);
//    startActivity(i);
//  }

//  public void returnHome(View v) throws IOException {
//      SharedPreferences buttonPrefs = getSharedPreferences("btnPrefs", Context.MODE_PRIVATE);
//      SharedPreferences.Editor editor = buttonPrefs.edit();
//
//      Date d = Calendar.getInstance().getTime();
//      String[] dateString = d.toString().split(" ");
//      String noon = "12:00:00";
//      String evening = "18:00:00";
//      String bedtime = "20:00:00";
//      String pattern = "HH:mm:ss";
//      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
//      try {
//          if (dateFormat.parse(dateString[3]).before(dateFormat.parse(noon))) {
//              editor.putBoolean("bPVT", false);
//              editor.putBoolean("WakeTestDone", true);
//              editor.commit();
//          } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(noon)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(evening))) {
//              editor.putBoolean("b2PVT", false);
//              editor.putBoolean("DayTime1Done", true);
//              editor.commit();
//          } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(evening)) && dateFormat.parse(dateString[3]).before(dateFormat.parse(bedtime))) {
//              editor.putBoolean("b3PVT", false);
//              editor.putBoolean("DayTime2Done", true);
//              editor.commit();
//          } else if (dateFormat.parse(dateString[3]).after(dateFormat.parse(bedtime))) {
//              editor.putBoolean("b4PVT", false);
//              editor.putBoolean("SleepTimeDone", true);
//              editor.commit();
//          }
//      }catch (Exception e) {
//          e.printStackTrace();        }
//
//	  Intent i = new Intent(this, UserActivity.class);
//	  startActivity(i);
//  }
  
//  private class NumericInputWatcher implements TextWatcher {
//
//  	private final PVTIntParam paramName;
//		private final int multiplier;
//
//		public NumericInputWatcher(PVTIntParam paramName, int multiplier) {
//			this.paramName = paramName;
//			this.multiplier = multiplier;
//		}
  	
//		@Override
//		public void afterTextChanged(Editable s) {
//			if (s.toString().equals("")) return;
//
//			try {
//				int value = Integer.parseInt(s.toString());
//				if (value < 1) throw new NumberFormatException();
//
//				params.put(paramName, Integer.toString(value * multiplier));
//			}
//			catch (NumberFormatException e) {
//				s.clear();
//				s.append("1");
//			}
//		}

//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//		}
  	
//  }



//  private class StringInputWatcher implements TextWatcher {
//
//  	private final PVTStringParam paramName;
//
//  	public StringInputWatcher(PVTStringParam paramName) {
//			this.paramName = paramName;
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			params.put(paramName, s.toString().trim());
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//		}
//
//  }
//}
