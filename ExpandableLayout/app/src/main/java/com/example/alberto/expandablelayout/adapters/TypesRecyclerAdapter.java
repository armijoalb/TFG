package com.example.alberto.expandablelayout.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alberto.expandablelayout.R;
import com.example.alberto.expandablelayout.models.CityNode;
import com.example.alberto.expandablelayout.viewholders.CityNodesViewHolder;
import com.example.alberto.expandablelayout.viewholders.TypeViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class TypesRecyclerAdapter extends ExpandableRecyclerViewAdapter<TypeViewHolder,CityNodesViewHolder> {

    public TypesRecyclerAdapter(List<? extends ExpandableGroup> tipos){
        super(tipos);
    }

    @Override
    public TypeViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.types_viewholder, parent,false);
        return new TypeViewHolder(view);
    }

    @Override
    public CityNodesViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nodes_viewholder,parent,false);
        return new CityNodesViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(CityNodesViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        CityNode mNode = (CityNode) group.getItems().get(childIndex);
        holder.setNodeName(mNode.getName());

    }

    @Override
    public void onBindGroupViewHolder(TypeViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTypeText(group.getTitle());
    }
}
