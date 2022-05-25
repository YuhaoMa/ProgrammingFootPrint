package com.example.app_footprint.module;

import com.google.android.gms.maps.model.LatLng;

public class Position {
    private LatLng latLng;
    private String date;
    private String name;

    public Position(LatLng latLng, String date, String name) {
        this.latLng = latLng;
        this.date = date;
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
