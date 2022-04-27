package com.example.app_footprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.example.app_footprint.module.LoginViewModel;
import com.example.app_footprint.presenter.ViewModeNoti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ViewModeNoti viewModeNoti = new LoginViewModel();
    private Button btnLogin;
    private EditText email;
    private TextView passwd;
    private RequestQueue requestQueue;
    private String basicurl = "https://studev.groept.be/api/a21pt105/";
    private TextView sees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button)findViewById(R.id.btn_login);
        email = findViewById(R.id.txtName);
        passwd = (TextView)findViewById(R.id.editTextTextPassword);
        sees = findViewById((R.id.textView3));
    }

    public void onBtnLogin_Clicker(View caller){
        String user = email.getText().toString();
        String password = passwd.getText().toString();
        requestQueue = Volley.newRequestQueue(this);
        //if(user.equals('1') ){
          //  System.out.println("True");

        //}
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, basicurl+"login/"+user
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            String responseString = "";
                            JSONObject curObject = response.getJSONObject( 0 );
                            responseString = curObject.getString("Password").toString();
                            if(responseString.equals(password) ){
                                sees.setText("checked");
                            }
                            else {
                                sees.setText("not correct");
                            }
                            //sees.setText(responseString);
                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        sees.setText(error.getLocalizedMessage());
                    }
                }
        );

        requestQueue.add(submitRequest);
    }


}