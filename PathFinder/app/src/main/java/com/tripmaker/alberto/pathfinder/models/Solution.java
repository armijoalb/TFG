package com.tripmaker.alberto.pathfinder.models;


import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class Solution{
    private ArrayList<String> ids_solution = new ArrayList<>();
    private ArrayList<SimpleEntry<String,String>> franja_horaria = new ArrayList<>();

    public void add(String id,SimpleEntry<String,String> franja){
        ids_solution.add(id);
        franja_horaria.add(franja);
    }

    public ArrayList<String> getIds(){
        return ids_solution;
    }

    public ArrayList<SimpleEntry<String, String>> getHours() {
        return franja_horaria;
    }

    public int size(){
        return ids_solution.size();
    }

    public ArrayList<String> getTimeEntrada() {
        ArrayList<String> entradas = new ArrayList<>();
        for (int i=0; i < franja_horaria.size(); i++){
            entradas.add(franja_horaria.get(i).getKey());
        }

        return entradas;
    }

    public ArrayList<String> getTimeSalida() {
        ArrayList<String> salida = new ArrayList<>();
        for (int i=0; i < franja_horaria.size(); i++){
            salida.add(franja_horaria.get(i).getValue());
        }

        return salida;
    }
}