package com.tripmaker.alberto.pathfinder.models;

public class SolutionNode {
    private String name;
    private String entrada;
    private String salida;

    public SolutionNode(String n, String en, String sa){
        name = n;
        entrada = en;
        salida = sa;
    }

    public String getName(){
        return name;
    }

    public String getEntrada(){
        return entrada;
    }

    public String getSalida(){
        return salida;
    }
}
