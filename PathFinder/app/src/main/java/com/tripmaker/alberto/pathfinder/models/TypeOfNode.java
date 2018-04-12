package com.tripmaker.alberto.pathfinder.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class TypeOfNode extends ExpandableGroup {

    private String nombre_tipo;
    private List items;

    public TypeOfNode(String title, List items) {
        super(title, items);
        nombre_tipo = title;
        this.items = items;
    }

    public List getItems(){
        return items;
    }

    public String getTitle(){ return nombre_tipo; }
}
