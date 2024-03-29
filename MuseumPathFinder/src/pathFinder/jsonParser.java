package pathFinder;

import org.json.*;

import javax.print.DocFlavor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class jsonParser {
    private HashMap<String,Vector<HashMap<String,String>>> city_nodes = new HashMap<>();
    private String json_path_file;
    private JSONObject file_info = new JSONObject();
    private ArrayList<ArrayList<Integer>> segs = new ArrayList<>();

    public jsonParser(String file_path){
        this.json_path_file = file_path;

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

    protected void processOSMRJSON(){

        JSONArray times = file_info.getJSONArray("durations");
        ArrayList<Integer> tim = new ArrayList<>();
        System.out.println("Tamaño de durations: " + times.length());
        for(int i=0; i < times.length(); i++){
            JSONArray aux_t = times.getJSONArray(i);
            System.out.println("Tamaño vector " +i +": " + aux_t.length());
            for(int j=0; j < aux_t.length(); j++){
                int dist_time = aux_t.getInt(j);
                System.out.println("Elemento "+i+" "+j+" : "+dist_time);
                tim.add(dist_time);
            }

            segs.add(tim);
            tim = new ArrayList<>();
        }

    }

    protected int getSizeMatrix(){
        return segs.size();
    }

    protected ArrayList<ArrayList<Integer>> getSegs(){
        return segs;
    }

    protected Vector<String> getIds(){
        Vector<String> ids = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            for(Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();){
                HashMap<String,String> m_hashmap = it_v.next();
                ids.add(m_hashmap.get("id"));
            }
        }

        return ids;
    }

    protected Vector<String> getTipoIds(String tipo){
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

    protected Vector<String> getNodeNames(){
        Vector<String> names = new Vector<>();
        for(Map.Entry<String,Vector<HashMap<String,String>>> it:city_nodes.entrySet()){
            for(Iterator<HashMap<String,String>> it_v = it.getValue().iterator(); it_v.hasNext();){
                HashMap<String,String> m_hashmap = it_v.next();
                names.add(m_hashmap.get("name"));
            }
        }
        return names;
    }

    protected Vector<String> getNodeNamesTipo(String tipo){
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

    protected Vector<AbstractMap.SimpleEntry<String,String>> getLatLon(){
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

    protected Vector<AbstractMap.SimpleEntry<String,String>> getLatLonTipo(String tipo){
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

    protected int getSize(){
        return city_nodes.size();
    }


}
