package com.example.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.smartcart.util.PreferenceManager;

public class HelperActivity extends Activity {

    String firstRun;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getInstance(HelperActivity.this).saveBoolean("FirstRun",false);
        String firstRun = String.valueOf(PreferenceManager.getInstance(HelperActivity.this).fetchBoolean("FirstRun"));
        Log.d("FirstRunValueOnCreate",firstRun);
        Intent intent = new Intent(HelperActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
        String firstRun = String.valueOf(PreferenceManager.getInstance(HelperActivity.this).fetchBoolean("FirstRun"));
        Log.d("FirstRunValueOnResume",firstRun);
        if(PreferenceManager.getInstance(HelperActivity.this).fetchBoolean("FirstRun")!=false){
            PreferenceManager.getInstance(HelperActivity.this).saveBoolean("FirstRun",false);
            Intent intent = new Intent(HelperActivity.this,MainActivity.class);
            startActivity(intent);
        }else{
            PreferenceManager.getInstance(HelperActivity.this).saveBoolean("FirstRun",true);
            if(PreferenceManager.getInstance(HelperActivity.this).fetchBoolean("Remember")==true){
                Intent intent = new Intent(HelperActivity.this,LocationActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(HelperActivity.this,LoginActivity.class);
                startActivity(intent);
            }

        }
    }
    }
