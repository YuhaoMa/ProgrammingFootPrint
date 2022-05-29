package com.example.app_footprint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.os.Build;
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
import com.example.app_footprint.module.MapsActivityNotifier;
import com.example.app_footprint.module.Position;
import com.example.app_footprint.module.Positions;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener
        , MapsActivityNotifier {
    private RequestQueue requestQueue;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MINI_TIME = 1000;
    private final long MINI_DIST = 10;
    private Toolbar tToolbar;
    private Toolbar bToolbar;
    private int PICK_IMAGE_REQUEST = 111;
    private String userAddress;
    private String date;
    private String userid;
    private static Location uploadLocation;
    private TextView UserNametext;
    private Positions positionsModel;
    private Json baseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);
        baseConnection = new Json(requestQueue, this);
        baseConnection.setController(MapsActivity.this);
        Bundle extras = getIntent().getExtras();
        positionsModel = new Positions(extras.getString("address"), extras.getString("userId")
                , (Map<String, String>) extras.getSerializable("Positions"), extras.getString("username"));
        positionsModel.setMapsActivityNotifier(this);
        tToolbar = (Toolbar) findViewById(R.id.toolbar);
        UserNametext = findViewById(R.id.textView9);
        UserNametext.setText(positionsModel.getUserName());
        //update groupmap
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = new Intent(this, PhotoActivity.class);
        tToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.addP) {
                    if (positionsModel.getGroupName() == null) {
                        Toast.makeText(MapsActivity.this, "You are not in a group",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<Double> myLatitude = new ArrayList<>();
                        ArrayList<Double> myLongitude = new ArrayList<>();

                       //Lambda
                        positionsModel.getMyPositions().forEach((Position p)->{
                            myLatitude.add(p.getLatLng().latitude);
                            myLongitude.add(p.getLatLng().longitude);
                        });


                        intent.putExtra("address", userAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("latitude", uploadLocation.getLatitude());
                        intent.putExtra("longitude", uploadLocation.getLongitude());
                        intent.putExtra("userid", userid);
                        intent.putExtra("groupId", positionsModel.getGroupId());
                        intent.putExtra("myLatitude", myLatitude);
                        intent.putExtra("myLongitude", myLongitude);
                        startActivity(intent);
                    }

                } else {
                    mMap.clear();
                    positionsModel.setGroupName(item.toString());
                    baseConnection.getMyPosition(positionsModel);
                    Toast.makeText(MapsActivity.this, "To Group " + positionsModel.getGroupName()
                            , Toast.LENGTH_SHORT).show();
                }
                return true;
            }

        });
        bToolbar = (Toolbar) findViewById(R.id.toolbar3);
        userAddress = (String) extras.get("address");
        userid = (String) extras.get("userId");
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
        baseConnection.getMyPosition(positionsModel);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                uploadLocation = location;
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
        positionsModel.setGroupName(null);
        baseConnection.getMyPosition(positionsModel);
        Toast.makeText(this, "Return to My Homepage", Toast.LENGTH_SHORT).show();
    }

    public void searchGroup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText code = new EditText(this);
        builder.setTitle("Search Group");
        builder.setMessage("Please enter the code of the group you want to join");
        builder.setView(code);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                baseConnection.SearchGroup(code.getText().toString(), positionsModel, view);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                builder1.setTitle("Reminder");
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void createGroup(View view) {
        baseConnection.createNewGroup(positionsModel, view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        int i = 0;
        for (String name : positionsModel.getGroupMap().keySet()) {
            menu.add(1, 1, i, name);
            i++;
        }
        return true;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Intent intent = new Intent(this, ShowActivity.class);
        int groupid = 0;
        if (positionsModel.getGroupName() != null) {
            groupid = Integer.valueOf(positionsModel.getGroupId());
        }
        intent.putExtra("userid", Integer.valueOf(userid));
        intent.putExtra("groupid", groupid);
        intent.putExtra("latitude", marker.getPosition().latitude);
        intent.putExtra("longitude", marker.getPosition().longitude);
        startActivity(intent);
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setMarker(List<Position> positionList) {
        mMap.clear();
        positionList.forEach((Position position)->{
            mMap.addMarker(new MarkerOptions().position(position.getLatLng())
                    .title(position.getDate())
                    .snippet("Recently posted by : " + position.getName()));
        });

    }

    @Override
    public void refresh() {
        System.out.println("ddd");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (positionsModel.getGroupId() != null) {
            baseConnection.getMyPosition(positionsModel);
        }
    }
}