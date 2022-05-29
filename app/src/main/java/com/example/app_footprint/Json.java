package com.example.app_footprint;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.app_footprint.module.AbstractPositions;
import com.example.app_footprint.module.Photo;
import com.example.app_footprint.module.PhotoActivityModel;
import com.example.app_footprint.module.Photos;
import com.example.app_footprint.module.Position;
import com.example.app_footprint.module.Positions;
import com.example.app_footprint.module.UserModel;
import com.google.android.gms.maps.model.LatLng;

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
    private RequestQueue requestQueue;
    private Context controller;

    public Json(RequestQueue requestQueue,Context context){
        this.requestQueue = requestQueue;
        controller = context;
    }

    public void getUserInfo(UserModel inUser,TextView textView)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url + "getUserInfo/" + inUser.getEmail(), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (!response.getJSONObject(0).getString("Password").equals(inUser.getPassword())) {
                                textView.setText("Incorrect Password");
                            }
                            else{
                                inUser.setUserId(response.getJSONObject(0).getString("idUsers"));
                                inUser.setUserName(response.getJSONObject(0).getString("Name"));
                                inUser.jumpToMap();

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
        requestQueue.add(jsonArrayRequest);
    }

    public void forgetMyPassword(UserModel inUser)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "getUserInfo/" + inUser.getEmail(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            if(inUser.getEmail().equals(response.getJSONObject(0).getString("emailaddress"))){
                                inUser.setSendCode();
                                inUser.setUserId(response.getJSONObject(0).getString("idUsers"));
                                inUser.setUserName(response.getJSONObject(0).getString("Name"));
                            }
                        }
                        catch (JSONException e){
                            inUser.notifyErrorView();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    public void LoginSuccessfully(AbstractPositions p)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+ "getGroup/"+p.getEmail(), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            p.setGroupMap(setMap(response));
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
        requestQueue.add(jsonArrayRequest);

    }

    public void SearchGroup(String code,Positions inModel,View caller)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+ "searchGroupCode/"+code, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(caller.getContext());
                        builder1.setTitle("Reminder");
                        try
                        {
                            if(response.getJSONObject(0).getString("code").equals(code))
                            {
                                builder1.setMessage("You've successfully joined a new group");
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        insertNewGroup(inModel,caller,code);
                                    }
                                });
                            }
                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                            builder1.setMessage("Error code, the group does not exist");
                            builder1.setPositiveButton("Cancel",null);
                        }
                        AlertDialog dialog1=builder1.create();
                        dialog1.show();
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
        requestQueue.add(jsonArrayRequest);

    }

    public void insertNewGroup(Positions inModel,View caller,String code)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+"addGroup/"+inModel.getEmail()+"/"+code, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        LoginSuccessfully(inModel);
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
        requestQueue.add(jsonArrayRequest);
    }

    public void newUser(String textPassword,String textEmail,String textName
            ,View caller)
    {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET,
                url + "newUser/" + textPassword + "/" + textEmail + "/" + textName,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(caller.getContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(caller.getContext(),"Register Failed",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(submitRequest);
    }

    public void createNewGroup(Positions inModel, View caller)
    {
        GenerateCode generateCode = new GenerateCode(3);
        String Sendcode = generateCode.generateCode();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url+"createGroup/"+Sendcode+"/"
                , null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                        AlertDialog.Builder builder=new AlertDialog.Builder(caller.getContext());
                        EditText groupName = new EditText(caller.getContext());
                        builder.setTitle("Create a new Group");
                        builder.setMessage("Enter the name of the group");
                        builder.setView(groupName);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder1=new AlertDialog.Builder(caller.getContext());
                                builder1.setTitle("Reminder");
                                if(!groupName.getText().toString().equals(""))
                                {
                                    builder1.setMessage("The group \""+groupName.getText().toString()
                                            +"\" was created successfully!\n" + "Invitation code :"+Sendcode);
                                    SendMailUtil.send(inModel.getEmail(),Sendcode,2,groupName.getText().toString());
                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setGroupName(Sendcode,groupName.getText().toString(),inModel,caller);
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
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteGroup();
                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();
                    }},
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        Toast.makeText(caller.getContext(),"Create Group Failed.",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void setGroupName(String code,String GroupName,Positions inModel,View caller)
    {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET,
                url + "setGroupName/" + GroupName + "/" + code, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        insertNewGroup(inModel,caller,code);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(submitRequest);
    }

    public void getMyPosition(Positions inPositions)
    {
        JsonArrayRequest jsonArrayRequest;
        if(inPositions.getGroupName()==null) {
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                    , url + "getMyPosition/" + inPositions.getEmail(), null
                    , new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try
                    {
                        inPositions.setMyPositions(setPosition(response));
                        System.out.println("MyPositions!!!!!!!!!!!!!+"+inPositions.getMyPositions());
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
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
                            try
                            {
                                inPositions.setMyPositions(setPosition(response));
                            }
                            catch( JSONException e )
                            {
                                Log.e( "Database", e.getMessage(), e );
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
        requestQueue.add(jsonArrayRequest);
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
                addPhoto(inmodel.getBitmapBase(),progressDialog,requestQueue,inmodel);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void addPhoto(String imageString,ProgressDialog progressDialog
            ,RequestQueue requestQueue,PhotoActivityModel inmodel)
    {
        //Start an animating progress widget
        StringRequest submitRequest = new StringRequest (Request.Method.POST
                , url+"addPhoto"
                ,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off
                progressDialog.dismiss();
                Toast.makeText(controller, "Post request executed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(controller,ShowActivity.class);
                intent.putExtra("userid",Integer.valueOf(inmodel.getUserid()));
                intent.putExtra("groupid",Integer.valueOf(inmodel.getGroupId()));
                intent.putExtra("latitude",inmodel.getLatitude());
                intent.putExtra("longitude",inmodel.getLongitude());
                controller.startActivity(intent);

            }
        },
                error -> {
            Toast.makeText(controller, "Post request failed", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
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

    public void changePassword(UserModel inUser)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"changePassword/"+inUser.getPassword()+"/"+inUser.getEmail(), null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        inUser.notifyChangePassword();
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

        requestQueue.add(jsonArrayRequest);
    }

    public void getPhoto(Photos photos)
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
                            try
                            {
                                photos.setPhotos(setPhoto(response));
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
                    url+"getGroupPhotos/"+photos.getGroupId()+"/"+photos.getLatitude()+"/"+photos.getLongitude()
                    , null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            try
                            {
                                photos.setPhotos(setPhoto(response));
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
    }

    private void deleteGroup()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url+"deletGroup", null, null,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void setController(Context context){this.controller = context;}
    private  Map<String, String> setMap(JSONArray jsonArray) throws JSONException {
        String groupName;
        String groupid;
        Map<String, String>  tempGroupMap = new HashMap<>();
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject curObject = jsonArray.getJSONObject( i );
            groupid = curObject.getString("idGroup");
            groupName = curObject.getString("GroupName");
            tempGroupMap.put(groupName,groupid);
        }
        return tempGroupMap;
    }
    private  List<Position>      setPosition(JSONArray jsonArray) throws JSONException {
        List<Position> myPositions = new ArrayList<>();
            JSONObject curObject;
            LatLng latLng ;
            String date;
            String name;
            for(int i = 0; i < jsonArray.length();i++){
                curObject = jsonArray.getJSONObject(i);
                latLng = new LatLng(
                        curObject.getDouble("Lat"),
                        curObject.getDouble("Lon"));
                date = curObject.getString("date");
                name = curObject.getString("Name");
                Position position = new Position(latLng,date,name);
                myPositions.add(position);
            }
        return myPositions;
    }
    private  List<Photo> setPhoto(JSONArray jsonArray) throws JSONException {
        List<Photo> myPhotos = new ArrayList<>();
        JSONObject curObject = new JSONObject();
        String photoBase;
        String name;
        String date;
        for(int i = 0; i < jsonArray.length();i++){
            curObject = jsonArray.getJSONObject(i);
            photoBase = curObject.getString("PhotoBase");
            name = curObject.getString("Name");
            date = curObject.getString("date");
            Photo photo = new Photo(name,date,photoBase);
            myPhotos.add(photo);
        }
        return myPhotos;
    }

}
