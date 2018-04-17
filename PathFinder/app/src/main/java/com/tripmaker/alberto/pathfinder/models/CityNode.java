package com.tripmaker.alberto.pathfinder.models;

public class CityNode extends ModelNode{
    private String type;

    public CityNode(String n,String type){
        super(n);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
