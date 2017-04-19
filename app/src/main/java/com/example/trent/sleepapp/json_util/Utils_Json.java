package com.example.trent.sleepapp.json_util;

/**
 * Created by Trent on 2/13/2017.
 */

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class Utils_Json {

    public static String toJSon(Bundle bundle) {
        try {
            //Retrieve all bundles
            JSONObject json_obj = new JSONObject();

            JSONObject json_subject_info = new JSONObject(); // we need another object to store the address
            Set<String> subject_info_strings = bundle.keySet();
            for (String info_string : subject_info_strings) {
                try {
                    json_subject_info.put(info_string, bundle.getString(info_string));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // We add the object to the main object
            json_obj.put(bundle.getString("packet"), json_subject_info);

            return json_obj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}