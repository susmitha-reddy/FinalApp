package com.example.smartcart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.smartcart.util.PreferenceManager;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class FilterDialog extends AppCompatDialogFragment implements  LocationService.AsyncResponse{
    TextView  storestext, selectedvalue;
    SeekBar seekbar;
    Button find_stores,apply;
    AutoCompleteTextView dropdown;
    static View view;
    static ArrayList<Store> stores = new ArrayList<Store>();
    private FilterDialogListener listener;
    String appId = "smartcartdb-unnio";
    String selectedStore;
    private App app;

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.filter_dailog_layout,null);

        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(stores!=null){
                            listener.applyTexts(stores);
                            Log.d("INFO","Stored the store details");
                        }else{
                            Log.d("From Apply click","Find something else");
                        }
                    }
                });
        //apply = builder.show().getButton(AlertDialog.BUTTON_POSITIVE);
        //apply.setEnabled(false);
        storestext = view.findViewById(R.id.stores_found);
        selectedvalue = view.findViewById(R.id.selectedValue);
        seekbar = view.findViewById(R.id.seekBar);
        find_stores = view.findViewById(R.id.find_stores);
        dropdown = view.findViewById(R.id.store_dropdown);
        ArrayList<String> retailStores = new ArrayList<String>();
        retailStores.add("All");
        retailStores.add("Metro");
        retailStores.add("Real Canadian Store");
        retailStores.add("Sobeys");
        retailStores.add("Loblaws");
        retailStores.add("Walmart");
        retailStores.add("Food Basics");
        retailStores.add("Freshco");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.store_list, retailStores);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStore = adapterView.getItemAtPosition(i).toString();

            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                selectedvalue.setText(String.valueOf(seekBar.getProgress()+5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        find_stores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = (PreferenceManager.getInstance(getActivity()).fetchString("Latitude"));
                String lng = (PreferenceManager.getInstance(getActivity()).fetchString("Longitude"));
                LocationService locationService = new LocationService();
                locationService.delegate = FilterDialog.this;
                PreferenceManager.getInstance(getActivity()).saveString("Radius",String.valueOf(selectedvalue.getText()));
                if(selectedStore!= null){
                    locationService.execute(new String[]{String.valueOf(selectedvalue.getText()), lat, lng,selectedStore});
                }
                else{
                    locationService.execute(new String[]{String.valueOf(selectedvalue.getText()), lat, lng});
                }
                find_stores.setEnabled(false);
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (FilterDialogListener)context;
    }

    @Override
    public void processFinish(ArrayList<Store> output) {
        Log.d("Progress","Done with location service, next to Realm Service");
        Log.d("URL Info",String.valueOf(output.size()));
        if(output.size()!=0){
            getinHouseStores(output);
        }
        else{
            find_stores = view.findViewById(R.id.find_stores);
            storestext = view.findViewById(R.id.stores_found);
            find_stores.setEnabled(true);
            storestext.setText("Found 0 nearby "+ selectedStore +" stores.");

        }
    }


    public interface FilterDialogListener{
        void applyTexts(ArrayList<Store> stores);
    }

    public void getinHouseStores(ArrayList<Store> output){
        ArrayList<Store> in = output;
        app = new App(new AppConfiguration.Builder(appId).build());

        User user = app.currentUser();
        if(user==null){
            Log.d("Filter","user works");
        }
        ArrayList<Store> inHousestores = new ArrayList<Store>();
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SmartCartDb");
        MongoCollection<Document> storeCollection = mongoDatabase.getCollection("StoreLocation");
        Document filter = getStoreQuery(in);
        RealmResultTask<MongoCursor<Document>> findTask = storeCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                Log.v("Method", "Started");
                MongoCursor<Document> results = task.get();
                Log.d("Results",String.valueOf(results.hasNext()));
                while (results.hasNext()) {
                    Document current = results.next();
                    for(Store s : in){
                        if(current.getString("ZipCodes").equals(s.getZipCode())){
                            inHousestores.add(s);
                        }
                    }
                }
                Log.d("Data",String.valueOf(inHousestores.size()));
                stores = inHousestores;
                find_stores = view.findViewById(R.id.find_stores);
                storestext = view.findViewById(R.id.stores_found);
                find_stores.setEnabled(true);
                storestext.setText("Found " + String.valueOf(stores.size())+" nearby stores.");
               // apply.setEnabled(true);
            }
        });


    }


    private Document getStoreQuery(ArrayList<Store> stores) {
        Document filter = new Document();
        List<Document> zipcodePipeline = new ArrayList<Document>();
        for (Store z : stores){
            zipcodePipeline.add(new Document("ZipCodes",z.getZipCode()));
        }
        filter = new Document("$or" , zipcodePipeline);
        return filter;
    }
}
