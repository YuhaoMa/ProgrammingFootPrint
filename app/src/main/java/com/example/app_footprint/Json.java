package com.example.app_footprint;

import static com.example.app_footprint.MapsActivity.getmMap;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.module.LoginViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Json extends AppCompatActivity {
    private final static String url = "https://studev.groept.be/api/a21pt105/";
    private static Model model = new Model();
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
                                Marker marker=getmMap().addMarker(new MarkerOptions().position(latLng)
                                        .title(curObject.getString("date")));
                                        //.snippet(model.findByUserId("9","2").get(0).getGroupId()));
                                marker.setTag(i);
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
            ,String user) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url + "newPosition/" + lat + "/" + log + "/" + date + "/" + label + "/" + user
                , null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getLocalizedMessage();
                    }
                }
        );
        return jsonArrayRequest;
    }

    public static JsonArrayRequest getGroupPosition(String name){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"getGroupPosition/"+name
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
                                        .snippet(curObject.getString("userId")));
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

    public static JsonArrayRequest addPhotoInfo(String date, String pId
            , String uId, String gId)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url + "addPhotoInfo/" + date + "/" + pId + "/" + date + "/" + uId + "/" + gId
                , null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getLocalizedMessage();
                    }
                }
        );
        return jsonArrayRequest;
    }

    public static StringRequest addPhoto(ProgressDialog progressDialog,Activity activity
            ,String imageString){
        StringRequest submitRequest = new StringRequest (Request.Method.POST
                , url+"addPhoto"
                ,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off
                progressDialog.dismiss();
                Toast.makeText(activity, "Post request executed", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Post request failed", Toast.LENGTH_LONG).show();
            }
        }) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("photobase", imageString);
                return params;
            }
        };
        return submitRequest;
    }

    public static JsonArrayRequest showPhoto(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"show", null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            JSONObject curObject = new JSONObject();
                            for(int i = 0; i < response.length();i++){
                                curObject = response.getJSONObject(i);
                                Position position = new Position
                                        (curObject.getString("PhotoBase"),curObject.getString("date")
                                        ,curObject.getString("userId"),curObject.getString("groupId"),
                                                curObject.getString("positionId"));
                                model.getmData().add(position);
                            }
                            //System.out.println(model.getmData().get(0).getPhotoBase());
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

    public static Model getModel() {
        return model;
    }
}
