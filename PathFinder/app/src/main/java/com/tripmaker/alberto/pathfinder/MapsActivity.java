package com.tripmaker.alberto.pathfinder;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView mRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFloatingButton;
    private ArrayList<String> cityNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        cityNames.add("Parque de las ciencias");
        cityNames.add("Mirador de San Nicolás");
        cityNames.add("Alhambra");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.e("map: ", "mapa puesto");

        // Creamos un recyclerView.
        mRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        mRecycler.setHasFixedSize(true);

        Log.e("recycler: ", "recyclerView created");
        // Establecemos un layout linear para el recyclerView.
        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);

        Log.e("recycler: ", "layoutManager set");

        // Establecemos un adapter. (Hay que crear una clase)
        mAdapter = new MyAdapter(cityNames);
        mRecycler.setAdapter(mAdapter);

        Log.e("recycler: ", "adapter set");

        // Establecemos el FloatingActionButton y creamos onClickListener.
        mFloatingButton = (FloatingActionButton) findViewById(R.id.sendButton);
        mFloatingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );

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
        LatLng granada = new LatLng(37.1886273, -3.5907775 );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(granada));
    }

    private class SendNodes extends AsyncTask<Void,Void,Void>{

        private ArrayList<String> selectedNodes = new ArrayList<>();
        private Context context;
        public SendNodes(ArrayList<String> nodes,Context mContext){
            selectedNodes = nodes;
            context = mContext;
        }

        @Override
        protected Void doInBackground(Void... strings) {
            Toast.makeText(context, selectedNodes.size(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
