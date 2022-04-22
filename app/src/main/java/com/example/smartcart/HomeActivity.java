package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class HomeActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener {

    TextView address,welcome,username;
    EditText searchBar;
    ImageButton cart,filter;
    RecyclerView recyclerView, recyclerView2;
    String appId = "smartcartdb-unnio";
    private App app;
    Double lat;
    Double lng;
    static ArrayList<Store> nstores = new ArrayList<Store>();
    
    ArrayList<Product> productsList = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i = getIntent();
        address = findViewById(R.id.address);
        welcome = findViewById(R.id.welome_text);
        address.setText(String.valueOf(PreferenceManager.getInstance(HomeActivity.this).fetchString("Location")));
        lat = Double.valueOf(PreferenceManager.getInstance(this).fetchString("Latitude"));
        lng = Double.valueOf(PreferenceManager.getInstance(this).fetchString("Longitude"));
        app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();
        Log.d("INFO",user.getCustomData().toString());
        welcome.setText("Welcome "+ user.getCustomData().getString("firstName"));
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.HomeActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.CartActivity:
//                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.HomeActivity:
                        return true;
                    case R.id.ProfileActivity:
//                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        filter = findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INFO","Inside filter");
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.show(getSupportFragmentManager(),"filter dialog");

            }
        });
        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(PreferenceManager.getInstance(HomeActivity.this).fetchString("Radius")!= null){
                    PreferenceManager.getInstance(HomeActivity.this).saveStoresList("NearbyStores",nstores);
                    ArrayList<Store> savedStores = PreferenceManager.getInstance(HomeActivity.this).getStoresList("NearbyStores");
                    Intent i = new Intent(HomeActivity.this, SearchActivity.class);
                    i.putExtra("Stores", nstores);
                    startActivity(i);
                }
                else{
                    FilterDialog filterDialog = new FilterDialog();
                    filterDialog.show(getSupportFragmentManager(),"filter dialog");
                }

                return false;
            }
        });

        cart  = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(HomeActivity.this, CartActivity.class);
//                startActivity(i);
            }
        });


    }

    @Override
    public void applyTexts(ArrayList<Store> stores) {
        nstores = stores;
        Log.d("Stores info",String.valueOf(nstores.size()));

    }
}