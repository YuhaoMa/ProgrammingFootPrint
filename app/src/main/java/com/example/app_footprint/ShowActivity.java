package com.example.app_footprint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.Photo;
import com.example.app_footprint.module.Photos;
import com.example.app_footprint.module.ShowActivityNotifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowActivity extends AppCompatActivity implements ShowActivityNotifier {
    private GridView gridView;
    private SimpleAdapter simpleAdapter;
    private RequestQueue requestQueue;
    private Photos photosModel;
    //private Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Bundle extras = getIntent().getExtras();
        requestQueue = Volley.newRequestQueue(this);
        photosModel = new Photos(extras.getInt("groupid"),extras.getInt("userid")
                ,extras.getDouble("latitude"),extras.getDouble("longitude"));
        photosModel.setShowActivityNotifier(this);
        Json baseConnection = new Json(requestQueue);
        baseConnection.getPhoto(photosModel);
    }

    private void setData(Bitmap bitmap, String date,String name,List<Map<String,Object>> data) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img",bitmap);
            map.put("date",date);
            map.put("name",name);

            data.add(map);
    }
    public void onClick_return(View caller){
        finish();
    }

    @Override
    public void setGridView(List<Photo> photosList) {
        for(int i = 0; i< photosList.size();i++){
            System.out.println(photosList.get(i).toString());
        }
        gridView = (GridView) findViewById(R.id.view_photo);
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < photosList.size();i++){
            setData(photosList.get(i).getBitmap(),photosList.get(i).getDate(),photosList.get(i).getName(),data);
        }
        simpleAdapter = new SimpleAdapter(this,data,R.layout.grid_item,
                new String[]{"img","date","name"},new int[]{R.id.img_item,R.id.date_item,R.id.name_item});
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
}