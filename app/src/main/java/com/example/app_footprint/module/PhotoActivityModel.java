package com.example.app_footprint.module;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivityModel{
    private String userid;
    private String date;
    private String bitmapBase;
    private double latitude;
    private double longitude;
    private String groupId;
    private Bitmap bitmap;

    public PhotoActivityModel(String userid, String groupId) {
        this.userid = userid;
        Date dNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(dNow);
        this.groupId = groupId;
        bitmapBase = null;
        bitmap = null;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBitmapBase() {
        return bitmapBase;
    }

    public void setBitmapBase() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        Base64.encodeToString(imageBytes, Base64.DEFAULT);
        this.bitmapBase = Base64.encodeToString(imageBytes, Base64.DEFAULT);
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
