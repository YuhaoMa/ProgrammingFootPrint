package com.example.app_footprint.module;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Photos extends AbstractPhotos{
    private int groupId;
    private int userId;
    private double latitude;
    private double longitude;
    private List<Photo> photos;

    public Photos(int groupId,int userId,double latitude,double longitude){
        this.groupId = groupId;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        photos = new ArrayList<>();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        showActivityNotifier.setGridView(photos);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
