package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName, lastName, phoneNumber, email, postalCode;
    Button button;
    String appId = "smartcartdb-unnio";
    private App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        postalCode = findViewById(R.id.postalCode);
        button = findViewById(R.id.register);
        app = new App(new AppConfiguration.Builder(appId).build());
        User user = app.currentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
                MongoDatabase mongoDatabase = mongoClient.getDatabase("SmartCartDb");
                MongoCollection mongoCollection = mongoDatabase.getCollection("UserCollection");
                Document document = new Document("Id",user.getId().toString()).append("firstName",String.valueOf(firstName.getText()))
                        .append("lastName",String.valueOf(lastName.getText()))
                        .append("phoneNumber",String.valueOf(phoneNumber.getText()))
                        .append("email",String.valueOf(email.getText()))
                        .append("postalCode",String.valueOf(postalCode.getText()));
                mongoCollection.insertOne(document).getAsync(result1 -> {
                    if (result1.isSuccess()) {
                        Log.v("Data", "Data Inserted Successfully");

                    } else {
                        Log.v("Data", "Error:" + result1.getError().toString());
                    }
                });
                Intent i = new Intent(RegisterActivity.this,LocationActivity.class);
                startActivity(i);
            }
        });
    }
}