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
}