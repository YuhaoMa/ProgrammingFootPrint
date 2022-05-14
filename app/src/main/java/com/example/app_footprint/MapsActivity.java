package com.example.app_footprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.app_footprint.databinding.ActivityMapsBinding;

import java.security.acl.Group;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {
    private RequestQueue requestQueue;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MINI_TIME = 1000;
    private final long MINI_DIST = 10;
    private Toolbar tToolbar;
    private Toolbar bToolbar;
    private LatLng latLng;
    private ImageButton homeBtn;
    private ImageButton searchBtn;
    private  ImageButton newBtn;
    private ArrayList<String> groups;
    private  String userAddress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();
        tToolbar = (Toolbar) findViewById(R.id.toolbar);
        tToolbar.setTitle((String) extras.get("username"));
        setSupportActionBar(tToolbar);
        bToolbar = (Toolbar) findViewById(R.id.toolbar3);
        homeBtn =(ImageButton) findViewById(R.id.HomeBtn);
        searchBtn = (ImageButton) findViewById(R.id.SearchBtn);
        newBtn = (ImageButton) findViewById(R.id.AddBtn);
        groups =(ArrayList<String>) extras.get("GroupInfo");
        userAddress = (String)extras.get("address");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!+/n" +
                groups);
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("My Position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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

    public void showDialog(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        EditText code = new EditText(this);
        builder.setTitle("Search Group");
        builder.setMessage("Please enter the code of the group you want to join");
        builder.setView(code);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(Json.SearchGroup(code,builder,this,userAddress));

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

}