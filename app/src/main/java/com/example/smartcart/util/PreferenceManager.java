package com.example.smartcart.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smartcart.Store;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PreferenceManager {

    private static final String PREFERENCE_TAG = "com.example.sharedprefexample";

    private SharedPreferences sharedPreferences;

    private static PreferenceManager preferenceManager = null;

    private Gson gson;

    private PreferenceManager(Context applicationContext){
        gson = new Gson();;
        sharedPreferences =applicationContext.getApplicationContext().getSharedPreferences(PREFERENCE_TAG, Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance(Context applicationContext){
        if(preferenceManager == null)
            preferenceManager = new PreferenceManager(applicationContext);
        return preferenceManager;
    }

    public  boolean saveString(String key, String value){
        return sharedPreferences.edit().putString(key,value).commit();
    }

    public String fetchString(String key){
        return sharedPreferences.getString(key, null);
    }



    public boolean saveObjects( String key, Object value){
        return saveString(key, gson.toJson(value));
    }

    public Object fetchObjects(String key, Class targetClass){
        Object objectFromPreference = fetchString(key);
        if(objectFromPreference != null){
            objectFromPreference = gson.fromJson((String)objectFromPreference, targetClass);
        }
        return objectFromPreference;
    }

    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public ArrayList<String> getArrayList(String key){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveStoresList(String key, ArrayList<Store> stores){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String stringValue = gson.toJson(stores);
        editor.putString(key,stringValue);
        editor.apply();
    }

    public ArrayList<Store> getStoresList(String key){
        Gson gson = new Gson();
        ArrayList<Store> stores = gson.fromJson(sharedPreferences.getString(key,null), new TypeToken<ArrayList<Store>>() {}.getType());
        return stores;
    }

    public void deletepreference(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String k : sharedPreferences.getAll().keySet()) {
            if (key.contains(key)) {
                editor.remove(k);
            }
        }
        editor.commit();
    }

}
