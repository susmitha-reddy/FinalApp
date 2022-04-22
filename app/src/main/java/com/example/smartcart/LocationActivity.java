package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.util.PreferenceManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    Button find_location,button_continue;
    TextView address,text_or;
    EditText manualZipcode;
    LocationManager locationManager;
    private Location location;
    private Double lat;
    private Double lng;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLng() {
        return lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLat() {
        return lat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toast.makeText(this, "User Logged in successfully", Toast.LENGTH_SHORT).show();

        find_location = findViewById(R.id.find_location);
        button_continue = findViewById(R.id.continue_button);
        address = findViewById(R.id.address_text);
        manualZipcode = findViewById(R.id.manual_zipcode);
        text_or = findViewById(R.id.or);

        address.setVisibility(View.GONE);
        button_continue.setVisibility(View.GONE);

        find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_location.setEnabled(false);
                manualZipcode.setEnabled(false);
                try {
                    getCurrentLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        manualZipcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    try {
                        find_location.setEnabled(false);
                        manualZipcode.setEnabled(false);
                        getlocationmanual(String.valueOf(manualZipcode.getText()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

        });
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getInstance(LocationActivity.this).saveString("Location",String.valueOf(address.getText()));
                PreferenceManager.getInstance(LocationActivity.this).saveString("Latitude", String.valueOf(getLat()));
                PreferenceManager.getInstance(LocationActivity.this).saveString("Longitude", String.valueOf(getLng()));
                Intent i = new Intent(LocationActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

    }

    @SuppressLint("MissingPermission")
    public void  getCurrentLocation() throws IOException {


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            Log.d("INFO","Started");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
        }

        if(network){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*60*1,0, this);

        }

        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc == null){
            Log.d("Check","Lost again");
        }

        if(loc ==null){
            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if(loc == null){
            Log.d("Check","Lost again");
        }


    }


    public String address(Double lat, Double lng) throws IOException {
        String home;
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(lat,lng,1);
        home = addresses.get(0).getAddressLine(0).split(",")[0 ];
        Log.v("Address Line", home);

        return home;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        setLocation(location);
        setLat(location.getLatitude());
        setLng(location.getLongitude());
        findViewById(R.id.find_location).setVisibility(View.GONE);
        findViewById(R.id.manual_zipcode).setVisibility(View.GONE);
        findViewById(R.id.or).setVisibility(View.GONE);
        address = findViewById(R.id.address_text);
        address.setVisibility(View.VISIBLE);
        try {
            address.setText(address(location.getLatitude(),location.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button_continue = findViewById(R.id.continue_button);
        button_continue.setVisibility(View.VISIBLE);
        locationManager.removeUpdates(this);
    }

    public void getlocationmanual( String zipcode) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocationName(zipcode,1);
            setLng(addresses.get(0).getLongitude());
            setLat(addresses.get(0).getLatitude());
            String place  = address(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
            findViewById(R.id.find_location).setVisibility(View.GONE);
            findViewById(R.id.manual_zipcode).setVisibility(View.GONE);
            findViewById(R.id.or).setVisibility(View.GONE);
            address = findViewById(R.id.address_text);
            address.setVisibility(View.VISIBLE);
            address.setText(place);
            button_continue = findViewById(R.id.continue_button);
            button_continue.setVisibility(View.VISIBLE);
            Log.d("Place", place);
        }
        catch (Exception e){
            Toast.makeText(this, "Please Enter a Valid Postal Code", Toast.LENGTH_LONG).show();
            find_location.setEnabled(true);
            manualZipcode.setEnabled(true);

        }


    }
}