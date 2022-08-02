package com.example.smartcart;

import java.io.Serializable;

public class ProductPrice implements Serializable {
    private String product;
    private String price;

    public ProductPrice(String product, String price){
        this.product = product;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
