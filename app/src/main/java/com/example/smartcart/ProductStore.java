package com.example.smartcart;

import java.io.Serializable;

import io.realm.annotations.Required;

public class ProductStore implements Serializable {
    private String Product;
    private Double Price;
    private String Store;
    private String Zipcode;

    public ProductStore( String Product, Double Price, String Store, String Zipcode){
        //this._id = id;
        this.Product = Product;
        this.Price = Price;
        this.Store = Store;
        this.Zipcode = Zipcode;
    }

    public ProductStore(){}


    public void setProduct(String product) {
        Product = product;
    }

    public String getProduct() {
        return Product;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }



    public void setStore(String store) {
        Store = store;
    }

    public String getStore() {
        return Store;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getZipcode() {
        return Zipcode;
    }
}

