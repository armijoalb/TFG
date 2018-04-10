package pathFinder;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.*;


public class main {
    public static void main(String[] args) throws IOException {
        // Identificadores.
        String [] idents = {"Inicio", "A", "B", "C", "D", "E"};
        Vector<String> ids = new Vector<String>(Arrays.asList(idents) ) ;

        // Horarios de apertura.
        Vector<Vector<AbstractMap.SimpleEntry<GregorianCalendar,GregorianCalendar>>> hor = new Vector<Vector<AbstractMap.SimpleEntry<GregorianCalendar,GregorianCalendar>>>();
        GregorianCalendar aux1 = new GregorianCalendar(1,1,1,9,0,0);
        GregorianCalendar aux2 = new GregorianCalendar(1,1,1,14,0,0);
        AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar> s = new AbstractMap.SimpleEntry<>(aux1,aux2);
        Vector<AbstractMap.SimpleEntry<GregorianCalendar,GregorianCalendar> > aux_v = new Vector<AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>>();
        aux_v.add(s);
        s = new AbstractMap.SimpleEntry<>(new GregorianCalendar(1,1,1,15,30,0),
                new GregorianCalendar(1,1,1,20,0,0));
        aux_v.add(s);
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>>();

        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,9,30,0),
                new GregorianCalendar(1,1,1,12,30,0) ) );
        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,15,30,0),
                new GregorianCalendar(1,1,1,20,0,0)) );
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>>();

        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,9,0,0),
                new GregorianCalendar(1,1,1,13,0,0)));
        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,15,0,0),
                new GregorianCalendar(1,1,1,19,0,0)));
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>>();

        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,9,30,0),
                new GregorianCalendar(1,1,1,13,30,0)));
        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,16,0,0),
                new GregorianCalendar(1,1,1,20,0,0)));
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>>();

        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>( new GregorianCalendar(1,1,1,9,30,0),
                new GregorianCalendar(1,1,1,13,30,0)));
        aux_v.add(new AbstractMap.SimpleEntry<GregorianCalendar, GregorianCalendar>(new GregorianCalendar(1,1,1,16,0,0),
                new GregorianCalendar(1,1,1,20,0,0)));
        hor.add(aux_v);

        for(int i=0; i <hor.size(); i++ ){
            for(AbstractMap.SimpleEntry<GregorianCalendar,GregorianCalendar> local_t:aux_v){
                System.out.print(local_t.toString()+ '\t');
            }
            System.out.println();
        }

        // Tiempos.
        Vector<Vector<Integer>> tim = new Vector<Vector<Integer>>();
        Vector<Integer> aux_t = new Vector<>();
        aux_t.add(0);
        aux_t.add(100);
        aux_t.add(550);
        aux_t.add(300);
        aux_t.add(500);
        aux_t.add(180);
        tim.add(aux_t);

        aux_t = new Vector<Integer>();
        aux_t.add(100);
        aux_t.add(0);
        aux_t.add(500);
        aux_t.add(200);
        aux_t.add(350);
        aux_t.add(240);
        tim.add(aux_t);

        aux_t = new Vector<Integer>();
        aux_t.add(550);
        aux_t.add(500);
        aux_t.add(0);
        aux_t.add(150);
        aux_t.add(250);
        aux_t.add(700);
        tim.add(aux_t);

        aux_t = new Vector<Integer>();
        aux_t.add(300);
        aux_t.add(250);
        aux_t.add(150);
        aux_t.add(0);
        aux_t.add(180);
        aux_t.add(200);
        tim.add(aux_t);

        aux_t = new Vector<Integer>();
        aux_t.add(500);
        aux_t.add(350);
        aux_t.add(250);
        aux_t.add(180);
        aux_t.add(0);
        aux_t.add(220);
        tim.add(aux_t);

        aux_t = new Vector<Integer>();
        aux_t.add(180);
        aux_t.add(240);
        aux_t.add(700);
        aux_t.add(200);
        aux_t.add(220);
        aux_t.add(0);
        tim.add(aux_t);


        System.out.println("size: "+tim.size()+"\t"+tim.elementAt(0).size());

        for(int i=0; i <tim.size(); i++){
            for(int j=0; j < tim.elementAt(i).size(); j++){
                System.out.print(tim.elementAt(i).elementAt(j).toString() + " ");
            }
            System.out.println();
        }

        // Distancias.
        Vector<Vector<Float>> dist = new Vector<Vector<Float>>();



        pathFinder.PathFinder myPathFinder = new pathFinder.PathFinder(ids,hor,tim);

        HashMap<String,AbstractMap.SimpleEntry<GregorianCalendar,GregorianCalendar> > theSolution = myPathFinder.obtainGreedySolution(new GregorianCalendar(1,1,1,9,0,0));

        for(Map.Entry<String,AbstractMap.SimpleEntry<GregorianCalendar,GregorianCalendar>>i: theSolution.entrySet()){
            System.out.print(i.getKey() +' ');
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar(1,1,1,9,30,0);
        GregorianCalendar gregorianCalendar1 = new GregorianCalendar(1,1,1,9,00,0);
        System.out.println(gregorianCalendar1.after(gregorianCalendar));
        System.out.println(gregorianCalendar1.before(gregorianCalendar));
        gregorianCalendar.add(GregorianCalendar.SECOND,3600);
        System.out.println(gregorianCalendar.get(GregorianCalendar.HOUR)+":"+gregorianCalendar.get(GregorianCalendar.MINUTE));
        gregorianCalendar = gregorianCalendar1;
        System.out.println(gregorianCalendar.get(GregorianCalendar.HOUR)+":"+gregorianCalendar.get(GregorianCalendar.MINUTE));



        jsonParser mParser = new jsonParser("/home/alberto/TFG/salida_overpass.json");
        mParser.processJSON();
        Vector<String> identificadores = mParser.getIds();
        System.out.println(identificadores.size());

        StringBuilder stringBuilder = new StringBuilder("http://router.project-osrm.org/table/v1/foot/");
        String osrm_query = new String();
        Vector<AbstractMap.SimpleEntry<String,String>> lat_longs = mParser.getLatLon();

        for(Iterator<AbstractMap.SimpleEntry<String,String>> it = lat_longs.iterator(); it.hasNext();){
            AbstractMap.SimpleEntry<String,String> lat_lon = it.next();
            stringBuilder.append(lat_lon.getKey()+","+lat_lon.getValue());
            if(it.hasNext())
                stringBuilder.append(";");
        }


        osrm_query = stringBuilder.toString();
        System.out.println(osrm_query);


    }

}
