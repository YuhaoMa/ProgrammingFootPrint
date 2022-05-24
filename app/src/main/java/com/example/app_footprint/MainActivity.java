package com.example.app_footprint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private TextView passwd;
    private RequestQueue requestQueue;
    private TextView sees;
    private static ArrayList<ArrayList<String>> UserData;
    private static boolean check = false;
    private  String username;
    private  String address;
   private static ArrayList<String> GroupInfo;
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
        Intent intent = new Intent(this, MapsActivity.class);
        requestQueue.add(Json.getUserInfo(user,password,sees,intent,this));
    }


    public void onBtnRegister_Clicker(View Caller) {
        //Intent intent = new Intent(this, RegisterActivity.class);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void forgetBtn_Clicker(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Enter your User emailaddress");
        EditText textEmail = new EditText(this);
        builder.setView(textEmail);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailaddress = textEmail.getText().toString();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                if(emailaddress.equals("")){
                    builder1.setMessage("Email address can't be empty.");
                    builder1.setPositiveButton("Close",null);
                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                }
                else{
                    requestQueue.add(Json.forgetMyPassword(emailaddress,builder1,MainActivity.this));
                }

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