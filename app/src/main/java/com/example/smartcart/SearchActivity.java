package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SearchActivity extends AppCompatActivity {

    ProgressBar progressBar;
    AutoCompleteTextView autoCompleteTextView;
    MaterialButton search_button;
    ConstraintLayout constraintLayout1, add_layout;
    TextView add_text;
    MaterialButton add_button, cart_button;
    MaterialButton done_button;

    int counter = 0;
    ArrayList<String> inventoryProducts = new ArrayList<String>();
    ArrayList<String> addedItems = new ArrayList<String>();

    ArrayList<Store> prefStores = new ArrayList<Store>();

    String appId = "smartcartdb-unnio";
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.SearchActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:

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
        prefStores = PreferenceManager.getInstance(this).getStoresList("NearbyStores");
        Log.d("PrefStores Value",String.valueOf(prefStores.size()));
        progressBar = findViewById(R.id.progressBar);
        autoCompleteTextView = findViewById(R.id.autoComplete);
        search_button = findViewById(R.id.search_button);
        constraintLayout1 = findViewById(R.id.search_layout);
        add_layout = findViewById(R.id.add_layout);
        add_text = findViewById(R.id.add_text);
        add_button = findViewById(R.id.add_button);
        done_button = findViewById(R.id.done_button);
        cart_button = findViewById(R.id.cart_button);
        constraintLayout1.setVisibility(View.GONE);

        if(PreferenceManager.getInstance(this).getArrayList("SelectedItems") != null){
            addedItems = PreferenceManager.getInstance(this).getArrayList("SelectedItems");
            Log.d("Initial items",String.valueOf(addedItems.size()));
        }

        RecyclerView recyclerView = findViewById(R.id.cart_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddItemsDisplayAdapter addItemsDisplayAdapter = new AddItemsDisplayAdapter(addedItems, this);
        recyclerView.setAdapter(addItemsDisplayAdapter);

        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SmartCartDb");
        MongoCollection productCollection = mongoDatabase.getCollection("ProductCategory");

        RealmResultTask<MongoCursor<Document>> findTask = productCollection.find().iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                Log.v("Product", "Started");
                MongoCursor<Document> results = task.get();
                while (results.hasNext()) {
                    String product = results.next().getString("product_name");
                    inventoryProducts.add(product);
                }
                Log.d("SIZE", String.valueOf(inventoryProducts.size()));
                Log.d("New Products", inventoryProducts.get(0));
                progressBar.setVisibility(View.GONE);
                constraintLayout1.setVisibility(View.VISIBLE);
                add_layout.setVisibility(View.GONE);
                if(addedItems == null){
                    done_button.setVisibility(View.GONE);
                }
                Log.d("size", String.valueOf(inventoryProducts.size()));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inventoryProducts);
                if (arrayAdapter != null) {
                    Log.d("Some Hope", "Hope");
                }
                autoCompleteTextView.setAdapter(arrayAdapter);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_layout.setVisibility(View.VISIBLE);
                add_text.setText(autoCompleteTextView.getText());
                autoCompleteTextView.setEnabled(false);
                search_button.setEnabled(false);

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addedItems.add(String.valueOf(add_text.getText()));
                counter++;
                addItemsDisplayAdapter.notifyItemInserted(addedItems.size()-1);
                autoCompleteTextView.setText("");
                add_layout.setVisibility(View.GONE);
                done_button.setVisibility(View.VISIBLE);
                PreferenceManager.getInstance(SearchActivity.this).saveArrayList(addedItems,"SelectedItems");
                autoCompleteTextView.setEnabled(true);
                autoCompleteTextView.setText("");
                search_button.setEnabled(true);

            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INFO","Omclick worked");
                PreferenceManager.getInstance(SearchActivity.this).saveArrayList(addedItems,"ShoppingCart");
                PreferenceManager.getInstance(SearchActivity.this).saveArrayList(addedItems,"CartItems");
                if(prefStores == null){
                    Toast.makeText(SearchActivity.this,"Please set the search radius before proceeding",Toast.LENGTH_LONG);
                }else{
                    getFinalProductStores(addedItems, prefStores);
                }
            }
        });

        cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getInstance(SearchActivity.this).saveArrayList(addedItems,"CartItems");
                Intent i = new Intent(SearchActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
    }

    public void getFinalProductStores(ArrayList<String> listItems, ArrayList<Store> Stores){
        ArrayList<ProductStore> listf = new ArrayList<ProductStore>();
        ArrayList<Store> finalStores = new ArrayList<Store>();
        if(Stores.size() > 1){
            Log.d("Stores","There are stores details");
        }
        if(listItems.size() < 1){
            Log.d("Stores","There are product details");
        }
        User user = app.currentUser();
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SmartCartDb");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("ProductandStoreCollection");
        Document filter = getQuery(listItems,Stores);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        Log.d("INFO","Back to getfinalproducts");
        findTask.getAsync(task ->{
            if(task.isSuccess()){
                
                Log.v("Method","Started");
                MongoCursor<Document> results = task.get();
                Log.v("Results","got results");
                while(results.hasNext()){
                    Document current = results.next();
                    ProductStore ps = new ProductStore(current.getString("Name"),Double.valueOf(current.getString("Price")),
                            current.getString("Store"),current.getString("Zipcode"));
                    listf.add(ps);
                }
                Log.d("Products","Price details found");
                displayData(listf,Stores);
            }
            else{
                Log.d("MongoDb ERROR",task.getError().toString());
            }
        });
    }

    private void displayData(ArrayList<ProductStore> listf, ArrayList<Store> stores) {
        ArrayList<DisplayData> finalData = new ArrayList<DisplayData>();
        HashMap<String,ArrayList<ProductStore>> displayData = new HashMap<String, ArrayList<ProductStore>>();
        for(ProductStore p : listf){
            String key = p.getZipcode();
            if(displayData.containsKey(key)){
                displayData.get(key).add(p);
            }else{
                ArrayList<ProductStore> pstores = new ArrayList<ProductStore>();
                pstores.add(p);
                displayData.put(key,pstores);
            }
        }
        Log.d("ProductsHash",String.valueOf(displayData.size()));
        Iterator<HashMap.Entry<String, ArrayList<ProductStore>>> iterator = displayData.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, ArrayList<ProductStore>> entry = iterator.next();
            ArrayList<ProductStore> products = entry.getValue();
            Double total = 0.0;
            Store store = null;
            for(ProductStore k: products){
                ArrayList<String> p = new ArrayList<>();
                total = total+k.getPrice();
                for (Store s : stores){
                    if(entry.getKey().equals(s.getZipCode())){
                        Log.d("Zipcode",s.getZipCode());
                        store = new Store(s.getName(),s.getZipCode(),s.getDistance(),s.getAddress(),s.getLat(),s.getLng());
                    }
                }
            }
            DisplayData d = new DisplayData(total,store,products);
            finalData.add(d);
        }
        Log.d("Final Data", String.valueOf(finalData.size()));
        for (DisplayData d : finalData){
            Log.d("Details",d.getStore().getName() + " " + String.valueOf(d.getPrice()) + String.valueOf(d.getProductsList().size()));
        }
        if(finalData.size() > 0){
            Intent i = new Intent(SearchActivity.this,MapsActivity2.class);
            i.putExtra("FinalData",finalData);
            startActivity(i);
        } else {
            Toast.makeText(SearchActivity.this,"Selected Products are not available in the store", Toast.LENGTH_LONG).show();
        }

    }

    public Document getQuery(@NonNull ArrayList<String> products, ArrayList<Store> stores){

        Document filter = new Document();
        List<Document> pipeline = new ArrayList<Document>(); //ArrayList<Document>();
        List<Document> productPipeline = new ArrayList<Document>();
        List<Document> zipcodePipeline = new ArrayList<Document>();
        for (String s : products) {
            productPipeline.add(new Document("Name", s));
        }

        Document productDoc = new Document("$or",productPipeline);
        Log.d("Stores", String.valueOf(stores.size()));
        for (Store z : stores){
            zipcodePipeline.add(new Document("Zipcode",z.getZipCode()));
        }
        Document zipcodeDoc = new Document("$or" , zipcodePipeline);
        pipeline.add(productDoc);
        pipeline.add(zipcodeDoc);
        filter.put("$and",pipeline);
        return filter;
    }


}