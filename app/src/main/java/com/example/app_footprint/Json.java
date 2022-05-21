package com.example.app_footprint;

import static com.example.app_footprint.MapsActivity.currentLatitude;
import static com.example.app_footprint.MapsActivity.currentLongitude;
import static com.example.app_footprint.MapsActivity.getmMap;
import static com.example.app_footprint.MapsActivity.group;
import static com.example.app_footprint.MapsActivity.positionId;
import static com.example.app_footprint.ShowActivity.clearData;
import static com.example.app_footprint.ShowActivity.setData;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
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
import com.example.app_footprint.Email.GenerateCode;
import com.example.app_footprint.Email.SendMailUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                               user.add(curObject.getString("idUsers").toString());
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

    public static JsonArrayRequest LoginSuccessfully(String address, String username, Intent intent, Activity activity,String id)
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
                                group.put(curObject.getString("GroupName").toString(),
                                        curObject.getString("idGroup").toString());
                                Userdata.add(curObject.getString("GroupName").toString());
                                //System.out.println("!!!!!object+"+Userdata);
                            }
                            intent.putExtra("username",username);
                            intent.putExtra("address",address);
                            System.out.println("Userdata!!!!!!!!!"+Userdata);
                            intent.putExtra("GroupInfo",Userdata);
                            intent.putExtra("id",id);
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
    public static JsonArrayRequest SearchGroup(EditText code, AlertDialog.Builder builder, Activity activity,String address,
    String userName,Intent intent, String id)
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
                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                                    JsonArrayRequest jsonArrayRequest1 = Json.insertNewGroup(address,code.getText().toString(),intent,activity,userName,id);
                                    requestQueue.add(jsonArrayRequest1);
                            }
                        });
                    }
                    else
                    {
                        builder1.setMessage("Error code, the group does not exist");
                        builder1.setPositiveButton("Cancel",null);
                    }
                AlertDialog dialog1=builder1.create();
                dialog1.show();
            }
        });
      builder.setNegativeButton("Cancel",null);
        AlertDialog dialog=builder.create();
        dialog.show();
        return jsonArrayRequest;


    }
    public static JsonArrayRequest insertNewGroup(String address, String code,Intent intent,Activity activity ,String userName, String id)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"addGroup/"+address+"/"+code
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        RequestQueue requestQueue = Volley.newRequestQueue(activity);
                        JsonArrayRequest jsonArrayRequest1 = Json.LoginSuccessfully(address,userName,intent,activity,id);
                        requestQueue.add(jsonArrayRequest1);
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

    public static JsonArrayRequest createNewGroup(EditText groupName,String address,String userName,
                                                  Activity activity,Intent intent,
                                                  AlertDialog.Builder builder,String id )
    {
        GenerateCode generateCode = new GenerateCode(3);
        String Sendcode = generateCode.generateCode();
        System.out.println("Genearate a new Groupcode:::: "+Sendcode);



                System.out.println("Create a new Group name is :::: "+groupName.getText().toString());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"createGroup/"+Sendcode+"/"+
                groupName.getText().toString()
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("你输入的是: " + groupName.getText().toString());
                AlertDialog.Builder builder1=new AlertDialog.Builder(activity);
                builder1.setTitle("Reminder");
                RequestQueue requestQueue = Volley.newRequestQueue(activity);

                if(groupName.getText().toString()!= null)
                {
                    builder1.setMessage("The group \""+groupName.getText().toString()+"\" was created successfully!\n" +
                            "Invitation code :"+Sendcode);
                    SendMailUtil.send(address,Sendcode,2,groupName.getText().toString());
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            JsonArrayRequest jsonArrayRequest1 = Json.setGroupName(Sendcode,groupName.getText().toString(),
                                    address,intent,activity,userName,id);
                            requestQueue.add(jsonArrayRequest1);
                        }
                    });
                }
                else
                {
                    builder1.setMessage("The group name cannot be blank!");
                    builder1.setPositiveButton("Cancel",null);
                }

                AlertDialog dialog1=builder1.create();
                dialog1.show();
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog=builder.create();
        dialog.show();
        return jsonArrayRequest;
    }

    public static JsonArrayRequest setGroupName(String code,String GroupName,String address,Intent intent
    ,Activity activity,String userName,String id)
    {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET,
                url+"setGroupName/"+GroupName+"/"+code,
                null,
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
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JsonArrayRequest jsonArrayRequest1 = Json.insertNewGroup(address,code,intent,activity,userName,id);
        requestQueue.add(jsonArrayRequest1);
        return submitRequest;
    }

    public static JsonArrayRequest getMyPosition(String user)
    {
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
                                        .title(curObject.getString("date"))
                                        .snippet(curObject.getString("userId")));
                                marker.setTag(curObject.getString("idPositions"));
                                if(i==response.length()-1){
                                    positionId = curObject.getString("idPositions");
                                    currentLatitude = Double.parseDouble(curObject.getString("latitude"));
                                    currentLongitude = Double.parseDouble(curObject.getString("longitude"));
                                }
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
                                Marker marker = getmMap().addMarker(new MarkerOptions().position(latLng)
                                        .title(curObject.getString("date"))
                                        .snippet(curObject.getString("userId")));
                                marker.setTag(curObject.getString("idPositions"));
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

    public static JsonArrayRequest addPhotoInfo(String date, String uId
            , String gId, String pId,ProgressDialog progressDialog, Activity activity
            , String imageString)
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url + "addPhotoInfo/" + date + "/" + uId + "/" + gId + "/" + pId
                , null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("ADD photo info########");
                        RequestQueue requestQueue = Volley.newRequestQueue(activity);
                        StringRequest submitRequest = Json.addPhoto(progressDialog,activity,imageString);
                        requestQueue.add(submitRequest);
                    }
                },
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
        System.out.println(imageString);
        StringRequest submitRequest = new StringRequest (Request.Method.POST
                , url+"addPhoto"
                ,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off
                System.out.println("ADD photo %%%%%%%%%");
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

    public static Model getModel() {
        return model;
    }

    public static JsonArrayRequest getPhoto(String uid, String gid, String pid,Intent intent
            , Activity activity)
    {
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();;
        JsonArrayRequest jsonArrayRequest;
        System.out.println(uid);
        System.out.println(gid);
        System.out.println(pid);
        clearData();
        if(gid==null){
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url+"getMyPhoto/"+uid+"/"+pid
                    , null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            System.out.println("getmyphoto!!!!!!!!!");
                            try
                            {
                                JSONObject curObject = new JSONObject();
                                String photoBase;
                                Bitmap bitmap;
                                for(int i = 0; i < response.length();i++){
                                    curObject = response.getJSONObject(i);
                                    photoBase = curObject.getString("PhotoBase");
                                    byte[] imageBytes = Base64.decode( photoBase, Base64.DEFAULT );
                                    bitmap = BitmapFactory.decodeByteArray( imageBytes
                                            , 0, imageBytes.length );
                                    setData(bitmap,curObject.getString("date"));
                                }
                                //intent.putExtra("data", (Serializable) data);
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
                            error.getLocalizedMessage();
                        }
                    }
            );
        }
        else{
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url+"getGroupPhotos/"+uid+"/"+gid+"/"+pid
                    , null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            System.out.println("getmyphoto!!!!!!!!!");
                            try
                            {
                                JSONObject curObject = new JSONObject();
                                String photoBase;
                                Bitmap bitmap;
                                for(int i = 0; i < response.length();i++){
                                    curObject = response.getJSONObject(i);
                                    photoBase = curObject.getString("PhotoBase");
                                    byte[] imageBytes = Base64.decode( photoBase, Base64.DEFAULT );
                                    bitmap = BitmapFactory.decodeByteArray( imageBytes
                                            , 0, imageBytes.length );
                                    setData(bitmap,curObject.getString("date"));
                                }
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
                            error.getLocalizedMessage();
                        }
                    }
            );

        }
        Bundle bundle = new Bundle();


        return jsonArrayRequest;
    }
}
