package com.tripmaker.alberto.pathfinder.models;

public class TypeOfNode extends ModelNode  {
    private boolean visible = true;
    public TypeOfNode(String title,boolean isVisible) {
        super(title);
        visible = isVisible;
    }

    public boolean getVisible(){
        return visible;
    }



}
