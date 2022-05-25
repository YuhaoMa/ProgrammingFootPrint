package com.example.app_footprint.module;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Photo {
    private String name;
    private String date;
    private Bitmap bitmap;
    private String bitmapBase;
    public Photo(String name, String date,String bitmapBase)
    {
        this.name = name;
        this.date = date;
        this.bitmapBase = bitmapBase;
        byte[] imageBytes = Base64.decode( bitmapBase, Base64.DEFAULT );
        bitmap = BitmapFactory.decodeByteArray( imageBytes
                , 0, imageBytes.length );
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBitmapBase() {
        return bitmapBase;
    }

    public void setBitmapBase(String bitmapBase) {
        this.bitmapBase = bitmapBase;
    }
}
