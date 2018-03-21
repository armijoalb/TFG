package com.tripmaker.alberto.pathfinder.pathFinder;

import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class PathFinder {
    private Vector<Vector<Float>> distancias;
    private Vector<String> identificadores;
    private Vector<Vector<SimpleEntry<GregorianCalendar,GregorianCalendar> > > horarios_abierto;
    private Vector<Vector<Integer>> duracion;

    // Contructor por defecto.
    public PathFinder(){
        this.distancias = new Vector<Vector<Float>>();
        this.identificadores = new Vector<String>();
        this.horarios_abierto = new Vector<Vector<SimpleEntry<GregorianCalendar,GregorianCalendar>>>();
        this.duracion = new Vector<>();
    }

    private String GregorianCalendarToString(GregorianCalendar mTime){
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        return fmt.format(mTime.getTime());
    }

    // Constructor con parámetros.
    public PathFinder( Vector<String> ids,
                       Vector< Vector<SimpleEntry<GregorianCalendar,GregorianCalendar>> > horario, Vector<Vector<Integer>> dur)
    {
        this.horarios_abierto = horario;
        this.identificadores = ids;
        this.duracion = dur;
    }

    // Función para comprobar que el tiempo es válida, es decir, que está dentro de alguno de los periodos de tiempo en los que
    // el museo está abierto.
    protected boolean checkTime(GregorianCalendar actualTime, Vector<SimpleEntry<GregorianCalendar,GregorianCalendar> > open_hour){
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
    protected  boolean checkLunchTime(GregorianCalendar actualTime,Vector<SimpleEntry<GregorianCalendar,GregorianCalendar>> open_hour){
        boolean is_lunch_time = false;

        if(open_hour.size() > 1){
            // Comprobamos si la hora actual está entre el horario de cierre de la mañana y el horario de apertura por la tarde.
            SimpleEntry<GregorianCalendar,GregorianCalendar> maniana = open_hour.elementAt(0);
            SimpleEntry<GregorianCalendar,GregorianCalendar> tarde = open_hour.elementAt(1);
            if( (actualTime.after(maniana.getValue()) || actualTime.equals(maniana.getValue()) ) && actualTime.before(tarde.getKey()) ) {

                is_lunch_time = true;
            }
        }

        return is_lunch_time;

    }

    // Función para calcular el punto más cercano al actual de un conjunto de no visitados.
    protected Integer findNearest(Integer id, Vector<Integer> non_valid){
        Integer id_nearest = non_valid.elementAt(0);
        Vector<Integer> v_nearest = duracion.elementAt(id);
        Integer nearest = v_nearest.elementAt(id_nearest);
        Integer aux;

        for(Iterator<Integer> it = non_valid.iterator(); it.hasNext();){
            aux = it.next();
            if(aux != id && duracion.elementAt(id).elementAt(aux) < nearest ){
                id_nearest = aux;
                nearest = duracion.elementAt(id).elementAt(aux);
            }
        }

        return id_nearest;
    }

    // Función para comprobar que el horario de salida aproximado calculado no sea mayor que el horario de cierre del museo.
    // Si es menor se devuelve el horario calculado, sino se devuelve la hora de cierre del museo.
    protected GregorianCalendar checkIfNotClosed(GregorianCalendar current_time,Vector<SimpleEntry<GregorianCalendar,GregorianCalendar>> horario){
        GregorianCalendar real_time = current_time;

        if(horario.size() > 1){
            if(horario.elementAt(0).getValue().before(current_time) && horario.elementAt(1).getKey().after(current_time))
                real_time = horario.elementAt(0).getValue();
            else if( horario.elementAt(1).getValue().before(current_time))
                real_time = horario.elementAt(1).getValue();
        }else{
            if(horario.elementAt(0).getValue().before(current_time))
                real_time = horario.elementAt(0).getValue();
        }

        return real_time;
    }

    // Algoritmo Greedy básico que calcula una solución seleccionando el museo más cercano a la posición actual.
    public HashMap<String,SimpleEntry<GregorianCalendar,GregorianCalendar>> obtainGreedySolution(GregorianCalendar starting_time){
        HashMap<String,SimpleEntry<GregorianCalendar,GregorianCalendar>> mSolution = new HashMap<>();
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

        // Terminaremos cuando ya hayamos llegado al tiempo límite.
        while( current_time.before(finish_time) && !non_added.isEmpty()){
            added = false;
            valid = non_added;

            while(!added && !valid.isEmpty()){
                // Calculamos el museo al cual tardamos menos en llegar desde donde estamos.
                Integer pos = findNearest(id,valid);
                aux_time = (GregorianCalendar)current_time.clone();
                aux_time.add(GregorianCalendar.SECOND,duracion.elementAt(id).elementAt(pos));
                // Comprobamos que podamos ir dentro del horario que esté abierto, o por la mañana o por la tarde.
                if( checkTime( aux_time, horarios_abierto.elementAt(pos-1)) ) {
                    current_time.add(GregorianCalendar.SECOND,duracion.elementAt(id).elementAt(pos));
                    aux_greg = (GregorianCalendar) current_time.clone();

                    visita = ThreadLocalRandom.current().nextInt(60,180+1);
                    current_time.add(GregorianCalendar.MINUTE,visita );

                    current_time = checkIfNotClosed(current_time,horarios_abierto.elementAt(pos-1));

                    id = pos;
                    solution.add(id);
                    added = true;
                    non_added.remove(id);
                    valid.remove(id);

                    mSolution.put(identificadores.elementAt(id),new SimpleEntry<>(aux_greg,current_time));



                } // Si no es válido, comprobamos si estamos dentro del horario incluido, si es así, cambiamos current_time y lo incluimos en la solución.
                else if(checkLunchTime(aux_time, horarios_abierto.elementAt(pos-1))){
                    if(horarios_abierto.elementAt(pos-1).size() > 1) {
                        current_time = horarios_abierto.elementAt(pos - 1).elementAt(1).getKey();
                    }else{ // Si solo está abierto pr la tarde.
                        current_time = horarios_abierto.elementAt(pos-1).elementAt(0).getKey();
                    }
                    aux_greg = (GregorianCalendar) current_time.clone();

                    visita = ThreadLocalRandom.current().nextInt(60,180+1);
                    current_time.add(GregorianCalendar.MINUTE,visita );

                    current_time = checkIfNotClosed(current_time,horarios_abierto.elementAt(pos-1));

                    id = pos;
                    solution.add(id);
                    added = true;
                    non_added.remove(id);
                    valid.remove(id);

                    mSolution.put(identificadores.elementAt(id),new SimpleEntry<>(aux_greg,current_time));

                } // Si no es posible ir en este momento, lo eliminamos de los casos válidos.
                else{
                    valid.remove(pos);
                }

            }

            if(valid.isEmpty() && !added){
                non_added.clear();
            }

        }


        return mSolution;
    }

}