package com.tripmaker.alberto.pathfinder.json_parser;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class JsonParser {
    private HashMap<String,Vector<HashMap<String,String>>> city_nodes = new HashMap<>();
    private String json_path_file;
    private JSONObject file_info = new JSONObject();
    private ArrayList<ArrayList<Integer>> segs = new ArrayList<>();
    private String TAG = JsonParser.class.getSimpleName();

    public JsonParser(){}


    public JsonParser(String file_path){
        this.json_path_file = file_path;
        segs = new ArrayList<>();

        try {
            // Abrimos el archivo y lo metemos en un String, después lo metemos en un JSONObject.
            InputStream in = new FileInputStream(json_path_file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();

            String jsonStr = new String(buffer,"UTF-8");
            file_info = new JSONObject(jsonStr);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void processJSON() throws JSONException{
        Integer visita = 0;
        JSONArray elements = file_info.getJSONArray("elements");
        for(int i=0; i < elements.length(); i++){
            JSONObject node = elements.getJSONObject(i);

            // Obtenemos datos del archivo.
            String id = node.optString("id");
            String latitud = node.optString("lat");
            String longitud = node.optString("lon");
            JSONObject tags = node.getJSONObject("tags");
            String name = tags.optString("name", "desconocido");

            String tipo = tags.optString("tourism");
            if( tipo.equals("") ){
                tipo = tags.optString("amenity");
            }


            HashMap<String, String> aux_hashMap = new HashMap<>();
            // Utilizamos un hashMap auxiliar.
            if( !name.equals("desconocido")) {
                aux_hashMap.put("id", id);
                aux_hashMap.put("lat", latitud);
                aux_hashMap.put("lon", longitud);
                aux_hashMap.put("name", name);

                switch (tipo){
                    case "viewpoint":
                        aux_hashMap.put("morning_schedule_open","00:00");
                        aux_hashMap.put("morning_schedule_close","00:00");
                        visita = ThreadLocalRandom.current().nextInt(15,31);
                        aux_hashMap.put("visit_time",visita.toString());
                        break;
                    case "place_of_worship":
                        aux_hashMap.put("morning_schedule_open", "9:30");
                        aux_hashMap.put("morning_schedule_close","14:00");
                        aux_hashMap.put("afternoon_schedule_open","15:30");
                        aux_hashMap.put("afternoon_schedule_close","20:00");
                        visita = ThreadLocalRandom.current().nextInt(60,180+1);
                        aux_hashMap.put("visit_time",visita.toString());
                        break;
                    case "museum":
                        aux_hashMap.put("morning_schedule_open", "9:30");
                        aux_hashMap.put("morning_schedule_close","14:00");
                        aux_hashMap.put("afternoon_schedule_open","15:30");
                        aux_hashMap.put("afternoon_schedule_close","20:00");
                        visita = ThreadLocalRandom.current().nextInt(60,180+1);
                        aux_hashMap.put("visit_time",visita.toString());
                        break;
                    case "hostel":
                        aux_hashMap.put("morning_schedule_open","1:00");
                        aux_hashMap.put("morning_schedule_close","00:00");
                        visita = 0;
                        aux_hashMap.put("visit_time",visita.toString());
                        break;
                    case "hotel":
                        aux_hashMap.put("morning_schedule_open","00:00");
                        aux_hashMap.put("morning_schedule_close","00:00");
                        visita = 0;
                        aux_hashMap.put("visit_time",visita.toString());
                        break;
                }

                if(!city_nodes.containsKey(tipo)){
                    // Metemos un nuevo nodo.
                    System.out.println("nuevo tipo: " + tipo);
                    Vector<HashMap<String,String>> v_aux = new Vector<>();
                    v_aux.add(aux_hashMap);
                    city_nodes.put(tipo, v_aux);

                }else{
                    System.out.println("adding new map to "+ tipo);
                    city_nodes.get(tipo).add(aux_hashMap);
                }

            }


        }

    }

    public void processOSMRJSON() throws JSONException{

        JSONArray times = file_info.getJSONArray("durations");
        ArrayList<Integer> tim = new ArrayList<>();
        for(int i=0; i < times.length(); i++){
            JSONArray aux_t = times.getJSONArray(i);
            for(int j=0; j < aux_t.length(); j++){
                int dist_time = aux_t.getInt(j);
                if(dist_time == 0 && i!=j)
                    dist_time = ThreadLocalRandom.current().nextInt(180,2000);

                tim.add(dist_time);
            }

            segs.add(tim);
            tim = new ArrayList<>();
        }

        Log.i(TAG,segs.toString());

    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public List<List<HashMap<String,String>>> parseRoutes(String geo_coord) throws JSONException{
        JSONObject object = new JSONObject(geo_coord);

        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {

            jRoutes = object.getJSONArray("routes");


            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();

                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude) );
                            hm.put("lng", Double.toString((list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }


        return routes;
    }

    public int getSizeMatrix(){
        return segs.size();
    }

    public ArrayList<ArrayList<Integer>> getSegs(){
        return segs;
    }

    public Vector<String> getIds(){
        Vector<String> ids = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            for(Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();){
                HashMap<String,String> m_hashmap = it_v.next();
                ids.add(m_hashmap.get("id"));
            }
        }

        return ids;
    }

    public Vector<String> getTipoIds(String tipo){
        Vector<String> ids = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            if(it.getKey().equals(tipo)) {
                for (Iterator<HashMap<String, String>> it_v = it.getValue().iterator(); it_v.hasNext(); ) {
                    HashMap<String, String> m_hashmap = it_v.next();
                    ids.add(m_hashmap.get("id"));
                }
            }
        }
        return ids;
    }

    public Vector<String> getNodeNames(){
        Vector<String> names = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            for(Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();){
                HashMap<String,String> m_hashmap = it_v.next();
                names.add(m_hashmap.get("name"));
            }
        }
        return names;
    }

    public Vector<String> getNodeNamesTipo(String tipo){
        Vector<String> names = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            if(it.getKey().equals(tipo)){
                for(Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();){
                    HashMap<String,String> m_hashmap = it_v.next();
                    names.add(m_hashmap.get("name"));
                }
            }

        }
        return names;
    }

    public Vector<AbstractMap.SimpleEntry<String,String>> getLatLon(){
        Vector<AbstractMap.SimpleEntry<String, String>> lat_lon = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            for(Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();){
                HashMap<String,String> m_hashmap = it_v.next();
                AbstractMap.SimpleEntry<String,String> se = new AbstractMap.SimpleEntry<>(m_hashmap.get("lat"),
                        m_hashmap.get("lon"));
                lat_lon.add(se);

            }

        }
        return lat_lon;
    }

    public Vector<AbstractMap.SimpleEntry<String,String>> getLatLonTipo(String tipo){
        Vector<AbstractMap.SimpleEntry<String, String>> lat_lon = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            if(it.getKey().equals(tipo)) {
                for (Iterator<HashMap<String, String>> it_v = it.getValue().iterator(); it_v.hasNext(); ) {
                    HashMap<String, String> m_hashmap = it_v.next();
                    AbstractMap.SimpleEntry<String, String> se = new AbstractMap.SimpleEntry<>(m_hashmap.get("lat"),
                            m_hashmap.get("lon"));
                    lat_lon.add(se);

                }
            }

        }
        return lat_lon;
    }
    String getFilePath(){
        return json_path_file;
    }

    public int getSize(){
        return city_nodes.size();
    }

    public HashMap<String, Vector<HashMap<String, String>>> getAllInfo() {
        return city_nodes;
    }
}
