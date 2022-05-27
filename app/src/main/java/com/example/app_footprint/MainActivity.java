package com.example.app_footprint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.MainActivityNotifier;
import com.example.app_footprint.module.Positions;
import com.example.app_footprint.module.UserModel;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityNotifier {
    private EditText email;
    private TextView passwd;
    private RequestQueue requestQueue;
    private TextView sees;
    private static ArrayList<ArrayList<String>> UserData;
    private static boolean check = false;
    private Positions positions;
    private  String address;
    private UserModel userModel;
    private Json baseConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.txtName);
        passwd = findViewById(R.id.editTextTextPassword);
        sees = findViewById((R.id.error_message));
        requestQueue = Volley.newRequestQueue(this);
        baseConnect = new Json(requestQueue,this);
        userModel = new UserModel();
        userModel.setMainActivityNotifier(this);
    }

    public void onBtnLogin_Clicker(View caller) {

        String user = email.getText().toString();
        String password = passwd.getText().toString();
        if(user.equals("") || password.equals(""))
        {
            Toast.makeText(this,"The information is incomplete.",Toast.LENGTH_SHORT).show();
        }
        else
        {

            userModel.setEmail(user);
            userModel.setPassword(password);
            baseConnect.getUserInfo(userModel,sees);
        }
    }


    public void onBtnRegister_Clicker(View Caller) {
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
                    userModel.setEmail(emailaddress);
                    baseConnect.forgetMyPassword(userModel);
                }

            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setEmail(String Emailaddress){ address = Emailaddress;}

    @Override
    public void jumpToMap() {
        baseConnect.LoginSuccessfully(userModel);
        sees.setText("");
    }

    @Override
    public void setWarningView(String SendCode) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Enter the Verification Code ");
        EditText textCode = new EditText(this);
        builder1.setView(textCode);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(textCode.getText().toString().equals(SendCode)){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setMessage("Enter the new Password ");
                    EditText textPassword = new EditText(MainActivity.this);
                    builder2.setView(textPassword);
                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(textPassword.getText().toString()  != "")
                            {
                                userModel.setPassword(textPassword.getText().toString());
                                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                                builder3.setMessage("Set Successfully ");
                                builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setResetSuccessfully();
                                    }
                                });
                                AlertDialog dialog3 = builder3.create();
                                dialog3.show();
                                baseConnect.changePassword(userModel);
                            }
                            else
                            {
                                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                                builder3.setMessage("Password can't be empty.");
                                builder3.setPositiveButton("OK", null);
                                AlertDialog dialog3 = builder3.create();
                                dialog3.show();
                            }
                        }

                    });
                    AlertDialog dialog2 = builder2.create();
                    dialog2.show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Incorrect Code", Toast.LENGTH_SHORT).show();
                }

            }
        });
        AlertDialog dialog1 = builder1.create();
        dialog1.show();
    }

    @Override
    public void setResetSuccessfully() {
        baseConnect.LoginSuccessfully(userModel);
    }

    @Override
    public void setErrorView() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("User doesn't exist!");
        builder1.setPositiveButton("Close",null);
        AlertDialog dialog1 = builder1.create();
        dialog1.show();
    }

    @Override
    public void parsePositions() {
        Intent intent = new Intent(this, MapsActivity.class);
        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
        intent.putExtra("Positions",(Serializable) userModel.getGroupMap());
        intent.putExtra("username",userModel.getUserName());
        intent.putExtra("address",userModel.getEmail());
        intent.putExtra("userId",userModel.getUserId());
        startActivity(intent);
    }
}