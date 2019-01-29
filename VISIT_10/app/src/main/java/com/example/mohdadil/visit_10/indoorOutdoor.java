package com.example.mohdadil.visit_10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import es.situm.sdk.SitumSdk;
import es.situm.sdk.error.Error;
import es.situm.sdk.location.LocationListener;
import es.situm.sdk.location.LocationManager;
import es.situm.sdk.location.LocationRequest;
import es.situm.sdk.location.LocationStatus;
import es.situm.sdk.model.location.Location;

public class indoorOutdoor extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final String TAG = getClass().getSimpleName();
    private final int ACCESS_FINE_LOCATION_REQUEST_CODE = 3096;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Circle circle;
    private ProgressBar progressBar;
    int k=0;

    ArrayList<String> email = new ArrayList<String>() {//add new email ids here
        {
            add("adilkhot81@gmail.com");
            add("robinjk12345@gmail.com");
        }
    };
    ArrayList<String> keys = new ArrayList<String>() {//add new keys here....order should be same
        {
            add("6cb695a6abfd77b058eeaa98bc4e98aa7e1a27119c73336ea1dfae04f55c9c09");
            add("3679dd3670fb4954674183220cc5e32834fd379a5214080e63f156ff64153787");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_outdoor);
        //You must initialize the Situm SDK before using it
        SitumSdk.init(this);
        SitumSdk.configuration().setApiKey(email.get(k), keys.get(k));
        k++;


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setup();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        stopLocation();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        checkPermissions();
    }

    private void setup() {
        locationManager = SitumSdk.locationManager();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(indoorOutdoor.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(indoorOutdoor.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showPermissionsNeeded();
            } else {
                // No explanation needed, we can request the permission.
                requestPermission();
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            startLocation();
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(indoorOutdoor.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                ACCESS_FINE_LOCATION_REQUEST_CODE);
    }


    private void showPermissionsNeeded(){
        requestPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startLocation();
                } else {
                    showPermissionsNeeded();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void startLocation() {
        if (locationManager.isRunning()) {
            return;
        }
        locationListener = new LocationListener(){

            @Override
            public void onLocationChanged(@NonNull Location location) {
                progressBar.setVisibility(View.GONE);
                LatLng latLng = new LatLng(location.getCoordinate().getLatitude(),
                        location.getCoordinate().getLongitude());
                if (circle == null) {
                    circle = mMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(1d)
                            .strokeWidth(0f)
                            .fillColor(Color.BLUE));

                }else{
                    circle.setCenter(latLng);
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

            }

            @Override
            public void onStatusChanged(@NonNull LocationStatus locationStatus) {

            }

            @Override
            public void onError(@NonNull Error error) {
                Toast.makeText(indoorOutdoor.this, error.getMessage() , Toast.LENGTH_LONG).show();
            }
        };
        LocationRequest locationRequest = new LocationRequest.Builder()
                .useWifi(true)
                .useBle(true)
                .useForegroundService(true)
                .build();
        locationManager.requestLocationUpdates(locationRequest, locationListener);
    }


    private void stopLocation() {
        if (!locationManager.isRunning()) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }
}
