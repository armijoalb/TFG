package pathFinder;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class PathFinder {
    private Vector<Vector<Float>> distancias;
    private Vector<String> identificadores;
    private Vector<Vector<SimpleEntry<LocalTime,LocalTime> > > horarios_abierto;
    private Vector<Vector<Long>> duracion;

    // Contructor por defecto.
    public PathFinder(){
        this.distancias = new Vector<Vector<Float>>();
        this.identificadores = new Vector<String>();
        this.horarios_abierto = new Vector<Vector<SimpleEntry<LocalTime,LocalTime>>>();
        this.duracion = new Vector<Vector<Long>>();
    }

    // Constructor con parámetros.
    public PathFinder(Vector<Vector<Float>> distances, Vector<String> ids,
                      Vector< Vector<SimpleEntry<LocalTime,LocalTime>> > horario, Vector<Vector<Long>> dur)
    {
        this.distancias = distances;
        this.horarios_abierto = horario;
        this.identificadores = ids;
        this.duracion = dur;
    }

    // Función para comprobar que el tiempo es válida, es decir, que está dentro de alguno de los periodos de tiempo en los que
    // el museo está abierto.
    protected boolean checkTime(LocalTime actualTime, Vector<SimpleEntry<LocalTime,LocalTime> > open_hour){
        boolean is_posible = false;
        SimpleEntry<LocalTime,LocalTime> aux_h;

        for(Iterator<SimpleEntry<LocalTime,LocalTime>> it = open_hour.iterator();it.hasNext() && !is_posible;){
            aux_h = it.next();
            if( (aux_h.getKey().equals(actualTime) || actualTime.isAfter(aux_h.getKey()) ) && actualTime.isBefore(aux_h.getValue()) ){
                is_posible = true;
            }

        }

        return is_posible;
    }

    // Función para comprobar que no se está dentro del horario de cierre por horario de comida.
    protected  boolean checkLunchTime(LocalTime actualTime,Vector<SimpleEntry<LocalTime,LocalTime>> open_hour){
        boolean is_lunch_time = false;

        if(open_hour.size() > 1){
            // Comprobamos si la hora actual está entre el horario de cierre de la mañana y el horario de apertura por la tarde.
            SimpleEntry<LocalTime,LocalTime> maniana = open_hour.elementAt(0);
            SimpleEntry<LocalTime,LocalTime> tarde = open_hour.elementAt(1);
            if( (actualTime.isAfter(maniana.getValue()) || actualTime.equals(maniana.getValue()) ) && actualTime.isBefore(tarde.getKey()) )
                is_lunch_time = true;
        }

        return is_lunch_time;

    }

    // Función para calcular el punto más cercano al actual de un conjunto de no visitados.
    protected Integer findNearest(Integer id, Vector<Integer> non_valid){
        Integer id_nearest = non_valid.elementAt(0);
        Vector<Long> v_nearest = duracion.elementAt(id);
        Long nearest = v_nearest.elementAt(id_nearest);
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
    protected LocalTime checkIfNotClosed(LocalTime current_time,Vector<SimpleEntry<LocalTime,LocalTime>> horario){
        LocalTime real_time = current_time;

        for(SimpleEntry<LocalTime,LocalTime> time_h : horario){
            if(current_time.isAfter(time_h.getValue())){
                real_time = time_h.getValue();
                break;
            }
        }

        return real_time;
    }

    // Algoritmo Greedy básico que calcula una solución seleccionando el museo más cercano a la posición actual.
    public Vector<String> obtainGreedySolution(LocalTime starting_time){
        //
        LocalTime finish_time = LocalTime.of(20,00);
        LocalTime current_time = starting_time;
        Vector<Integer> solution = new Vector<Integer>();
        Vector<String> ids_solution = new Vector<String>();
        Vector<Integer> non_added = new Vector<Integer>();
        for(int i=0; i < identificadores.size(); i++){
            non_added.add(i);
        }
        Vector<Integer> valid = new Vector<Integer>(non_added);
        boolean added = false;
        Integer id = 0;

        // Metemos siempre el índice 0 en la solución, que será nuestro punto de partida siempre, este puede ser un múseo, u otra dirección.
        solution.add(id);
        non_added.remove(id);
        valid.remove(id);

        // Terminaremos cuando ya hayamos llegado al tiempo límite.
        while( current_time.isBefore(finish_time) && !non_added.isEmpty()){
            added = false;
            valid = non_added;

            while(!added && !valid.isEmpty()){
                // Calculamos el museo al cual tardamos menos en llegar desde donde estamos.
                Integer pos = findNearest(id,valid);

                // Comprobamos que podamos ir dentro del horario que esté abierto, o por la mañana o por la tarde.
                if( checkTime(current_time.plusSeconds(duracion.elementAt(id).elementAt(pos)), horarios_abierto.elementAt(pos-1)) ) {
                    current_time = current_time.plusSeconds(duracion.elementAt(id).elementAt(pos));
                    System.out.println("Entrando en museo: " + pos + " a las " + current_time);
                    current_time = current_time.plusMinutes(ThreadLocalRandom.current().nextInt(60,180+1) );
                    System.out.println("Saliendo de museo: " + pos + " a las " + current_time);
                    id = pos;
                    solution.add(id);
                    added = true;
                    non_added.remove(id);
                    valid.remove(id);

                } // Si no es válido, comprobamos si estamos dentro del horario incluido, si es así, cambiamos current_time y lo incluimos en la solución.
                else if(checkLunchTime(current_time.plusSeconds(duracion.elementAt(id).elementAt(pos)), horarios_abierto.elementAt(pos-1))){
                    current_time = horarios_abierto.elementAt(pos).elementAt(1).getKey();
                    System.out.println("Entrando en museo: " + pos + " a las " + current_time);
                    current_time = current_time.plusMinutes(ThreadLocalRandom.current().nextInt(60,180+1) );
                    System.out.println("Saliendo de museo: " + pos + " a las " + current_time);
                    id = pos;
                    solution.add(id);
                    added = true;
                    non_added.remove(id);
                    valid.remove(id);
                } // Si no es posible ir en este momento, lo eliminamos de los casos válidos.
                else{
                    valid.remove(pos);
                }

            }

            if(valid.isEmpty() && !added){
                non_added.clear();
            }

        }

        for(Integer i:solution)
            ids_solution.add(identificadores.elementAt(i));

        return ids_solution;
    }

}
