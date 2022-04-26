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

public class CartItemsDisplayAdapter  extends  RecyclerView.Adapter<CartItemsViewHolder>{
    ArrayList<String> items;
    Activity activity;

    public CartItemsDisplayAdapter(ArrayList<String> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }
    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items,parent,false);
        return new CartItemsViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
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

class CartItemsViewHolder extends RecyclerView.ViewHolder{
    TextView textView;
    ImageView imageView;
    private CartItemsDisplayAdapter cartItemsDisplayAdapter;
    public CartItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.item_name);
        imageView = itemView.findViewById(R.id.delete_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItemsDisplayAdapter.items.remove(getAdapterPosition());
                PreferenceManager.getInstance(cartItemsDisplayAdapter.activity).saveArrayList(cartItemsDisplayAdapter.items,"CartItems");
                cartItemsDisplayAdapter.notifyItemChanged(getAdapterPosition());
            }
        });
    }

    public CartItemsViewHolder linkAdapter(CartItemsDisplayAdapter adapter){
        this.cartItemsDisplayAdapter = adapter;
        return this;
    }
}
