package com.tripmaker.alberto.pathfinder;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripmaker.alberto.pathfinder.json_parser.JsonParser;

import org.json.JSONException;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class ResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<ArrayList<Integer>> segundos = new ArrayList<>();

    private ArrayList<String> identificadores = new ArrayList<>();

    private String TAG = ResultActivity.class.getSimpleName();
    private double lat_city;
    private double lon_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.i(TAG,"onCreate");
        lat_city = getIntent().getExtras().getDouble("LAT");
        lon_city = getIntent().getExtras().getDouble("LON");
        identificadores = getIntent().getExtras().getStringArrayList("IDS");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.resultMap);
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
        LatLng granada = new LatLng(lat_city, lon_city );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(granada,13));
    }
}
