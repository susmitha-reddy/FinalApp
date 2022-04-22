package com.example.smartcart;

import android.location.Address;
import android.location.Location;

import java.io.Serializable;

public class Store implements Serializable {

    private String name;
    private String zipCode;
    private Double distance;
    private String address;
    private Double lat;
    private Double lng;

    public Store(String name, String zipCode, Double distance, String address, Double lat, Double lng){

        this.name = name;
        this.zipCode = zipCode;
        this.distance = distance;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public Store() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String address) {
        this.address = address;
    }

    public String getLocation() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}

