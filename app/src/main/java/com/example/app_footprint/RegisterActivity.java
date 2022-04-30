package com.example.app_footprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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
    private String number;
    private RequestQueue requestQueue;
    private String basicurl = "https://studev.groept.be/api/a21pt105/";

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
    }

    public void onBtnCancel_Clicker(View caller){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onBtnSendEmail_Clicker(View caller){
        String textEmail = emailAddress.getText().toString();
        if(textEmail.equals("")){
            errorMessage.setText("The email is empty");
        }
        else{
            SendEmail email = new SendEmail(textEmail);
            email.sendEmail("Test email", "code");
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
            else{
                requestQueue = Volley.newRequestQueue(this);
                JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET,
                        basicurl+"newUser/"+textPassword+"/"+textEmail+"/"+textName,
                        null,null,
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                errorMessage.setText(error.getLocalizedMessage());
                            }
                        }
                );
                requestQueue.add(submitRequest);
            }
        }
    }
}