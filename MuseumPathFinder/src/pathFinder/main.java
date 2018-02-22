package pathFinder;

import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Vector;


public class main {
    public static void main(String[] args){
        // Identificadores.
        String [] idents = {"Inicio", "A", "B", "C", "D", "E"};
        Vector<String> ids = new Vector<String>(Arrays.asList(idents) ) ;

        // Horarios de apertura.
        Vector<Vector<AbstractMap.SimpleEntry<LocalTime,LocalTime>>> hor = new Vector<Vector<AbstractMap.SimpleEntry<LocalTime,LocalTime>>>();
        LocalTime aux1 = LocalTime.parse("09:00");
        LocalTime aux2 = LocalTime.parse("14:00");
        AbstractMap.SimpleEntry s = new AbstractMap.SimpleEntry(aux1,aux2);
        Vector<AbstractMap.SimpleEntry<LocalTime,LocalTime> > aux_v = new Vector<AbstractMap.SimpleEntry<LocalTime, LocalTime>>();
        aux_v.add(s);
        s = new AbstractMap.SimpleEntry(LocalTime.parse("15:30"), LocalTime.parse("20:00"));
        aux_v.add(s);
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<LocalTime, LocalTime>>();

        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("09:30"),LocalTime.parse("12:30")));
        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("15:30"),LocalTime.parse("20:00")));
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<LocalTime, LocalTime>>();

        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("09:00"),LocalTime.parse("13:00")));
        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("15:00"),LocalTime.parse("19:00")));
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<LocalTime, LocalTime>>();

        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("09:30"),LocalTime.parse("13:30")));
        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("16:00"),LocalTime.parse("20:00")));
        hor.add(aux_v);
        aux_v = new Vector<AbstractMap.SimpleEntry<LocalTime, LocalTime>>();

        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("09:30"),LocalTime.parse("13:30")));
        aux_v.add(new AbstractMap.SimpleEntry<LocalTime, LocalTime>(LocalTime.parse("16:00"),LocalTime.parse("20:00")));
        hor.add(aux_v);

        for(int i=0; i <hor.size(); i++ ){
            for(AbstractMap.SimpleEntry<LocalTime,LocalTime> local_t:aux_v){
                System.out.print(local_t.toString()+ '\t');
            }
            System.out.println();
        }

        // Tiempos.
        Vector<Vector<Long>> tim = new Vector<Vector<Long>>();
        Vector<Long> aux_t = new Vector<>();
        aux_t.add(Long.valueOf(0));
        aux_t.add(Long.valueOf(100));
        aux_t.add(Long.valueOf(550));
        aux_t.add(Long.valueOf(300));
        aux_t.add(Long.valueOf(500));
        aux_t.add(Long.valueOf(180));
        tim.add(aux_t);

        aux_t = new Vector<Long>();
        aux_t.add(Long.valueOf(100));
        aux_t.add(Long.valueOf(0));
        aux_t.add(Long.valueOf(500));
        aux_t.add(Long.valueOf(200));
        aux_t.add(Long.valueOf(350));
        aux_t.add(Long.valueOf(240));
        tim.add(aux_t);

        aux_t = new Vector<Long>();
        aux_t.add(Long.valueOf(550));
        aux_t.add(Long.valueOf(500));
        aux_t.add(Long.valueOf(0));
        aux_t.add(Long.valueOf(150));
        aux_t.add(Long.valueOf(250));
        aux_t.add(Long.valueOf(700));
        tim.add(aux_t);

        aux_t = new Vector<Long>();
        aux_t.add(Long.valueOf(300));
        aux_t.add(Long.valueOf(250));
        aux_t.add(Long.valueOf(150));
        aux_t.add(Long.valueOf(0));
        aux_t.add(Long.valueOf(180));
        aux_t.add(Long.valueOf(200));
        tim.add(aux_t);

        aux_t = new Vector<Long>();
        aux_t.add(Long.valueOf(500));
        aux_t.add(Long.valueOf(350));
        aux_t.add(Long.valueOf(250));
        aux_t.add(Long.valueOf(180));
        aux_t.add(Long.valueOf(0));
        aux_t.add(Long.valueOf(220));
        tim.add(aux_t);

        aux_t = new Vector<Long>();
        aux_t.add(Long.valueOf(180));
        aux_t.add(Long.valueOf(240));
        aux_t.add(Long.valueOf(700));
        aux_t.add(Long.valueOf(200));
        aux_t.add(Long.valueOf(220));
        aux_t.add(Long.valueOf(0));
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



        pathFinder.PathFinder myPathFinder = new pathFinder.PathFinder(dist,ids,hor,tim);

        Vector<String> solution = myPathFinder.obtainGreedySolution(LocalTime.parse("09:00"));

        for(String i:solution){
            System.out.print(i +' ');
        }
    }

}
