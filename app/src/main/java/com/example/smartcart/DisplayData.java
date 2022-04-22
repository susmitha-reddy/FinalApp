package com.example.smartcart;

import java.io.Serializable;
import java.util.ArrayList;

public class DisplayData implements Serializable {
    private Double Price;
    private  Store store;
    private ArrayList<ProductStore> productsList;

    public DisplayData(Double price, Store store, ArrayList<ProductStore> productsList){
        this.Price = price;
        this.store = store;
        this.productsList = productsList;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getPrice() {
        return Price;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void setProductsList(ArrayList<ProductStore> productsList) {
        this.productsList = productsList;
    }

    public ArrayList<ProductStore> getProductsList() {
        return productsList;
    }

}
