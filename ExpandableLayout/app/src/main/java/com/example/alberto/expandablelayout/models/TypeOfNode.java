package com.example.alberto.expandablelayout.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class TypeOfNode extends ExpandableGroup {

    private String nombre_tipo;

    public TypeOfNode(String title, List items) {
        super(title, items);
        nombre_tipo = title;
    }

    public String getTitle(){ return nombre_tipo; }
}
