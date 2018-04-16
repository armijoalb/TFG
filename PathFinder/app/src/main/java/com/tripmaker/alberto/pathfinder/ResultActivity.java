package com.tripmaker.alberto.pathfinder;

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

public class ResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<ArrayList<Integer>> segundos = new ArrayList<>();
    private ArrayList<ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar>>> horario = new ArrayList<>();
    private ArrayList<String> identificadores = new ArrayList<>();
    private JsonParser mParser;
    private String TAG = ResultActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.i(TAG,"onCreate");

//        identificadores = getIntent().getExtras().getStringArrayList("IDS");
//        String path = getIntent().getExtras().getString("PATH");
//        mParser = new JsonParser(path);
//        try {
//            mParser.processOSMRJSON();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        segundos = mParser.getSegs();
//        generateDefaultHours();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.resultMap);
        mapFragment.getMapAsync(this);
    }

    private void generateDefaultHours(){
        ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar>> open_h = new ArrayList<>();
        GregorianCalendar aux_mañana, aux_tarde;
        for(Iterator<String> it = identificadores.iterator(); it.hasNext();){
            aux_mañana = new GregorianCalendar(1,1,1,9,0,0);
            aux_tarde = new GregorianCalendar(1,1,1,20,0,0);
            open_h.add(new SimpleEntry<GregorianCalendar, GregorianCalendar>(aux_mañana,aux_tarde) );
            horario.add(open_h);
            open_h = new ArrayList<>();
        }
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
