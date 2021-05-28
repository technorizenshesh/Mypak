package com.mypakuser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mypakuser.models.ModelLogin;

import java.util.ArrayList;

public class SharedPref {

    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor myPrefsEditor;
    public static SharedPref myObj;

    public SharedPref() {}

    public static SharedPref getInstance(Context context) {

        if (myObj == null) {
            myObj = new SharedPref();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            myPrefsEditor = myPrefs.edit();
        }

        return myObj;

    }

    public void setlanguage(String key,String value) {
        SharedPreferences.Editor myPrefEditor = myPrefs.edit();
        myPrefEditor.putString(key,value);
        myPrefEditor.commit();
    }

    public String getLanguage(String key) {
        return myPrefs.getString(key,"");
    }

    public void setLatLngList(String key, String value) {
        SharedPreferences.Editor myPrefEditor = myPrefs.edit();
        myPrefEditor.putString(key,value);
        myPrefEditor.commit();
    }

    public ArrayList<LatLng> getLatLngList(String key) {
        return new Gson().fromJson(myPrefs.getString(key,""),new TypeToken<ArrayList<LatLng>>() {}.getType());
    }

    public void setSharedType(String key, String value) {
        SharedPreferences.Editor myPrefEditor = myPrefs.edit();
        myPrefEditor.putString(key,value);
        myPrefEditor.commit();
    }

    public String getSharedType(String key) {
        return myPrefs.getString(key,"");
    }

    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor myPrefEditor = myPrefs.edit();
        myPrefEditor.putBoolean(key,value);
        myPrefEditor.commit();
    }

    public boolean getBooleanValue(String key) {
        return myPrefs.getBoolean(key,false);
    }

    public void setUserDetails(String Key, ModelLogin loginModel){
        SharedPreferences.Editor myPrefEditor = myPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginModel);
        myPrefEditor.putString(Key,json);
        myPrefEditor.commit();
    }

    public ModelLogin getUserDetails(String Key) {
        Gson gson = new Gson();
        String json = myPrefs.getString(Key,"");
        ModelLogin obj = gson.fromJson(json, ModelLogin.class);
        return obj;
    }

    public void clearAllPreferences() {
        myPrefsEditor = myPrefs.edit();
        myPrefsEditor.clear();
        myPrefsEditor.commit();
    }

    public void clearPreference(String Key) {
        myPrefsEditor.remove(Key);
        myPrefsEditor.commit();
    }


}
