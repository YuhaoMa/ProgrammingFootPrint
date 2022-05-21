package com.example.app_footprint;

import static com.example.app_footprint.Json.getModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ShowActivity extends AppCompatActivity {
    private GridView gridView;
    private static List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private SimpleAdapter simpleAdapter;
    //private Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        displayData();
        gridView = (GridView) findViewById(R.id.view_photo);
        simpleAdapter = new SimpleAdapter(this,data,R.layout.grid_item,
                new String[]{"img","txt"},new int[]{R.id.img_item,R.id.txt_item});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object bitmapData, String s) {
                if(view instanceof ImageView && bitmapData instanceof Bitmap){
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap)bitmapData);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);
    }

    public static void setData(Bitmap bitmap, String date) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img",bitmap);
            map.put("txt",date);
            data.add(map);
    }

    public static void clearData(){
        data.clear();
    }

    public static void displayData(){
        for(int i = 0; i < data.size(); i++){
            System.out.println(data.get(i).toString());
        }
    }
}