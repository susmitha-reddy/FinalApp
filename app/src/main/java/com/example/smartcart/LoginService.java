package com.example.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.smartcart.util.PreferenceManager;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginService {
    String appId = "smartcartdb-unnio";
    private App app;

    public void loginVerification(String username, String password, Activity activity, Boolean newUser){
        app = new App(new AppConfiguration.Builder(appId).build());
        Credentials credentials = Credentials.emailPassword(username,password);
        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("User Info", "User logged in sucessfully");
                    if(newUser){
                        Intent i = new Intent(activity,RegisterActivity.class);
                        activity.startActivity(i);
                    }else{
                        Intent i = new Intent(activity,LocationActivity.class);
                        activity.startActivity(i);
                    }

                }
                else{
                    Toast.makeText(activity, "Please Enter a valid Username and Password", Toast.LENGTH_LONG).show();
                    Log.v("User Info","Login with email failed");
                    activity.findViewById(R.id.login_button).setEnabled(true);
                }
            }
        });
    }

    public void registerUser(String email, String password, Activity activity){
        app = new App(new AppConfiguration.Builder(appId).build());
        app.getEmailPassword().registerUserAsync(email,password,it->{
            if(it.isSuccess()){
                Log.d("INFO","User created successfully!!!!");
                Toast.makeText(activity, "User created successfully.", Toast.LENGTH_SHORT).show();
                loginVerification(email,password,activity, true);
            }
            else{
                Toast.makeText(activity, "Unable to Sign up. Please try again", Toast.LENGTH_SHORT).show();
                activity.findViewById(R.id.sign_up_button).setEnabled(true);
            }
        });
    }

}
