package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class TestActivity extends AppCompatActivity {
    ProgressBar progressBar;
    AutoCompleteTextView autoCompleteTextView;


    ArrayList<String> inventoryProducts = new ArrayList<String>();
    String appId = "smartcartdb-unnio";
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.d("INFO","IN TEST ACTIVITY");
        app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();
        progressBar = findViewById(R.id.progressBar);
        autoCompleteTextView  = findViewById(R.id.autoComplete);
        autoCompleteTextView.setVisibility(View.GONE);

        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SmartCartDb");
        MongoCollection productCollection = mongoDatabase.getCollection("ProductCollection");

        RealmResultTask<MongoCursor<Document>> findTask = productCollection.find().iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                Log.v("Product", "Started");
                MongoCursor<Document> results = task.get();
                while (results.hasNext()) {
                    Log.d("PRODUCT",results.next().getString("Name"));
                    String product =   results.next().getString("Name");
                    inventoryProducts.add(product);
                }
                Log.d("SIZE", String.valueOf(inventoryProducts.size()));
                progressBar.setVisibility(View.GONE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                Log.d("size",String.valueOf(inventoryProducts.size()));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,inventoryProducts);
                if(arrayAdapter != null){
                    Log.d("Some Hope", "Hope");
                }
                autoCompleteTextView.setAdapter(arrayAdapter);
            }
        });





            }
}