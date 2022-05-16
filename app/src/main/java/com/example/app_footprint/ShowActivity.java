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
    private List<Map<String,Object>> data;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        data = new ArrayList<Map<String, Object>>();
        gridView = (GridView) findViewById(R.id.view_photo);
        simpleAdapter = new SimpleAdapter(this,getData(),R.layout.grid_item,
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

    private List<Map<String,Object>> getData() {
        for (int i = 0; i < getModel().getmData().size();i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img",getModel().getmData().get(i).getBitmap());
            map.put("txt",getModel().getmData().get(i).getDate());
            data.add(map);
        }
        return data;
    }
}