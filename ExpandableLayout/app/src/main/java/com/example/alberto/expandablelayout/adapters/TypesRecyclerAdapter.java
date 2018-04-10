package com.example.alberto.expandablelayout.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alberto.expandablelayout.R;
import com.example.alberto.expandablelayout.interfaces.CustomViewHolderListener;
import com.example.alberto.expandablelayout.models.CityNode;
import com.example.alberto.expandablelayout.viewholders.CityNodesViewHolder;
import com.example.alberto.expandablelayout.viewholders.TypeViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TypesRecyclerAdapter extends ExpandableRecyclerViewAdapter<TypeViewHolder,CityNodesViewHolder>
    implements CustomViewHolderListener{

    private String TAG = TypesRecyclerAdapter.class.getSimpleName();
    private List<?extends ExpandableGroup> tipos = new ArrayList<>();

    public TypesRecyclerAdapter(List<? extends ExpandableGroup> tipos){
        super(tipos);
        this.tipos = tipos;
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
        return new CityNodesViewHolder(view,this);
    }

    @Override
    public void onBindChildViewHolder(CityNodesViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        final CityNode mNode = (CityNode) group.getItems().get(childIndex);
        final String name = group.getTitle();

        holder.setNodeName(mNode.getName());
    }

    @Override
    public void onBindGroupViewHolder(TypeViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTypeText(group.getTitle());
    }

    @Override
    public void onCityNodeSelected(String n) {
        Log.i(TAG,"checked "+n);
    }

    public void onCityNodeUnselected(String n){
        Log.i(TAG,"unchecked "+n);
    }
}
