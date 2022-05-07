package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.smartcart.util.PreferenceManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mIsRestore;
    BottomNavigationView bottomNavigationView;

    protected int getLayoutId() {
        return R.layout.activity_maps2;
    }
    ArrayList<DisplayData> data = new ArrayList<DisplayData>();
    Map<Marker, DisplayData> markerMap = new HashMap<Marker, DisplayData>();

    MaterialButton back, cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsRestore = savedInstanceState != null;
        setContentView(getLayoutId());
        setUpMap();
        data = (ArrayList<DisplayData>) getIntent().getExtras().getSerializable("FinalData");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SearchActivity:
                        if(PreferenceManager.getInstance(MapsActivity2.this).getStoresList("NearbyStores")!=null){
                            startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                        else{
                            FilterDialog filterDialog = new FilterDialog();
                            filterDialog.show(getSupportFragmentManager(),"filter dialog");
                        }
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

                }
                return false;
            }
        });
    }
    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;
        startDemo(mIsRestore);


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.d("Markerdata",String.valueOf(markerMap.get(marker).getStore().getName()));
                DisplayData m = markerMap.get(marker);
                ResultDialog resultDialog = new ResultDialog(m);
                resultDialog.show(getSupportFragmentManager(),"Result dialog");

                return false;
            }
        });
    }
    protected void startDemo(boolean isRestore) {
        if (!isRestore) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.9849,-81.2453 ), 10));
        }
        for(DisplayData d : data){
            Log.d("Price", String.valueOf(d.getPrice()));
            IconGenerator iconFactory = new IconGenerator(this);
            iconFactory.setColor(Color.RED);
            MarkerOptions markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("$"+ String.valueOf(d.getPrice())))).
                    position(new LatLng(d.getStore().getLat(), d.getStore().getLng())).
                    anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

            Marker marker = mMap.addMarker(markerOptions);
            markerMap.put(marker,d);
            mMap.addMarker(markerOptions);
        }
    }
    protected GoogleMap getMap() {
        return mMap;
    }
}