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

public class AddItemsDisplayAdapter extends RecyclerView.Adapter<AddItemsViewHolder> {
    ArrayList<String> items;
    Activity activity;

    public AddItemsDisplayAdapter(ArrayList<String> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }
    @NonNull
    @Override
    public AddItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items,parent,false);
        return new AddItemsViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemsViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
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

class AddItemsViewHolder extends RecyclerView.ViewHolder{
    TextView textView;
    ImageView imageView;
    private AddItemsDisplayAdapter addItemsDisplayAdapter;
    public AddItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.item_name);
        imageView = itemView.findViewById(R.id.delete_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemsDisplayAdapter.items.remove(getAdapterPosition());
                PreferenceManager.getInstance(addItemsDisplayAdapter.activity).saveArrayList(addItemsDisplayAdapter.items,"SelectedItems");
                addItemsDisplayAdapter.notifyItemChanged(getAdapterPosition());
            }
        });
    }

    public AddItemsViewHolder linkAdapter(AddItemsDisplayAdapter adapter){
        this.addItemsDisplayAdapter = adapter;
        return this;
    }
}

