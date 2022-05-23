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
import com.google.android.gms.maps.CameraUpdateFactory;
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
    public static JsonArrayRequest getUserInfo(String email,String password,TextView textView
            ,Intent intent,Activity activity)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "getUserInfo/" + email,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (!response.getJSONObject(0).getString("Password").equals(password)) {
                                textView.setText("Incorrect Password");
                            }
                            else{
                                Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show();
                                String userName = response.getJSONObject(0).getString("Name");
                                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                                JsonArrayRequest jsonArrayRequest1 = Json.LoginSuccessfully(email,userName
                                        ,intent,activity,response.getJSONObject(0).getString("idUsers"));
                                requestQueue.add(jsonArrayRequest1);
                            }
                        } catch (JSONException e) {
                            textView.setText("Invalid User, Please Sing Up");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        return jsonArrayRequest;
    }

    public static JsonArrayRequest forgetMyPassword(String address, AlertDialog.Builder builder,
                                                    Activity activity){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "getUserInfo/" + address,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            if(address.equals(response.getJSONObject(0).getString("emailaddress"))){
                                GenerateCode generateCode = new GenerateCode(8);
                                String Sendcode = generateCode.generateCode();
                                SendMailUtil.send(address, Sendcode, 3, null);
                                builder.setMessage("Enter the Verification Code ");
                                EditText textCode = new EditText(activity);
                                builder.setView(textCode);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
                                        builder2.setMessage("Enter the new Password ");
                                        EditText textPassword = new EditText(activity);
                                        builder2.setView(textPassword);
                                        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(activity);
                                                builder3.setMessage("Set Successfully ");
                                                builder3.setPositiveButton("OK", null);
                                                Intent intent = new Intent(activity, MainActivity.class);
                                                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                                                requestQueue.add(Json.changePassword(textPassword.getText().toString(),address,
                                                        intent,builder3,activity));
                                            }
                                        });
                                        AlertDialog dialog2 = builder2.create();
                                        dialog2.show();
                                    }
                                });
                                AlertDialog dialog1 = builder.create();
                                dialog1.show();
                            }
                        }
                        catch (JSONException e){
                            builder.setMessage("User doesn't exist!");
                            builder.setPositiveButton("Close",null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        return jsonArrayRequest;
    }
    public static JsonArrayRequest LoginSuccessfully(String address, String username, Intent intent
            , Activity activity,String id)
    {
        ArrayList<String> Userdata = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+ "getGroup/"+address, null,
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
                        error.printStackTrace();
                    }
                }
        );
        return jsonArrayRequest;

    }
    public static JsonArrayRequest SearchGroup(EditText code, AlertDialog.Builder builder
            ,Activity activity,String address, String userName,Intent intent, String id)
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
                                //System.out.println("Group Code INfo:::::::::::::::"+curObject);
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
                        //System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        error.printStackTrace();
                    }
                }
        );

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println("你输入的是: " + code.getText().toString());
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
                                    JsonArrayRequest jsonArrayRequest1 = Json.insertNewGroup(address
                                            ,code.getText().toString(),intent,activity,userName,id);
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
    public static JsonArrayRequest insertNewGroup(String address, String code,Intent intent
            ,Activity activity ,String userName, String id)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+"addGroup/"+address+"/"+code, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        RequestQueue requestQueue = Volley.newRequestQueue(activity);
                        JsonArrayRequest jsonArrayRequest1 = Json.LoginSuccessfully(address,userName
                                ,intent,activity,id);
                        requestQueue.add(jsonArrayRequest1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
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
                        error.printStackTrace();
                    }
                }
        );
        return submitRequest;
    }

    public static JsonArrayRequest createNewGroup(EditText groupName,String address,String userName
            , Activity activity,Intent intent,AlertDialog.Builder builder,String id )
    {
        GenerateCode generateCode = new GenerateCode(3);
        String Sendcode = generateCode.generateCode();
        //System.out.println("Genearate a new Groupcode:::: "+Sendcode);
        //System.out.println("Create a new Group name is :::: "+groupName.getText().toString());
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
                        error.printStackTrace();
                    }
                }
        );
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println("你输入的是: " + groupName.getText().toString());
                AlertDialog.Builder builder1=new AlertDialog.Builder(activity);
                builder1.setTitle("Reminder");
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
                System.out.println("GroupName:::::"+groupName.getText().toString());
                if(groupName.getText().toString()!= null)
                {
                    builder1.setMessage("The group \""+groupName.getText().toString()
                            +"\" was created successfully!\n" + "Invitation code :"+Sendcode);
                    SendMailUtil.send(address,Sendcode,2,groupName.getText().toString());
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            JsonArrayRequest jsonArrayRequest1 = Json.setGroupName(Sendcode
                                    ,groupName.getText().toString(), address,intent,activity
                                    ,userName,id);
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

    public static JsonArrayRequest setGroupName(String code,String GroupName,String address
            ,Intent intent,Activity activity,String userName,String id)
    {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET,
                url+"setGroupName/"+GroupName+"/"+code, null, null,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
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
                url+"getMyPosition/"+user, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            JSONObject curObject;
                            LatLng latLng;
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
            ,String user)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url + "newPosition/" + lat + "/" + log + "/" + date + "/" + label + "/" + user
                , null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(log));
                Marker marker=getmMap().addMarker(new MarkerOptions().position(latLng)
                        .title(date)
                        .snippet(user));
                marker.setTag(String.valueOf(Integer.getInteger(positionId))+1);
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

    public static JsonArrayRequest getGroupPosition(String name){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"getGroupPosition/"+name
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            JSONObject curObject;
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

    public static JsonArrayRequest getPhoto(String uid, String gid, String pid,Intent intent
            , Activity activity)
    {
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();;
        JsonArrayRequest jsonArrayRequest;
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
        else{
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url+"getGroupPhotos/"+uid+"/"+gid+"/"+pid
                    , null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
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
        return jsonArrayRequest;

    }

    public static JsonArrayRequest changePassword(String password, String emailaddress, Intent intent, AlertDialog.Builder builder, Activity activity)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"changePassword/"+password+"/"+emailaddress, null,
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
                        error.getLocalizedMessage();
                    }
                }
        );
        AlertDialog dialog3 = builder.create();
        dialog3.show();
        activity.startActivity(intent);
        return jsonArrayRequest;

    }
}
