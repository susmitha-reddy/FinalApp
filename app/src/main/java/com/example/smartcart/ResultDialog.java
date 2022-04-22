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

import java.util.ArrayList;

public class ResultDialog extends AppCompatDialogFragment {

    TextView product_text, product_number, distance_text, distance;
    static View view;

    DisplayData data;


    public ResultDialog(DisplayData data){
        this.data = data;
    }

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.store_results_dialog, null);
        product_text = view.findViewById(R.id.product_number_text);
        product_number = view.findViewById(R.id.product_number);
        distance_text = view.findViewById(R.id.distance_text);
        distance = view.findViewById(R.id.distance);

        product_number.setText(String.valueOf(data.getProductsList().size()));
        distance.setText(String.valueOf(data.getStore().getDistance()));

        builder.setView(view)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
}

