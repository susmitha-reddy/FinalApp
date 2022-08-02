package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.bson.Document;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> products =  new ArrayList<String>();
    TextView enpty_text;
    MaterialButton go_to_search;
    MaterialButton save_button;
    String appId = "smartcartdb-unnio";
    private App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.CartActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:
                        if(PreferenceManager.getInstance(CartActivity.this).getStoresList("NearbyStores")==null){
                            FilterDialog filterDialog = new FilterDialog();
                            filterDialog.show(getSupportFragmentManager(),"filter dialog");
                        }
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.HomeActivity:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.CartActivity:
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
        enpty_text = findViewById(R.id.empty_cart_text);
        go_to_search=findViewById(R.id.go_to_search);
        if(PreferenceManager.getInstance(CartActivity.this).getArrayList("CartItems") != null){
            products = PreferenceManager.getInstance(CartActivity.this).getArrayList("CartItems");
            RecyclerView recyclerView = findViewById(R.id.cart_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            CartItemsDisplayAdapter cartItemsDisplayAdapter = new CartItemsDisplayAdapter(products, this);
            recyclerView.setAdapter(cartItemsDisplayAdapter);
            enpty_text.setVisibility(View.GONE);
            go_to_search.setVisibility(View.GONE);
        }
        else{
            enpty_text.setVisibility(View.VISIBLE);
            go_to_search.setVisibility(View.VISIBLE);
        }
        save_button = findViewById(R.id.save_cart);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_button.setEnabled(false);
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);
                MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
                MongoDatabase mongoDatabase = mongoClient.getDatabase("SmartCartDb");
                MongoCollection savedCollection = mongoDatabase.getCollection("savedLists");

                savedCollection.insertOne(new Document("userid", user.getId()).append("products",products).
                        append("date",thisDate)).getAsync(result -> {
                            if(result.isSuccess()){
                                save_button.setEnabled(true);
                                Toast.makeText(CartActivity.this, "Cart Items are saved.", Toast.LENGTH_SHORT).show();
                            }
                });



            }
        });

    }
}