package com.tripmaker.alberto.pathfinder.pathFinder;

import android.util.Log;
import com.tripmaker.alberto.pathfinder.models.Solution;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;


public class PathFinder {
    private ArrayList<String> identificadores;
    private ArrayList<ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar> > > horarios_abierto;
    private ArrayList<ArrayList<Integer>> duracion;
    private String TAG = PathFinder.class.getSimpleName();

    // Contructor por defecto.
    public PathFinder(){
        this.identificadores = new ArrayList<>();
        this.horarios_abierto = new ArrayList<>();
        this.duracion = new ArrayList<>();
    }

    private String GregorianCalendarToString(GregorianCalendar mTime){
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        return fmt.format(mTime.getTime());
    }

    // Constructor con parámetros.
    public PathFinder( ArrayList<String> ids,
                       ArrayList< ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar>> > horario,
                       ArrayList<ArrayList<Integer>> dur)
    {
        Log.i(TAG,"creating objects");
        this.horarios_abierto = horario;
        this.identificadores = ids;
        this.duracion = dur;
    }

    // Función para comprobar que el tiempo es válida, es decir, que está dentro de alguno de los periodos de tiempo en los que
    // el museo está abierto.
    private boolean checkTime(GregorianCalendar actualTime, ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar> > open_hour){
        boolean is_posible = false;
        SimpleEntry<GregorianCalendar,GregorianCalendar> aux_h;



        for(Iterator<SimpleEntry<GregorianCalendar,GregorianCalendar>> it = open_hour.iterator();it.hasNext() && !is_posible;){
            aux_h = it.next();
            if( (aux_h.getKey().equals(actualTime) || actualTime.after(aux_h.getKey()) ) && actualTime.before(aux_h.getValue()) ){
                is_posible = true;
            }

        }

        return is_posible;
    }

    // Función para comprobar que no se está dentro del horario de cierre por horario de comida.
    private  boolean checkLunchTime(GregorianCalendar actualTime,ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar>> open_hour){
        boolean is_lunch_time = false;

        if(open_hour.size() > 1){
            // Comprobamos si la hora actual está entre el horario de cierre de la mañana y el horario de apertura por la tarde.
            SimpleEntry<GregorianCalendar,GregorianCalendar> maniana = open_hour.get(0);
            SimpleEntry<GregorianCalendar,GregorianCalendar> tarde = open_hour.get(1);
            if( (actualTime.after(maniana.getValue()) || actualTime.equals(maniana.getValue()) ) && actualTime.before(tarde.getKey()) ) {

                is_lunch_time = true;
            }
        }

        return is_lunch_time;

    }

    // Función para calcular el punto más cercano al actual de un conjunto de no visitados.
    private Integer findNearest(Integer id, Vector<Integer> non_valid){
        Integer id_nearest = non_valid.elementAt(0);
        ArrayList<Integer> v_nearest = duracion.get(id);
        Integer nearest = v_nearest.get(id_nearest);
        Integer aux;

        for(Iterator<Integer> it = non_valid.iterator(); it.hasNext();){
            aux = it.next();
            if(aux != id && duracion.get(id).get(aux) < nearest ){
                id_nearest = aux;
                nearest = duracion.get(id).get(aux);
            }
        }

        return id_nearest;
    }

    // Función para comprobar que el horario de salida aproximado calculado no sea mayor que el horario de cierre del museo.
    // Si es menor se devuelve el horario calculado, sino se devuelve la hora de cierre del museo.
    private GregorianCalendar checkIfNotClosed(GregorianCalendar current_time,ArrayList<SimpleEntry<GregorianCalendar,GregorianCalendar>> horario){
        GregorianCalendar real_time = current_time;

        if(horario.size() > 1){
            if(horario.get(0).getValue().before(current_time) && horario.get(1).getKey().after(current_time))
                real_time = horario.get(0).getValue();
            else if( horario.get(1).getValue().before(current_time))
                real_time = horario.get(1).getValue();
        }else{
            if(horario.get(0).getValue().before(current_time))
                real_time = horario.get(0).getValue();
        }

        return real_time;
    }



    // Algoritmo Greedy básico que calcula una solución seleccionando el museo más cercano a la posición actual.
    public Solution obtainGreedySolution(GregorianCalendar starting_time){
        Solution m_solution = new Solution();
        GregorianCalendar finish_time = new GregorianCalendar(1,1,1,20,0,0);
        GregorianCalendar current_time = new GregorianCalendar();
        current_time = starting_time;
        GregorianCalendar aux_time = new GregorianCalendar();
        aux_time = current_time;
        Vector<Integer> solution = new Vector<Integer>();
        Vector<String> ids_solution = new Vector<String>();
        Vector<Integer> non_added = new Vector<Integer>();
        for(int i=0; i < identificadores.size(); i++){
            non_added.add(i);
        }
        Vector<Integer> valid = new Vector<Integer>(non_added);
        boolean added = false;
        Integer id = 0;
        Integer visita = 0;
        GregorianCalendar aux_greg = new GregorianCalendar();

        // Metemos siempre el índice 0 en la solución, que será nuestro punto de partida siempre, este puede ser un múseo, u otra dirección.
        solution.add(id);
        non_added.remove(id);
        valid.remove(id);
        m_solution.add(identificadores.get(0),new SimpleEntry<String, String>(GregorianCalendarToString(starting_time),
                GregorianCalendarToString(starting_time)));


        // Terminaremos cuando ya hayamos llegado al tiempo límite.
        while( current_time.before(finish_time) && !non_added.isEmpty()){
            added = false;
            valid = non_added;

            while(!added && !valid.isEmpty()){
                // Calculamos el museo al cual tardamos menos en llegar desde donde estamos.
                Integer pos = findNearest(id,valid);
                aux_time = (GregorianCalendar)current_time.clone();
                aux_time.add(GregorianCalendar.SECOND,duracion.get(id).get(pos));
                // Comprobamos que podamos ir dentro del horario que esté abierto, o por la mañana o por la tarde.
                if( checkTime( aux_time, horarios_abierto.get(pos-1)) ) {
                    current_time.add(GregorianCalendar.SECOND,duracion.get(id).get(pos));
                    aux_greg = (GregorianCalendar) current_time.clone();

                    visita = ThreadLocalRandom.current().nextInt(60,180+1);
                    current_time.add(GregorianCalendar.MINUTE,visita );

                    current_time = checkIfNotClosed(current_time,horarios_abierto.get(pos-1));

                    id = pos;
                    solution.add(id);
                    added = true;
                    non_added.remove(id);
                    valid.remove(id);


                    m_solution.add(identificadores.get(id),new SimpleEntry<>(GregorianCalendarToString(aux_greg)
                            ,GregorianCalendarToString(current_time)));



                } // Si no es válido, comprobamos si estamos dentro del horario incluido, si es así, cambiamos current_time y lo incluimos en la solución.
                else if(checkLunchTime(aux_time, horarios_abierto.get(pos-1))){
                    if(horarios_abierto.get(pos-1).size() > 1) {
                        current_time = horarios_abierto.get(pos - 1).get(1).getKey();
                    }else{ // Si solo está abierto pr la tarde.
                        current_time = horarios_abierto.get(pos-1).get(0).getKey();
                    }
                    aux_greg = (GregorianCalendar) current_time.clone();

                    visita = ThreadLocalRandom.current().nextInt(60,180+1);
                    current_time.add(GregorianCalendar.MINUTE,visita );

                    current_time = checkIfNotClosed(current_time,horarios_abierto.get(pos-1));

                    id = pos;
                    solution.add(id);
                    added = true;
                    non_added.remove(id);
                    valid.remove(id);


                    m_solution.add(identificadores.get(id),new SimpleEntry<>(GregorianCalendarToString(aux_greg),
                            GregorianCalendarToString(current_time)) );

                } // Si no es posible ir en este momento, lo eliminamos de los casos válidos.
                else{
                    valid.remove(pos);
                }

            }

            if(valid.isEmpty() && !added){
                non_added.clear();
            }

        }


        return m_solution;
    }

}