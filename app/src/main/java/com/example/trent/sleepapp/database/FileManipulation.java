package com.example.trent.sleepapp.database;

import android.content.Context;
import android.content.SharedPreferences;
//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Trent on 2/27/2017.
 */

public class FileManipulation {
    private static final String ANDROID = "Android";
    private static final String DATA = "data";
    private static final String FILES = "files";
    public static final String PREFNAME = "userPrefs";


    public static void createDateFolder(Context context) {
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar c = Calendar.getInstance(tz);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        String currentDate = month + "-" + date + "-" + year;
        String packageName = context.getPackageName();
        File externalPath = Environment.getExternalStorageDirectory();
        File baseDirectory = new File(new File(new File(new File(
                externalPath, ANDROID), DATA), packageName), FILES);
        File dateDirectory = new File(baseDirectory, currentDate);
        dateDirectory.mkdir();
    }

    public static void pamJSON(Context context, int idx) throws JSONException {
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar c = Calendar.getInstance(tz);
        Date d = c.getTime();
        String[] dateString = d.toString().split(" ");
        String time = dateString[3];
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        String currentDate = month + "-" + date + "-" + year;
        JSONObject jsonObject = new JSONObject();
        SharedPreferences sp = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        String user = sp.getString("username", "DNE");
        jsonObject.put("UserName", user);
        jsonObject.put("ImagePosition", idx);
        String packageName = context.getPackageName();
        File externalPath = Environment.getExternalStorageDirectory();
        File f = new File (new File(new File(new File(new File(
                externalPath, ANDROID), DATA), packageName), FILES), currentDate);
        String fileName = "pamtest-" + time + ".json";
        File pamFile = new File(f, fileName);
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(pamFile));
            output.write(jsonObject.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void journalJSON(Context context, int hour, int minute, String type) throws JSONException {
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar c = Calendar.getInstance(tz);
        Date d = c.getTime();
        String[] dateString = d.toString().split(" ");
        String time = dateString[3];
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        String currentDate = month + "-" + date + "-" + year;
        JSONObject jsonObject = new JSONObject();
        SharedPreferences sp = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        String user = sp.getString("username", "DNE");
        jsonObject.put("UserName", user);
        jsonObject.put("Hour", hour);
        jsonObject.put("Minute", minute);
        String packageName = context.getPackageName();
        File externalPath = Environment.getExternalStorageDirectory();
        File f = new File (new File(new File(new File(new File(
                externalPath, ANDROID), DATA), packageName), FILES), currentDate);
        String fileName = type + "_journal-" + time + ".json";
        File pamFile = new File(f, fileName);
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(pamFile));
            output.write(jsonObject.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
