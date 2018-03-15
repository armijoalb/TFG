package pathFinder;

import org.json.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class jsonParser {
    private HashMap<String,HashMap<String,String>> city_nodes = new HashMap<>();
    private String json_path_file;
    private JSONObject file_info = new JSONObject();
    private Vector<Vector<Long>> segs;

    public jsonParser(String file_path){
        this.json_path_file = file_path;
        segs = new Vector<>();

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

    protected void processJSON(){
        JSONArray elements = file_info.getJSONArray("elements");
        for(int i=0; i < elements.length(); i++){
            JSONObject node = elements.getJSONObject(i);

            // Obtenemos datos del archivo.
            String id = node.optString("id");
            String latitud = node.optString("lat");
            String longitud = node.optString("lon");
            JSONObject tags = node.getJSONObject("tags");
            String name = tags.optString("name", "desconocido");

            // Utilizamos un hashMap auxiliar.
            HashMap<String, String> aux_hashMap = new HashMap<>();
            aux_hashMap.put("lat", latitud);
            aux_hashMap.put("lon", longitud);
            aux_hashMap.put("name", name);

            // Metemos un nuevo nodo.
            city_nodes.put(id,aux_hashMap);
        }

    }

    protected void processGrahhopperJSON(){

        JSONArray times = file_info.getJSONArray("times");
        Vector<Long> tim = new Vector<>();
        for(int i=0; i < times.length(); i++){
            JSONArray aux_t = times.getJSONArray(i);
            for(int j=0; j < aux_t.length(); j++){
                Long dist_time = aux_t.getLong(i);
                tim.add(dist_time);
            }

            segs.add(tim);
            tim = new Vector<Long>();
        }

    }

    protected int getSizeMatrix(){
        return segs.size();
    }

    protected Vector<Vector<Long>> getSegs(){
        return segs;
    }

    protected Vector<String> getIds(){
        Vector<String> ids = new Vector<>();

        city_nodes.forEach((k,v)-> ids.add(k));

        return ids;
    }

    protected Vector<String> getNodeNames(){
        Vector<String> names = new Vector<>();
        city_nodes.forEach((k,v) -> names.add(v.get("name")));
        return names;
    }

    protected Vector<AbstractMap.SimpleEntry<String,String>> getLatLon(){
        Vector<AbstractMap.SimpleEntry<String, String>> lat_lon = new Vector<>();
        for(Map.Entry<String,HashMap<String,String>> it:city_nodes.entrySet()){
            AbstractMap.SimpleEntry<String,String> se = new AbstractMap.SimpleEntry<>(it.getValue().get("lat"),
                    it.getValue().get("lon"));
            lat_lon.add(se);
        }
        return lat_lon;
    }

    String getFilePath(){
        return json_path_file;
    }

    protected int getSize(){
        return city_nodes.size();
    }


}
