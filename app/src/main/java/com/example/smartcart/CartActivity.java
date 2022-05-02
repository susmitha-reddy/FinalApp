package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> products =  new ArrayList<String>();
    TextView enpty_text;
    MaterialButton go_to_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.CartActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:
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

    }
}