package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class ProfileActivity extends AppCompatActivity {
    TextView name_text, name_value, email_text, email_value, phone_text, phone_value, location_text, location_value, radius_text, radius_value;
    MaterialButton logout, edit;
    String appId = "smartcartdb-unnio";
    private App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();
        Log.d("INFO", String.valueOf(user.getCustomData()));
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.ProfileActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:
                        if(PreferenceManager.getInstance(ProfileActivity.this).getStoresList("NearbyStores")==null){
                            FilterDialog filterDialog = new FilterDialog();
                            filterDialog.show(getSupportFragmentManager(),"filter dialog");
                        }
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.CartActivity:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.HomeActivity:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ProfileActivity:
                        return true;
                    case R.id.StoreActivity:
                        startActivity(new Intent(getApplicationContext(), StoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        name_text = findViewById(R.id.name_text);
        name_value = findViewById(R.id.name_value);
        email_text = findViewById(R.id.email_text);
        email_value = findViewById(R.id.email_value);
        phone_text = findViewById(R.id.phone_text);
        phone_value = findViewById(R.id.phone_value);
        location_text = findViewById(R.id.location_text);
        location_value = findViewById(R.id.location_value);
        radius_text = findViewById(R.id.radius_text);
        radius_value = findViewById(R.id.radius_value);
        logout = findViewById(R.id.logout);
        edit = findViewById(R.id.edit_button);

        name_value.setText(user.getCustomData().getString("firstName") + " "+user.getCustomData().getString("lastName"));
        email_value.setText(String.valueOf(user.getCustomData().getString("email")));
        phone_value.setText(String.valueOf(user.getCustomData().getString("phoneNumber")));
        location_value.setText(PreferenceManager.getInstance(ProfileActivity.this).fetchString("Location"));
        radius_value.setText(String.valueOf(PreferenceManager.getInstance(ProfileActivity.this).fetchString("Radius")));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getInstance(ProfileActivity.this).deleteAllPreferences();
                PreferenceManager.getInstance(ProfileActivity.this).deletepreference("FirstRun");
                Log.d("INFO",String.valueOf(PreferenceManager.getInstance(ProfileActivity.this).fetchBoolean("FirstRun")));
                if(PreferenceManager.getInstance(ProfileActivity.this).fetchBoolean("FirstRun")){
                    PreferenceManager.getInstance(ProfileActivity.this).deletepreference("FirstRun");
                    Log.d("INFO",String.valueOf(PreferenceManager.getInstance(ProfileActivity.this).fetchBoolean("FirstRun")));
                    Log.d("User Logout","User is successfully logged out");
                }
                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });




    }
}