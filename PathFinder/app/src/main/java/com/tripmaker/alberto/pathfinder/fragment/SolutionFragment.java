package com.tripmaker.alberto.pathfinder.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.adapters.SolutionRecyclerAdapter;
import com.tripmaker.alberto.pathfinder.json_parser.JsonParser;
import com.tripmaker.alberto.pathfinder.models.SolutionNode;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class SolutionFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;
    private ArrayList<String> entradas =  new ArrayList<>();
    private ArrayList<String> salidas = new ArrayList<>();
    private ArrayList<String> identificadores = new ArrayList<>();
    private ArrayList<String> lat_ids = new ArrayList<>();
    private ArrayList<String> lon_ids = new ArrayList<>();
    private RecyclerView recyclerView;
    private SolutionRecyclerAdapter adapter;
    private double lat_city;
    private double lon_city;
    private ArrayList<SolutionNode> nodes = new ArrayList<>();
    private String TAG = SolutionFragment.class.getSimpleName();
    private MapView mapView;

    public SolutionFragment() {
        // Required empty public constructor
    }

    public static SolutionFragment newInstance(ArrayList<String> entradas,ArrayList<String> salidas,
                                               ArrayList<String> ident, ArrayList<String> lats,
                                               ArrayList<String> lons, Double lat_city, Double lon_city) {
        SolutionFragment fragment = new SolutionFragment();
        Bundle args = new Bundle();
        args.putDouble("LAT",lat_city);
        args.putDouble("LON",lon_city);
        args.putStringArrayList("IDS",ident);
        args.putStringArrayList("ENTRADA",entradas);
        args.putStringArrayList("SALIDA",salidas);
        args.putStringArrayList("LATS",lats);
        args.putStringArrayList("LONS",lons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"oncreate");
        if (getArguments() != null) {
            lat_city = getArguments().getDouble("LAT");
            lon_city = getArguments().getDouble("LON");
            identificadores = getArguments().getStringArrayList("IDS");
            lat_ids = getArguments().getStringArrayList("LATS");
            lon_ids = getArguments().getStringArrayList("LONS");
            entradas = getArguments().getStringArrayList("ENTRADA");
            salidas = getArguments().getStringArrayList("SALIDA");
        }

        for(int i=0; i < identificadores.size(); i++){
            nodes.add(new SolutionNode(identificadores.get(i),
                    entradas.get(i),
                    salidas.get(i)));
        }

        adapter = new SolutionRecyclerAdapter(nodes);


    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_result,container,false);
        recyclerView = view.findViewById(R.id.recyclerSolution);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        mapView = (MapView) view.findViewById(R.id.resultMap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        FindRoutes findRoutes = new FindRoutes();
        findRoutes.execute();

        Log.i(TAG,"mapview initialize");
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        Log.i(TAG,"oncreateView");
        return view;
    }

    // Clase para generar la ruta de la solución.
    // Clase asíncrona para obtener la solucion.
    private class FindRoutes extends AsyncTask<Void,Void,Void> {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        private String TAG = FindRoutes.class.getSimpleName();
        private List<List<HashMap<String, String>>> route = new ArrayList<>();

        private void generateUrl(){
            url.append("origin="+lat_ids.get(0)+","+lon_ids.get(0)+"&");
            url.append("destination="+lat_ids.get(lat_ids.size()-1)+","+lon_ids.get(lon_ids.size()-1)+"&");
            url.append("mode=walking");

            if(lat_ids.size() > 2){
                url.append("&waypoints=");
                for(int i=1; i < lat_ids.size()-1;i++){
                    url.append(lat_ids.get(i)+","+lon_ids.get(i)+"|");
                }

                url.replace(url.lastIndexOf("|"),url.lastIndexOf("|")+1,"&key="+getString(R.string.google_maps_key));
            }
        }

        @Override
        protected void onPreExecute(){
            generateUrl();
        }

        @Override
        protected void onPostExecute(Void salida){
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < 1; i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = route.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.DKGRAY);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url_d = url.toString();
                Log.i(TAG,url_d);
                String json_info = downloadUrl(url_d);
                Log.i(TAG,json_info);
                JsonParser parser = new JsonParser();
                if( !json_info.equals("") ) {
                    route = parser.parseRoutes(json_info);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                Log.i("download","url created");
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("download","connection openned");

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();
                iStream.close();
                urlConnection.disconnect();

            } catch (Exception e) {
                Log.d("Exception downloading", e.toString());
            }
            return data;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng granada = new LatLng(lat_city, lon_city );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(granada,13));

        addMarker(Double.parseDouble(lat_ids.get(0)),
                Double.parseDouble(lon_ids.get(0)),
                        identificadores.get(0),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                "salida: "+salidas.get(0));

        for(int i=1; i < identificadores.size(); i++){
            addMarker(Double.parseDouble(lat_ids.get(i)),
                    Double.parseDouble(lon_ids.get(i)),
                    identificadores.get(i),
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                    "entrada: "+entradas.get(i)+"\n"+"salida: "+salidas.get(i)) ;
        }
    }

    // Función para añadir un marker al mapa.
    private void addMarker(double lat, double lon, String title,BitmapDescriptor color, String snippet ){

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .title(title)
                .icon(color)
                .snippet(snippet));
    }

}
