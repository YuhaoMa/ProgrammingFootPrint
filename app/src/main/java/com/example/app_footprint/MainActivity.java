package com.example.app_footprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.service.autofill.UserData;
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
import com.example.app_footprint.Email.GenerateCode;
import com.example.app_footprint.Email.SendMailUtil;
import com.example.app_footprint.module.LoginViewModel;
import com.example.app_footprint.presenter.ViewModeNoti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private TextView passwd;
    private RequestQueue requestQueue;
    private TextView sees;
    private static ArrayList<ArrayList<String>> UserData;
    private static boolean check = false;
    private  String username;
    private  String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.txtName);
        passwd = (TextView) findViewById(R.id.editTextTextPassword);
        sees = (TextView) findViewById((R.id.error_message));
        requestQueue = Volley.newRequestQueue(this);
    }

    public void onBtnLogin_Clicker(View caller) {
        requestQueue = Volley.newRequestQueue(this);
        String user = email.getText().toString();
        String password = passwd.getText().toString();
        /*int id = 0;
        for (ArrayList<String> userData : UserData) {
            if (user.equals(userData.get(0))) {
                if (password.equals(userData.get(1))) {
                    MainActivity.setCheck(true);
                    setUsername(userData.get(2));
                    setEmail(userData.get(0));
                    id = Integer.valueOf(userData.get(4));
                } else {
                    sees.setText("Password is incorrect");
                }
            }
        }
        if (check) {
            setCheck(false);
            Intent intent = new Intent(this,MapsActivity.class);
            requestQueue.add(Json.LoginSuccessfully(user,username,intent,this,String.valueOf(id)));

        }*/
        Intent intent = new Intent(this,MapsActivity.class);
        requestQueue.add(Json.getUserInfo(user,password,sees,intent,this));
    }


    public void onBtnRegister_Clicker(View Caller) {
        //Intent intent = new Intent(this, RegisterActivity.class);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void forgetBtn_Clicker(View view){
        GenerateCode generateCode = new GenerateCode(8);
         String Sendcode = generateCode.generateCode();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Enter your User emailaddress");
        EditText textEmail = new EditText(this);
        builder.setView(textEmail);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailaddress = textEmail.getText().toString();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                requestQueue.add(Json.forgetMyPassword(emailaddress,builder1,MainActivity.this));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void setCheck(boolean x) {
        check = x;
    }

    public static void setUserData(ArrayList<ArrayList<String>> User) {
        UserData = User;
    }


    public  String getUsername() {
        return username;
    }
    public void setEmail(String Emailaddress){ address = Emailaddress;}
    public void setUsername(String name){username = name;}

}