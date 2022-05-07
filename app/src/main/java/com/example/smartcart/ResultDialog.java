package com.example.smartcart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.util.PreferenceManager;

import java.util.ArrayList;

public class ResultDialog extends AppCompatDialogFragment {

    TextView product_text, product_number, distance_text, distance;
    static View view;

    DisplayData data;
    ArrayList<String> products = new ArrayList<String>();
    ArrayList<ProductPrice> productPrices = new ArrayList<ProductPrice>();
    public ResultDialog(DisplayData data){
        this.data = data;
    }

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.store_results_dialog, null);
        product_text = view.findViewById(R.id.no_of_products_text);
        product_number = view.findViewById(R.id.products_available_value);
        distance_text = view.findViewById(R.id.distance_text);
        distance = view.findViewById(R.id.distance_value);

        product_number.setText(String.valueOf(data.getProductsList().size()));
        distance.setText(String.valueOf(data.getStore().getDistance()/1000) +" Km");

        for(ProductStore p : data.getProductsList()){
            products.add(p.getProduct());
            ProductPrice pp = new ProductPrice(p.getProduct(),String.valueOf(p.getPrice()));
            productPrices.add(pp);
        }

        RecyclerView recyclerView = view.findViewById(R.id.store_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        StoreItemsDisplayAdapter storeItemsDisplayAdapter = new StoreItemsDisplayAdapter(productPrices, getActivity());
        recyclerView.setAdapter(storeItemsDisplayAdapter);
        builder.setView(view)
                .setPositiveButton("Save Store", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceManager.getInstance(getActivity()).saveDisplayData("StoreInfo", data);
                    }
                });
        return builder.create();
    }
}

