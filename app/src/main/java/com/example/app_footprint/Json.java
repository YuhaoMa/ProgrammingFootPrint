package com.example.app_footprint;

import static com.example.app_footprint.MapsActivity.currentLatitude;
import static com.example.app_footprint.MapsActivity.currentLongitude;
import static com.example.app_footprint.MapsActivity.getmMap;
import static com.example.app_footprint.MapsActivity.positionNum;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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
import com.example.app_footprint.module.Photo;
import com.example.app_footprint.module.PhotoActivityModel;
import com.example.app_footprint.module.Photos;
import com.example.app_footprint.module.Position;
import com.example.app_footprint.module.Positions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json extends AppCompatActivity {
    private final static String url = "https://studev.groept.be/api/a21pt105/";
    private RequestQueue requestQueue;
    public static ArrayList<Double> myLatitude = new ArrayList<>();
    public static ArrayList<Double> myLongitude = new ArrayList<>();

    public Json(RequestQueue requestQueue){
        this.requestQueue = requestQueue;
    }
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
                                textView.setText("");
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
                                                    Activity activity)
    {
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
                                        if(textCode.getText().toString().equals(Sendcode)){
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
                                                    AlertDialog dialog3 = builder3.create();
                                                    dialog3.show();
                                                    Intent intent = new Intent(activity, MainActivity.class);
                                                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                                                    requestQueue.add(Json.changePassword(textPassword.getText().toString(),address,
                                                            intent,builder3,activity));
                                                }

                                            });
                                            AlertDialog dialog2 = builder2.create();
                                            dialog2.show();
                                        }
                                        else {
                                            Toast.makeText(activity, "Incorrect Code", Toast.LENGTH_SHORT).show();
                                        }

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
                        //builder.setMessage("Email address can't be empty.");
                        //builder.setPositiveButton("Close",null);
                        //AlertDialog dialog1 = builder.create();
                        //dialog1.show();
                    }
                });
        return jsonArrayRequest;
    }

    public static JsonArrayRequest LoginSuccessfully(String address, String username, Intent intent
            , Activity activity,String userId)
    {
        ArrayList<String> groupname = new ArrayList<>();
        ArrayList<String> groupid = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+ "getGroup/"+address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                groupid.add(curObject.getString("idGroup").toString());
                                groupname.add(curObject.getString("GroupName").toString());

                            }
                            intent.putExtra("username",username);
                            intent.putExtra("address",address);
                            intent.putExtra("GroupId",groupid);
                            intent.putExtra("GroupName",groupname);
                            intent.putExtra("userId",userId);
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
            ,Activity activity,String address, String userName,Intent intent, String userid)
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
                        error.printStackTrace();
                    }
                }
        );

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                                            ,code.getText().toString(),intent,activity,userName,userid);
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
            ,Activity activity ,String userName, String userid)
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
                                ,intent,activity,userid);
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
                AlertDialog.Builder builder1=new AlertDialog.Builder(activity);
                builder1.setTitle("Reminder");
                RequestQueue requestQueue = Volley.newRequestQueue(activity);
               // System.out.println("GroupName:::::"+groupName.getText().toString());
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

    /*public static JsonArrayRequest getMyPosition(String useraddress)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"getMyPosition/"+useraddress, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try
                        {
                            JSONObject curObject;
                            LatLng latLng;
                            positionNum = response.length();
                            for(int i = 0; i < response.length();i++){
                                curObject = response.getJSONObject(i);
                                latLng = new LatLng(
                                        curObject.getDouble("Lat"),
                                        curObject.getDouble("Lon"));
                                Marker marker=getmMap().addMarker(new MarkerOptions().position(latLng)
                                        .title(curObject.getString("date"))
                                        .snippet(curObject.getString("Name")));
                                //marker.setTag(curObject.getString("idPhoto"));
                                marker.setTag(i);
                                if(i==response.length()-1){
                                    currentLatitude = Double.parseDouble(curObject.getString("Lat"));
                                    currentLongitude = Double.parseDouble(curObject.getString("Lon"));
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
    }*/

    public Positions getMyPosition(Positions inPositions)
    {
        JsonArrayRequest jsonArrayRequest;
        if(inPositions.getGroupName()==null) {
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                    , url + "getMyPosition/" + inPositions.getEmail(), null
                    , new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    List<Position> myPositions = new ArrayList<>();
                    try
                    {
                        JSONObject curObject;
                        LatLng latLng;
                        String date;
                        String name;
                        for(int i = 0; i < response.length();i++){
                            curObject = response.getJSONObject(i);
                            latLng = new LatLng(
                                    curObject.getDouble("Lat"),
                                    curObject.getDouble("Lon"));
                            date = curObject.getString("date");
                            name = curObject.getString("Name");
                            /*Marker marker=getmMap().addMarker(new MarkerOptions().position(latLng)
                                    .title(curObject.getString("date"))
                                    .snippet(curObject.getString("Name")));*/
                            Position position = new Position(latLng,date,name);
                            /*if(i==response.length()-1){
                                currentLatitude = Double.parseDouble(curObject.getString("Lat"));
                                currentLongitude = Double.parseDouble(curObject.getString("Lon"));
                            }*/
                            myPositions.add(position);
                        }
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                    inPositions.setMyPositions(myPositions);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
        }
        else{
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                    , url + "getGroupPosition/" + inPositions.getGroupName(), null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            List<Position> myPositions = new ArrayList<>();
                            try
                            {
                                JSONObject curObject;
                                LatLng latLng ;
                                String date;
                                String name;
                                for(int i = 0; i < response.length();i++){
                                    curObject = response.getJSONObject(i);
                                    latLng = new LatLng(
                                            curObject.getDouble("Lat"),
                                            curObject.getDouble("Lon"));
                                    date = curObject.getString("date");
                                    name = curObject.getString("Name");
                                    Position position = new Position(latLng,date,name);
                                    //Marker marker = getmMap().addMarker(new MarkerOptions().position(latLng)
                                    //        .title(curObject.getString("date"))
                                    //        .snippet("Recently posted by : "+curObject.getString("Name")));
                                    //marker.setTag(curObject.getString("idPositions"));
                                    //myLatitude.add(Double.parseDouble(curObject.getString("Lat")));
                                    //myLongitude.add(Double.parseDouble(curObject.getString("Lon")));
                                    //marker.setTag(i);
                                    myPositions.add(position);
                                }
                            }
                            catch( JSONException e )
                            {
                                Log.e( "Database", e.getMessage(), e );
                            }
                            inPositions.setMyPositions(myPositions);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
        requestQueue.add(jsonArrayRequest);
        return inPositions;
    }

    public void addPhotoInfo(PhotoActivityModel inmodel,View caller)
    {
        ProgressDialog progressDialog = new ProgressDialog(caller.getContext());
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url + "addPhotoInfo/" + inmodel.getDate() + "/" + inmodel.getUserid() + "/" + inmodel.getGroupId() + "/" +
                        inmodel.getLatitude() + "/" + inmodel.getLongitude()
                , null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                RequestQueue requestQueue = Volley.newRequestQueue(caller.getContext());
                addPhoto(inmodel.getBitmapBase(),caller,progressDialog,requestQueue);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void addPhoto(String imageString,View caller,ProgressDialog progressDialog
            ,RequestQueue requestQueue)
    {
        //Start an animating progress widget

        StringRequest submitRequest = new StringRequest (Request.Method.POST
                , url+"addPhoto"
                ,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off
                progressDialog.dismiss();
                Toast.makeText(caller.getContext(), "Post request executed", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(caller.getContext(), "Post request failed", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("photobase", imageString);
                return params;
            }
        };
        requestQueue.add(submitRequest);
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

    public Photos getPhoto(Photos photos)
    {
        JsonArrayRequest jsonArrayRequest;
        if(photos.getGroupId() == 0){
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url+"getMyPhoto/"+photos.getUserId()+"/"+photos.getLatitude()+"/"+photos.getLongitude()
                    , null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            List<Photo> myPhotos = new ArrayList<>();
                            try
                            {
                                JSONObject curObject;
                                String photoBase;
                                String name;
                                String date;
                                for(int i = 0; i < response.length();i++){
                                    curObject = response.getJSONObject(i);
                                    photoBase = curObject.getString("PhotoBase");
                                    name = curObject.getString("Name");
                                    date = curObject.getString("date");
                                    Photo photo = new Photo(name,date,photoBase);
                                    myPhotos.add(photo);
                                }
                            }
                            catch( JSONException e )
                            {
                                Log.e( "Database", e.getMessage(), e );
                            }

                            photos.setPhotos(myPhotos);
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
                    url+"getGroupPhotos/"+photos.getGroupId()+"/"+photos.getLatitude()+"/"+photos.getLongitude()
                    , null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            List<Photo> myPhotos = new ArrayList<>();
                            try
                            {
                                JSONObject curObject = new JSONObject();
                                String photoBase;
                                String name;
                                String date;
                                for(int i = 0; i < response.length();i++){
                                    curObject = response.getJSONObject(i);
                                    photoBase = curObject.getString("PhotoBase");
                                    name = curObject.getString("Name");
                                    date = curObject.getString("date");
                                    Photo photo = new Photo(name,date,photoBase);
                                    myPhotos.add(photo);
                                }
                                //activity.startActivity(intent);
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
        requestQueue.add(jsonArrayRequest);
        return photos;
    }
}
