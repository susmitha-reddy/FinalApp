package com.example.smartcart;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.util.PreferenceManager;

import java.util.ArrayList;

public class StoreItemsDisplayAdapter extends  RecyclerView.Adapter<StoreItemsViewHolder>{
    ArrayList<ProductPrice> items;
    Activity activity;

    public StoreItemsDisplayAdapter(ArrayList<ProductPrice> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }
    @NonNull
    @Override
    public StoreItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_list,parent,false);
        return new StoreItemsViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemsViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getProduct());
        holder.price.setText("$"+items.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        if(items!=null){
            return items.size();
        }
        else{
            return 0;
        }

    }
}

class StoreItemsViewHolder extends RecyclerView.ViewHolder{
    TextView textView,price;
    private StoreItemsDisplayAdapter storeItemsDisplayAdapter;
    public StoreItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.item_name);
        price = itemView.findViewById(R.id.item_price);
    }

    public StoreItemsViewHolder linkAdapter(StoreItemsDisplayAdapter adapter){
        this.storeItemsDisplayAdapter = adapter;
        return this;
    }
}

