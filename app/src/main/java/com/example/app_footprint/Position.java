package com.example.app_footprint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Position {
    private String photoBase;
    private String date;
    private String userId;
    private String positionId;
    private String groupId;
    private Bitmap bitmap;

    public String getPhotoBase() {
        return photoBase;
    }

    public void setPhotoBase(String photoBase) {
        this.photoBase = photoBase;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Position(String photoBase, String date, String userId, String positionId, String groupId) {
        this.photoBase = photoBase;
        this.date = date;
        this.userId = userId;
        this.positionId = positionId;
        this.groupId = groupId;
        byte[] imageBytes = Base64.decode( photoBase, Base64.DEFAULT );
        this.bitmap = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    /*public void setBitmap(String photoBase) {

    }*/
}
