package com.som_itsolutions.nirbhaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NirbhayaMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nirbhaya_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Intent tmp = getIntent();
        double lat = tmp.getDoubleExtra("lat", 0.0);
        double lon = tmp.getDoubleExtra("lon", 0.0);
        LatLng phoneLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(phoneLocation).title("Here i am..."));
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(phoneLocation,17));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(phoneLocation));


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(phoneLocation)     // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}