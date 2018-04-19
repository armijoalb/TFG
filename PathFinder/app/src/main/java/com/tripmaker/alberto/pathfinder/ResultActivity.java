package com.tripmaker.alberto.pathfinder;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripmaker.alberto.pathfinder.adapters.SolutionRecyclerAdapter;
import com.tripmaker.alberto.pathfinder.models.SolutionNode;

import java.util.ArrayList;

public class ResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<ArrayList<Integer>> segundos = new ArrayList<>();

    private ArrayList<String> identificadores = new ArrayList<>();

    private String TAG = ResultActivity.class.getSimpleName();
    private double lat_city;
    private double lon_city;
    private ArrayList<String> lat_ids = new ArrayList<>();
    private ArrayList<String> lon_ids = new ArrayList<>();
    private ArrayList<String> entradas = new ArrayList<>();
    private ArrayList<String> salidas = new ArrayList<>();
    private RecyclerView recyclerView;
    private SolutionRecyclerAdapter adapter;
    private ArrayList<SolutionNode> nodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.i(TAG,"onCreate");
        lat_city = getIntent().getExtras().getDouble("LAT");
        lon_city = getIntent().getExtras().getDouble("LON");
        identificadores = getIntent().getExtras().getStringArrayList("IDS");
        lat_ids = getIntent().getExtras().getStringArrayList("LATS");
        lon_ids = getIntent().getExtras().getStringArrayList("LONS");
        entradas = getIntent().getExtras().getStringArrayList("ENTRADA");
        salidas = getIntent().getExtras().getStringArrayList("SALIDA");

        for(int i=0; i < identificadores.size(); i++){
            nodes.add(new SolutionNode(identificadores.get(i),
                    entradas.get(i),
                    salidas.get(i)));
        }

        adapter = new SolutionRecyclerAdapter(nodes);

        recyclerView = findViewById(R.id.recyclerSolution);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

        for(int i=0; i < identificadores.size(); i++){
            addMarker(Double.parseDouble(lat_ids.get(i)),
                    Double.parseDouble(lon_ids.get(i)),
                    identificadores.get(i));
        }
    }

    // Función para añadir un marker al mapa.
    private void addMarker(double lat, double lon, String title){
        Log.i("addMaker: ", "adding new node");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .title(title));
    }
}
