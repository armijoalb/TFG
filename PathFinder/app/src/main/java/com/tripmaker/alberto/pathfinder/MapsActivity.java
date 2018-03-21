package com.tripmaker.alberto.pathfinder;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripmaker.alberto.pathfinder.custom_adapter.MyAdapter;
import com.tripmaker.alberto.pathfinder.fragments.RecyclerFragment;
import com.tripmaker.alberto.pathfinder.json_parser.jsonParser;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView mRecycler;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFloatingButton;
    private ArrayList<String> cityNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        openUrl();
        processJSON();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.e("map: ", "mapa puesto");

        // Metemos el fragment con el recyclerView
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putStringArrayList("nodes", cityNames);
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        recyclerFragment.setArguments(fragmentBundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.recyclerLayout,recyclerFragment);
        transaction.commit();

        Log.i("fragment", "fragment creado");

     }

    // OnClickListener para el  FAB
    class sendButtonClick implements View.OnClickListener{
        Context mContext;
        ArrayList<String> selected = new ArrayList<>();

        public sendButtonClick(Context context, ArrayList<String> s){
            mContext = context;
            selected = s;
        }

        @Override
        public void onClick(View view) {
            SendNodes mSender = new SendNodes(selected,mContext);
            Log.i("size_selected: ", ""+selected.size());
            mSender.execute();
        }
    }

    // Clase asíncrona para obtener distancias entre los nodos.
    private class SendNodes extends AsyncTask<Void,Void,Void>{

        private ArrayList<String> selectedNodes = new ArrayList<>();
        private Context context;
        public SendNodes(ArrayList<String> nodes,Context mContext){
            selectedNodes = nodes;
            context = mContext;
        }

        @Override
        protected Void doInBackground(Void... strings) {

            return null;
        }
    }

    /**
     * Método que se utiliza para descargar el archivo de datos de overpass. Junto con el
     * script.
     */
    public void openUrl() {

        // Código de la url que "abrimos" para obtener el archivoq ue tiene los nodos.
        String url_enconded = "https://overpass-api.de/api/interpreter?data=[out:json][timeout:100];" +
                "(node[\"place\"=\"city\"][\"name\"=\"Granada\"][\"is_in:province\"=\"Granada\"][\"is_in:country\"=\"Spain\"];)->.ciudad;" +
                "node[\"tourism\"=\"museum\"](around.ciudad:7000);out;>;out;" +
                "node[\"tourism\"=\"viewpoint\"](around.ciudad:7000);out;" +
                "node[\"tourism\"=\"hotel\"](around.ciudad:7000);out;node[\"tourism\"=\"hostel\"](around.ciudad:7000);out;" +
                "node[\"building\"=\"cathedral\"](around.ciudad:7000);out;";


        //Toast.makeText(this, "Iniciando la descarga", Toast.LENGTH_SHORT).show();
        // Utilizamos la nueva clase para descargar el contenido del fichero.
        DownloadFileFromURL mDownloader = new DownloadFileFromURL(this);
        String[] s = {Uri.parse(url_enconded).toString(), "overpass_api.json"};
        mDownloader.execute(s);


    }

    /**
     * Clase para descargar un archivo especificando el nombre del archivo.
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context mContext;

        public DownloadFileFromURL(Context context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            // Output stream
            String baseFolder = mContext.getFilesDir().getAbsolutePath();
            File file = new File(baseFolder + File.separator + f_url[1]);

            if(file.exists()){
                Log.i("downloader:", "file exists, no need to download");
                return null;
            }

            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);



                file.getParentFile().mkdirs();
                OutputStream output = new FileOutputStream(file);

                Log.e("file_out: ", "File opened");
                Log.e("file_out:", "File saved in: " + mContext.getFilesDir().getAbsolutePath());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                Log.e("file_out:", "Finished writting output");

//                FileInputStream inputStream = openFileInput(f_url[1]);
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                int num_lines = 0;
//
//                while(bufferedReader.readLine() != null){
//                    num_lines ++;
//                }
//
//                Log.e("file readed: ", "num_lines " + num_lines);


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


    }

    /**
     * Función para procesar el archivo json.
     */
    private void processJSON() {
        String baseFolder = this.getFilesDir().getAbsolutePath();
        String file = new String(baseFolder + File.separator + "overpass_api.json");
        new jsonProcessor(this).execute(file);
    }

    /**
     * Clase que procesa el archivo jSon.
     */
    class jsonProcessor extends AsyncTask<String,Void,Void>{

        jsonParser mParser;
        private Context mContext;

        public jsonProcessor(Context theContext){
            this.mContext = theContext;
        }

        @Override
        protected Void doInBackground(String... file_path) {

            mParser = new jsonParser(file_path[0]);
            try {
                mParser.processJSON();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //Toast.makeText(mContext,"El número de nodos es: " + mParser.getSize(),Toast.LENGTH_SHORT ).show();
            drawNodes();
        }

        private void drawNodes(){
            Log.i("draw_nodes:", "drawing nodes");
            HashMap<String,HashMap<String, String>> nodes = mParser.getAllInfo();

            double lat,lon;
            String node_name;
            for(Map.Entry<String,HashMap<String,String>> it:nodes.entrySet()){
                lat = Double.parseDouble( it.getValue().get("lat") );
                lon = Double.parseDouble( it.getValue().get("lon") );
                node_name = it.getValue().get("name");

                addMarker(lat, lon,node_name);
                cityNames.add(node_name);
            }


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
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("map_style", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("map_style", "Can't find style. Error: ", e);
        }



        // Add a marker in Sydney and move the camera
        LatLng granada = new LatLng(37.1886273, -3.5907775 );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(granada,13));


    }

    private void addMarker(double lat, double lon, String title){
        Log.i("addMaker: ", "adding new node");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .title(title));
    }


}
