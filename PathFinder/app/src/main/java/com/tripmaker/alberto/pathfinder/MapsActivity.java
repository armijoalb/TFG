package com.tripmaker.alberto.pathfinder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripmaker.alberto.pathfinder.fragment.TypesFragment;
import com.tripmaker.alberto.pathfinder.interfaces.CustomClickListener;
import com.tripmaker.alberto.pathfinder.json_parser.JsonParser;
import com.tripmaker.alberto.pathfinder.models.CityNode;
import com.tripmaker.alberto.pathfinder.models.TypeOfNode;
import org.json.JSONException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CustomClickListener {

    private GoogleMap mMap;
    private HashMap<String,Vector<String >> cityNames = new HashMap<>();
    private ArrayList<HashMap<String,String>> realData = new ArrayList<>();
    private final TypesFragment recyclerFragment = new TypesFragment();
    private final String TAG = MapsActivity.class.getSimpleName();
    private ArrayList<String> ids = new ArrayList<>();
    private String query_osmr;
    private String path_to_json_osmr;

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

    }

     // Función para inicializar el Fragment del RecyclerView.
    private void initFragment(){

        List<TypeOfNode> types = new ArrayList<>();
        for(Map.Entry<String,Vector<String>> it:cityNames.entrySet()){
            List<CityNode> nodes = new ArrayList<>();
            for(Iterator<String> it_v = it.getValue().iterator(); it_v.hasNext();){
                nodes.add(new CityNode(it_v.next()));
            }

            types.add(new TypeOfNode(it.getKey(),nodes));
        }

        recyclerFragment.setmList(types);
        recyclerFragment.setListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.recyclerLayout,recyclerFragment);
        transaction.commit();

        Log.i("fragment", "fragment creado");
    }

    @Override
    public void onSearchButtonClick(ArrayList<String> names) {
        if(names.size() > 0)
            Toast.makeText(this,"number_of_nodes:"+names.size(),Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"selecione solamente un hotel o hostal", Toast.LENGTH_LONG).show();

        ids = names;
        SendNodes sendNodes = new SendNodes(names,this);
        try {
            sendNodes.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] s = {query_osmr,"osmr_response.json"};
        Log.i(TAG,"hi");
        DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL(this);
        try {
            path_to_json_osmr = downloadFileFromURL.execute(s).get();
            Log.i(TAG,"path: "+ path_to_json_osmr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"saved file");


        Intent intent = new Intent(this,ResultActivity.class);
        this.startActivity(intent);
    }


    // Clase asíncrona para obtener distancias entre los nodos.
    private class SendNodes extends AsyncTask<Void,String,Void>{

        private ArrayList<String> selectedNodes = new ArrayList<>();
        private String osmr_query;
        private Context context;
        private final String TAG = SendNodes.class.getSimpleName();
        private String baseFolder = getApplicationContext().getFilesDir().getAbsolutePath();

        public SendNodes(ArrayList<String> nodes, Context mContext){
            selectedNodes = nodes;
            context = mContext;
            Log.i(TAG,"object created");
        }

        private ArrayList<String> getRealDataNames(){
            ArrayList<String> names = new ArrayList<>();
            for(Iterator<HashMap<String,String>> it = realData.iterator(); it.hasNext();){
                names.add(it.next().get("name"));
            }

            return names;
        }

        private ArrayList<SimpleEntry<String,String>> getLatLongs(){
            ArrayList<SimpleEntry<String,String>> lat_longs = new ArrayList<>();
            SimpleEntry<String, String> aux;
            ArrayList<String> names = getRealDataNames();
            for (Iterator<String> it = selectedNodes.iterator(); it.hasNext();){
                int index = names.indexOf(it.next());
                aux = new SimpleEntry<>(realData.get(index).get("lat"),realData.get(index).get("lon"));
                lat_longs.add(aux);
            }

            return lat_longs;
        }

        private String GenerateQueryString(){
            ArrayList<SimpleEntry<String,String>> mLatLongs = getLatLongs();
            StringBuilder stringBuilder = new StringBuilder("http://router.project-osrm.org/table/v1/foot/");
            for(Iterator<SimpleEntry<String,String>> it=mLatLongs.iterator(); it.hasNext();){
                SimpleEntry<String,String> aux = it.next();
                stringBuilder.append(aux.getKey()+","+aux.getValue());
                if(it.hasNext())
                    stringBuilder.append(";");
            }
            return stringBuilder.toString();
        }

        @Override
        protected Void doInBackground(Void... strings) {
            query_osmr = GenerateQueryString();
            Log.i(TAG,"All prepared");
            return null;
        }

        protected void onProgressUpdate(String... update){
        }

        @Override
        protected void onPostExecute(Void result){
            Log.i(TAG,"finished");
        }

        @Override
        protected void onPreExecute(){}
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
        private final String TAG = DownloadFileFromURL.class.getSimpleName();
        private boolean save_path;

        public DownloadFileFromURL(Context context) {
            this.mContext = context;
        }

        public DownloadFileFromURL(Context context, boolean spath){
            this.mContext = context;
            this.save_path = spath;
        }



        @Override
        protected void onPostExecute(String rt){
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            // Output stream
            String baseFolder = mContext.getFilesDir().getAbsolutePath();
            File file = new File(baseFolder + File.separator + f_url[1]);
            Log.i(TAG,"starting download");


            try {
                URL url = new URL(f_url[0]);
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                file.getParentFile().mkdirs();
                OutputStream output = new FileOutputStream(file);

                Log.i(TAG,"file_out: "+"File opened");
                Log.i(TAG,"file_out:"+ "File saved in: " + mContext.getFilesDir().getAbsolutePath());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                conection.disconnect();

                Log.i(TAG,"file_out:"+ "Finished writting output");

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return baseFolder + File.separator + f_url[1];
        }


    }

    /**
     * Función para procesar el archivo json.
     */
    private void processJSON() {
        String baseFolder = this.getFilesDir().getAbsolutePath();
        String file = new String(baseFolder + File.separator + "overpass_api.json");
        new jsonProcessor(this,"nodes").execute(file);
    }

    /**
     * Clase que procesa el archivo jSon.
     */
    class jsonProcessor extends AsyncTask<String,Void,Void>{

        private JsonParser mParser;
        private Context mContext;
        private String option;

        public jsonProcessor(Context theContext, String option){
            this.mContext = theContext;
            this.option = option;
        }

        @Override
        protected Void doInBackground(String... file_path) {

            mParser = new JsonParser(file_path[0]);
            switch (option){
                case "segs":
                    try{
                        mParser.processOSMRJSON();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "nodes":
                    try {
                        mParser.processJSON();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(option.equals("nodes"))
                drawNodes();

        }

        private void drawNodes(){
            Log.i("draw_nodes:", "drawing nodes");
            HashMap<String,Vector<HashMap<String, String>>> nodes = mParser.getAllInfo();

            double lat,lon;
            String node_name;
            HashMap<String,String> necesary_data = new HashMap<>();
            for(Map.Entry<String,Vector<HashMap<String,String>>> it:nodes.entrySet()){
                for (Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();) {
                    HashMap<String,String> m_hashmap = it_v.next();
                    lat = Double.parseDouble(m_hashmap.get("lat"));
                    lon = Double.parseDouble(m_hashmap.get("lon"));
                    node_name = m_hashmap.get("name");

                    // Añadimos Marker.
                    addMarker(lat, lon, node_name);

                    // Añadimos info a realData.
                    necesary_data.put("name",node_name);
                    necesary_data.put("lat",m_hashmap.get("lat"));
                    necesary_data.put("lon",m_hashmap.get("lon"));
                    realData.add(necesary_data);
                    necesary_data = new HashMap<>();

                    // Añadimos info a la lista que se mostrará.
                    if(cityNames.containsKey(it.getKey())){
                        cityNames.get(it.getKey()).add(node_name);
                    }else{
                        Vector<String> aux = new Vector<>();
                        aux.add(node_name);
                        cityNames.put(it.getKey(),aux);
                    }

                }
            }

            initFragment();


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

    // Función para añadir un marker al mapa.
    private void addMarker(double lat, double lon, String title){
        Log.i("addMaker: ", "adding new node");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .title(title));
    }


}
