package com.tripmaker.alberto.pathfinder.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.models.CityNode;
import com.tripmaker.alberto.pathfinder.viewholders.CityNodesViewHolder;
import com.tripmaker.alberto.pathfinder.viewholders.TypeViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TypesRecyclerAdapter extends ExpandableRecyclerViewAdapter<TypeViewHolder,CityNodesViewHolder> {

    private String TAG = TypesRecyclerAdapter.class.getSimpleName();
    private List<?extends ExpandableGroup> tipos = new ArrayList<>();
    private HashMap<String,Vector<String>> selected = new HashMap<>();

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
        return new CityNodesViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final CityNodesViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        final CityNode mNode = (CityNode) group.getItems().get(childIndex);
        final String name = group.getTitle();

        holder.setNodeName(mNode.getName());
        holder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.changeChecked();
                addOrRemoveNode(name,mNode.getName());
            }
        });
    }

    public void addOrRemoveNode(String tipo, String nombre){
        if(selected.containsKey(tipo)){
            if(selected.get(tipo).contains(nombre)){
                selected.get(tipo).remove(nombre);
            }else{
                selected.get(tipo).add(nombre);
            }
        }else{
            Vector<String>  aux = new Vector<>();
            aux.add(nombre);
            selected.put(tipo,aux);
        }
    }


    @Override
    public void onBindGroupViewHolder(TypeViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTypeText(group.getTitle());
    }

    public ArrayList<String> getSelected(){
        ArrayList<String> s_nodes = new ArrayList<>();
        String aux_key;
        if(selected.containsKey("hostel")){
            aux_key = "hostel";
        }else{
            aux_key = "hotel";
        }

        if(selected.get(aux_key).size() < 2){
            s_nodes.add(selected.get(aux_key).elementAt(0));

            for(Map.Entry<String,Vector<String>> it:selected.entrySet()){
                if(!it.getKey().equals(aux_key)){
                    s_nodes.addAll(it.getValue());
                }
            }
        }


        return s_nodes;
    }
}
