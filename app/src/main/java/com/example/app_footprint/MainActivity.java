package com.example.app_footprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.LoginViewModel;
import com.example.app_footprint.presenter.ViewModeNoti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegister;
    private EditText email;
    private TextView passwd;
    private RequestQueue requestQueue;
    private TextView sees;
    //private static ArrayList<String> groupNames = new ArrayList<>();
    private static boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnRegister = (Button)findViewById(R.id.btn_register);
        email = findViewById(R.id.txtName);
        passwd = (TextView)findViewById(R.id.editTextTextPassword);
        sees = findViewById((R.id.textView3));
    }

    public void onBtnLogin_Clicker(View caller){
        String user = email.getText().toString();
        String password = passwd.getText().toString();
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest submitRequest = Json.LogIn(user,password,sees);
        requestQueue.add(submitRequest);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(check){
            //JsonArrayRequest bRequest = Json.getGroup(user);
            //requestQueue.add(bRequest);
            Intent intent = new Intent(MainActivity.this,GroupActivity.class);
            intent.putExtra("Names", user);
            startActivity(intent);
        }
    }

    public void onBtnRegister_Clicker(View Caller){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public static void setCheck(boolean x){
        check = x;
    }


}