package com.example.app_footprint;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
    private final static String url = "https://studev.groept.be/api/a21pt105/";


    private static JSONObject jsonObject = null;
    private static JSONArray  jsonArray = null;
    public JsonArrayRequest LogIn(String user, String password, TextView sees)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"login/"+user
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
        return jsonArrayRequest;
    }

    public JsonArrayRequest newUser(String textPassword,String textEmail,String textName,TextView errorMessage)
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
