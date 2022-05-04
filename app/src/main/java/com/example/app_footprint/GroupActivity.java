package com.example.app_footprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.LoginViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity{
    private String user;
    private ListView listView;
    private TextView message;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        System.out.println("Name is"+intent.getStringExtra("Names"));
        user = intent.getStringExtra("Names");
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest bRequest = Json.getGroup(user);
        requestQueue.add(bRequest);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                GroupActivity.this, android.R.layout.simple_list_item_1,LoginViewModel.getGroups());
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        message=findViewById(R.id.textView9);
        System.out.println(LoginViewModel.getGroups().toString()+" HHHHH");
        System.out.println(user);
        message.setText(LoginViewModel.getGroups().toString());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String text = LoginViewModel.getGroups().get(position);
                message.setText(text);
            }
        });
    }


}