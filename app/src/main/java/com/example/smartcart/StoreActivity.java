package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {
    TextView store_name, location, price, no_store_selected;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    CardView cardView;
    MaterialButton notifyButton;
    ArrayList<ProductPrice> productPrices = new ArrayList<ProductPrice>();
    ArrayList<String> unavailableProducts =  new ArrayList<String>();
    String zipCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.StoreActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:
                        if(PreferenceManager.getInstance(StoreActivity.this).getStoresList("NearbyStores")==null){
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
                        startActivity((new Intent(getApplicationContext(),HomeActivity.class)));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ProfileActivity:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.StoreActivity:
                        return true;

                }
                return false;
            }
        });
        store_name = findViewById(R.id.store_name);
        cardView = findViewById(R.id.store_card);
        location = findViewById(R.id.location_value);
        price = findViewById(R.id.price_value);
        recyclerView = findViewById(R.id.cart_items);
        no_store_selected = findViewById(R.id.no_store_selected);
        notifyButton = findViewById(R.id.notify_button);
        if(PreferenceManager.getInstance(StoreActivity.this).getArrayList("UnavailableItems")!= null){
            unavailableProducts =  PreferenceManager.getInstance(StoreActivity.this).getArrayList("UnavailableItems");
        }
        if(PreferenceManager.getInstance(StoreActivity.this).getDisplayData("StoreInfo")!=null){
            DisplayData data = PreferenceManager.getInstance(StoreActivity.this).getDisplayData("StoreInfo");
            no_store_selected.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            store_name.setText(String.valueOf(data.getStore().getName()));
            String location_value = data.getStore().getAddress();
            location_value = location_value.replace("[","");
            location_value = location_value.replace("]","");
            location_value = location_value.split(",")[0];
            location_value = location_value.replace("\"" , "");
            Log.d("VALUE",location_value);
            location.setText(location_value);
            zipCode = data.getStore().getZipCode();
            price.setText("$"+String.valueOf(data.getPrice())+" CAD");
            recyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));
            for(ProductStore p : data.getProductsList()){
                ProductPrice pp = new ProductPrice(p.getProduct(),String.valueOf(p.getPrice()));
                productPrices.add(pp);
            }
            StoreItemsDisplayAdapter storeItemsDisplayAdapter = new StoreItemsDisplayAdapter(productPrices, StoreActivity.this);
            recyclerView.setAdapter(storeItemsDisplayAdapter);
        }
        else{
            cardView.setVisibility(View.GONE);
            store_name.setText("Store Name");

        }

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unavailableProducts.size()>0){
                    for (String item: unavailableProducts){
                        String topicName = item+"_"+zipCode;
                        topicName = topicName.replace(" ","_");
                        Log.d("Topic Name", topicName);
                        FirebaseMessaging.getInstance().subscribeToTopic(topicName)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Subscribed";
                                        if (!task.isSuccessful()) {
                                            msg = "Subscribe failed";
                                        }
                                        Log.d("Message", msg);
                                        Toast.makeText(StoreActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                }

            }
        });
        }
}