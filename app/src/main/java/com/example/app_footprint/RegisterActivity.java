package com.example.app_footprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.Email.GenerateCode;
import com.example.app_footprint.Email.SendMailUtil;

import java.security.GeneralSecurityException;
import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;


public class RegisterActivity extends AppCompatActivity {
    private Button btnConfirm;
    private Button btnCancel;
    private Button btnSendEmail;
    private TextView emailAddress;
    private TextView password;
    private TextView repeatPassword;
    private TextView code;
    private TextView name;
    private TextView errorMessage;
    private RequestQueue requestQueue;
    private  String Sendcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnConfirm = (Button) findViewById(R.id.btn_Confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSendEmail = (Button) findViewById(R.id.btn_sendEmail);
        emailAddress = (TextView) findViewById(R.id.text_emailAddress);
        password = (TextView) findViewById(R.id.text_password);
        repeatPassword = (TextView) findViewById(R.id.text_repeatPassword);
        name = (TextView) findViewById(R.id.text_name);
        code = (TextView) findViewById(R.id.text_code);
        errorMessage = (TextView) findViewById(R.id.message);
        Sendcode = null;
    }

    public void onBtnCancel_Clicker(View caller){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onBtnSendEmail_Clicker(View caller) throws GeneralSecurityException {
        String textEmail = emailAddress.getText().toString();
        if(textEmail.equals("")){
            errorMessage.setText("The email is empty");
        }
        else{
            GenerateCode generateCode = new GenerateCode(8);
             Sendcode = generateCode.generateCode();
            SendMailUtil.send(textEmail,Sendcode,1,null);
        }
    }


    public void onBtnConfirm_Clicker(View Caller){
        String textEmail = emailAddress.getText().toString();
        String textName = name.getText().toString();
        String textPassword = password.getText().toString();
        String textRepeatPassword = repeatPassword.getText().toString();
        if(textEmail.equals("")||textName.equals("")||textPassword.equals("")
                ||textRepeatPassword.equals("")){
            errorMessage.setText("Info incomplete");
        }
        else{
            if(!textPassword.equals(textRepeatPassword)){
                errorMessage.setText("The password is different.");
            }
            else if(code.getText().toString().equals(Sendcode)==false)
            {
                errorMessage.setText("Code incorrect!.");
            }
            else{
                requestQueue = Volley.newRequestQueue(this);
                JsonArrayRequest submitRequest = Json.newUser(textPassword,textEmail,
                        textName,errorMessage);
                 requestQueue.add(submitRequest);
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}