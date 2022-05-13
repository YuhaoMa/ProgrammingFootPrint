package com.example.app_footprint;

import static com.example.app_footprint.MapsActivity.getmMap;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.app_footprint.module.LoginViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Json extends AppCompatActivity {
    private final static String url = "https://studev.groept.be/api/a21pt105/";
    private static JSONObject jsonObject = null;
    private static JSONArray  jsonArray = null;
    public static JsonArrayRequest getUserData()
    {
        ArrayList<ArrayList<String>> UserData = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url + "UserData", null,
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

    public static JsonArrayRequest LoginSuccessfully(String address, String username, Intent intent
            , Activity activity)
    {
        ArrayList<String> Userdata = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+ "getGroup/"+address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                Userdata.add(curObject.getString("GroupName").toString());
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

        return jsonArrayRequest;

    }
    public static JsonArrayRequest newUser(String textPassword,String textEmail,String textName
            ,TextView errorMessage)
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

public JsonArrayRequest getUserInfo(String email,TextView textView)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"getUserInfo/"+email
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        jsonArray = response;
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

    public static JsonArrayRequest getMyPosition(String user){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"getMyPosition/"+user
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        System.out.println("Response!!!!!!!!!");
                        try
                        {
                            JSONObject curObject = new JSONObject();
                            LatLng latLng ;
                            for(int i = 0; i < response.length();i++){
                                curObject = response.getJSONObject(i);
                                latLng = new LatLng(
                                        Double.parseDouble(curObject.getString("latitude")),
                                        Double.parseDouble(curObject.getString("longitude")));
                                getmMap().addMarker(new MarkerOptions().position(latLng)
                                        .title(curObject.getString("date"))
                                        .snippet("Population: 4,137,400"));
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
                        error.getLocalizedMessage();
                    }
                }
        );
        return jsonArrayRequest;
    }

    public static JsonArrayRequest newPosition(String lat,String log, String date,String label
            ,String user)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"newPosition/"+lat+"/"+log+"/"+date+"/"+label+"/"+user
                , null, null,
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
    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static void setJsonObject(JSONObject jsonObject) {
        Json.jsonObject = jsonObject;
    }

    public static JSONArray getJsonArray() {
        return jsonArray;
    }

    public static void setJsonArray(JSONArray jsonArray) {
        Json.jsonArray = jsonArray;
    }
}
