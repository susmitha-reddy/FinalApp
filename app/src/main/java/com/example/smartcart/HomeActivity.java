package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class HomeActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener, CategoryAdapter.OnCategoryListener {

    TextView address,welcome,username;
    EditText searchBar;
    ImageButton cart,filter;
    RecyclerView category_view;
    String appId = "smartcartdb-unnio";
    private App app;
    Double lat;
    Double lng;
    CategoryAdapter categoryAdapter;
    static ArrayList<Store> nstores = new ArrayList<Store>();
    List<String> categories = Arrays.asList("Babies", "Bakery","Breakfast","Dairy","Frozen","Pantry", "Personal Care","Pets","Produce","Snacks");
    ArrayList<Integer> images = new ArrayList<Integer>();

    ArrayList<Product> productsList = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        images.add(R.drawable.baby_products);
        images.add(R.drawable.bread);
        images.add(R.drawable.breakfast);
        images.add(R.drawable.dairy_products);
        images.add(R.drawable.frozen_food);
        images.add(R.drawable.icons8_pantry_64);
        images.add(R.drawable.personal_care);
        images.add(R.drawable.icons8_pets_64);
        images.add(R.drawable.icons8_fruits_and_vegetables_64);
        images.add(R.drawable.icons8_snacks);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        Intent i = getIntent();
        address = findViewById(R.id.address);
        welcome = findViewById(R.id.welome_text);
        category_view = findViewById(R.id.categories_view);
        category_view.setLayoutManager(gridLayoutManager);
        category_view.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(this, categories, images, this);
        category_view.setAdapter(categoryAdapter);
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
                        if(PreferenceManager.getInstance(HomeActivity.this).getStoresList("NearbyStores")==null){
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
                        return true;
                    case R.id.ProfileActivity:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.StoreActivity:
                        startActivity(new Intent(getApplicationContext(), StoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
        if(PreferenceManager.getInstance(HomeActivity.this).fetchString("Radius")==null){
            PreferenceManager.getInstance(HomeActivity.this).saveString("Radius","5");
        }
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
        PreferenceManager.getInstance(HomeActivity.this).saveStoresList("NearbyStores",nstores);

    }

    @Override
    public void onCategoryClick(int position){
        Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
        i.putExtra("CategoryName", categories.get(position));
        startActivity(i);
    }
}