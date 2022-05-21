package com.example.app_footprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_footprint.Email.GenerateCode;
import com.example.app_footprint.Email.SendMailUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.app_footprint.databinding.ActivityMapsBinding;

import java.security.acl.Group;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private RequestQueue requestQueue;
    private static GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MINI_TIME = 1000;
    private final long MINI_DIST = 10;
    private Toolbar tToolbar;
    private Toolbar bToolbar;
    private LatLng latLng;
    //private ImageButton homeBtn;
    //private ImageButton searchBtn;
    //private  ImageButton newBtn;
    private ArrayList<String> groups;
    //private String address;
    private int PICK_IMAGE_REQUEST = 111;
    private String userAddress;
    //private Bitmap bitmap;
    private String date;
    private String id;
    public static String finalGroupName;
    public static Map<String,String> group = new HashMap<>();
    private static Location uploadLocation;
    public static String positionId;
    public static double currentLatitude;
    public static double currentLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(Json.showPhoto());
        Bundle extras = getIntent().getExtras();
        //System.out.println(extras.get("address")+"！！！！！！！！！！！！！！！address");
        tToolbar = (Toolbar) findViewById(R.id.toolbar);
        tToolbar.setTitle((String) extras.get("username"));
        setSupportActionBar(tToolbar);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Calendar cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());
        Intent intent = new Intent(this,PhotoActivity.class);
        tToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.addP){
                    intent.putExtra("address",userAddress);
                    intent.putExtra("date",date);
                    intent.putExtra("latitude",uploadLocation.getLatitude());
                    intent.putExtra("longitude",uploadLocation.getLongitude());
                    intent.putExtra("id",id);
                    intent.putExtra("groupId",group.get(finalGroupName));
                    intent.putExtra("positionId",positionId);
                    startActivity(intent);
                }
                else{
                    mMap.clear();
                    requestQueue.add(Json.getGroupPosition(item.toString()));
                    finalGroupName = item.toString();
                }
                return true;
            }

        });
        bToolbar = (Toolbar) findViewById(R.id.toolbar3);
        //homeBtn =(ImageButton) findViewById(R.id.HomeBtn);
        //searchBtn = (ImageButton) findViewById(R.id.SearchBtn);
        //newBtn = (ImageButton) findViewById(R.id.AddBtn);
        groups =(ArrayList<String>) extras.get("GroupInfo");
        userAddress = (String) extras.get("address");
        id = (String) extras.get("id");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PackageManager.PERMISSION_GRANTED);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerClickListener(this);
        requestQueue.add(Json.getMyPosition(userAddress));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                uploadLocation = location;
                if(Math.abs(currentLatitude - location.getLatitude()) > 0.05 &&
                Math.abs(currentLongitude - location.getLongitude()) > 0.05)
                {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    requestQueue.add(Json.newPosition(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()),date
                            ,"label",id));
                }
                else{}
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINI_TIME,
                    MINI_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        return false;
    }

    public void onBtnHome_Clicked(View caller) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void searchGroup(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        EditText code = new EditText(this);
        builder.setTitle("Search Group");
        builder.setMessage("Please enter the code of the group you want to join");
        builder.setView(code);
        Intent intent = new Intent(this,MapsActivity.class);
        //requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(Json.SearchGroup(code,builder,this,userAddress,tToolbar.getTitle().toString(),intent,id));

    }
    public void createGroup(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        EditText groupName = new EditText(this);
        builder.setTitle("Create a new Group");
        builder.setMessage("Enter the name of the group");
        builder.setView(groupName);
        Intent intent = new Intent(this,MapsActivity.class);
        //requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(Json.createNewGroup(groupName,userAddress,tToolbar.getTitle().toString(),
                this,intent,builder,id));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu,menu);
        for(int i =0 ; i<groups.size();i++)
        {
            menu.add(1,1,i,groups.get(i));
        }
        return super.onCreateOptionsMenu(menu);
    }



    public static GoogleMap getmMap() {
        return mMap;
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Intent intent = new Intent(this,ShowActivity.class);
        requestQueue.add(Json.getPhoto(marker.getSnippet(),group.get(finalGroupName)
                ,(String) marker.getTag(),intent,this));
        Toast.makeText(this,
                marker.getTitle() +
                        " has been clicked " + " times.",
                Toast.LENGTH_SHORT).show();
        return false;
    }

    public Location getUploadLocation() {
        return uploadLocation;
    }

    public void setUploadLocation(Location location) {
        uploadLocation = location;
    }
}