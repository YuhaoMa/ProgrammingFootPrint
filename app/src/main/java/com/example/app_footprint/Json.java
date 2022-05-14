package com.example.app_footprint;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Json extends AppCompatActivity {
    private final static String url = "https://studev.groept.be/api/a21pt105/";

    public static JsonArrayRequest getUserData()
    {
        ArrayList<ArrayList<String>> UserData = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "UserData", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                           for(int i=0;i<response.length();i++)
                           {
                               JSONObject curObject = response.getJSONObject( i );
                               ArrayList<String> user = new ArrayList<>();
                               user.add(curObject.getString("emailaddress").toString());
                               user.add(curObject.getString("Password").toString());
                               user.add(curObject.getString("Name").toString());
                               user.add(curObject.getString("HeadPhoto").toString());
                               UserData.add(user);
                           }

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
                        System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
        );
         MainActivity.setUserData(UserData);
        return jsonArrayRequest;

    }

    public static JsonArrayRequest LoginSuccessfully(String address, String username, Intent intent, Activity activity)
    {
        ArrayList<String> Userdata = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+ "getGroup/"+address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // System.out.println("response!!!!!!!!!!!!!!!!!!!"+response);
                        try
                        {
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject curObject = response.getJSONObject( i );

                                Userdata.add(curObject.getString("GroupName").toString());
                                //System.out.println("!!!!!object+"+Userdata);
                            }
                            intent.putExtra("username",username);
                            intent.putExtra("address",address);
                            System.out.println("Userdata!!!!!!!!!"+Userdata);
                            intent.putExtra("GroupInfo",Userdata);
                            activity.startActivity(intent);
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
                        System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
        );
        // System.out.println("find!!!!!!!!!!!!!!!!!!!!!!!!!!!"+Userdata);

        return jsonArrayRequest;

    }
    public static JsonArrayRequest SearchGroup(EditText code, AlertDialog.Builder builder, Activity activity,String address )
    {
        ArrayList<String> codes = new ArrayList<String>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+ "searchGroupCode", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try
                        {
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                System.out.println("Group Code INfo:::::::::::::::"+curObject);
                                codes.add(curObject.get("code").toString());
                            }
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
                        System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
        );

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("你输入的是: " + code.getText().toString());
                AlertDialog.Builder builder1=new AlertDialog.Builder(activity);
                builder1.setTitle("Reminder");
                boolean check = false;

                for(int i =0 ; i< codes.size();i++)
                {
                    if(codes.get(i).equals(code.getText().toString()))
                    {
                        check = true;
                    }
                }
                if(check)
                {
                    builder1.setMessage("You've successfully joined a new group");
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    JsonArrayRequest jsonArrayRequest1 = Json.insertNewGroup(address,code.getText().toString());
                    requestQueue.add(jsonArrayRequest1);
                }
                else
                {
                    builder1.setMessage("Error code, the group does not exist");

                }
                builder1.setPositiveButton("OK",null);

                AlertDialog dialog1=builder1.create();
                dialog1.show();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
        return jsonArrayRequest;


    }
    public static JsonArrayRequest newUser(String textPassword,String textEmail,String textName,TextView errorMessage)
    {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET,
                url+"newUser/"+textPassword+"/"+textEmail+"/"+textName,
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
        return submitRequest;
    }
public static JsonArrayRequest insertNewGroup(String user, String code )
{
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"addGroup/"+user+"/"+code
            , null,
            new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response)
                {

                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                    System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
    );
    return jsonArrayRequest;
}

public JsonArrayRequest getUserInfo(String email,TextView textView)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"getUserInfo/"+email
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        textView.setText(error.getLocalizedMessage());
                    }
                }
        );
        return jsonArrayRequest;
    }
    public static JsonArrayRequest getGroup(String user)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"getGroup/"+user
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            JSONObject curObject = new JSONObject();
                            String responseString = "";
                            for(int i = 0; i < response.length();i++){
                                curObject = response.getJSONObject(i);
                                responseString = curObject.getString("GroupName").toString();
                                LoginViewModel.setGroups(responseString);
                            }
                            System.out.println(LoginViewModel.getGroups());
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
                        error.getLocalizedMessage();
                    }
                }
        );
        return jsonArrayRequest;
    }

}
