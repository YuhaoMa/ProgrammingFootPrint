package com.example.app_footprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app_footprint.module.LoginViewModel;
import com.example.app_footprint.presenter.ViewModeNoti;

public class MainActivity extends AppCompatActivity {
    ViewModeNoti viewModeNoti = new LoginViewModel();
    private Button btnLogin;
    private EditText email;
    private TextView passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button)findViewById(R.id.btn_login);
        email = findViewById(R.id.txtName);
        passwd = (TextView)findViewById(R.id.editTextTextPassword);
    }

    public void onBtnLogin_Clicker(View caller){
        String user = email.getText().toString();
        String password = passwd.getText().toString();
        //if(user.equals('1') ){
          //  System.out.println("True");

        //}
        if(viewModeNoti.checkUserInfo(user,password)){
            System.out.println("OK");
        }
        else {
            System.out.println("False");
        }
    }

}