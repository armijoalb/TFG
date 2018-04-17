package com.tripmaker.alberto.pathfinder.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.models.CityNode;
import com.tripmaker.alberto.pathfinder.models.ModelNode;
import com.tripmaker.alberto.pathfinder.viewholders.CityNodesViewHolder;
import com.tripmaker.alberto.pathfinder.viewholders.TypeViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TypesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = TypesRecyclerAdapter.class.getSimpleName();
    private ArrayList<?extends ModelNode> tipos = new ArrayList<>();
    private HashMap<String, Vector<String>> selected = new HashMap<>();

    public TypesRecyclerAdapter(ArrayList< ?extends ModelNode> all) {
        this.tipos = all;
    }

    @Override
    public int getItemViewType(int position) {
        int pos=0;
        if(tipos.get(position).getClass().toString().contains("CityNode")){
            pos = 2;
        }
        return pos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.types_viewholder,
                        parent, false);
                return new TypeViewHolder(view);

            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nodes_viewholder,
                        parent,false);
                return new CityNodesViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case 0:
                final TypeViewHolder typeViewHolder = (TypeViewHolder) holder;
                typeViewHolder.setTypeText(tipos.get(position).getName());
                break;
            case 2:
                final CityNode cityNode = (CityNode) tipos.get(position);
                final CityNodesViewHolder node = (CityNodesViewHolder) holder;
                node.setNodeName(cityNode.getName());
                node.setNodeType(cityNode.getType());
                node.setCheck(isInSelected(cityNode.getType(),cityNode.getName()));
                node.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        node.changeChecked();
                        addOrRemoveNode(cityNode.getType(), cityNode.getName());
                    }
                });
                break;
        }


    }

    public boolean isInSelected(String tipo, String name){
        boolean is_contained = false;
        if(selected.containsKey(tipo)){
            if(selected.get(tipo).contains(name)){
                is_contained = true;
            }
        }
        return is_contained;
    }

    public void addOrRemoveNode(String tipo, String nombre) {
        Log.i(TAG, "calling to add or remove");
        if (selected.containsKey(tipo)) {
            if (selected.get(tipo).contains(nombre)) {
                Log.i(TAG, "addOrRemove:removing");
                if (selected.get(tipo).size() == 1)
                    selected.remove(tipo);
                else
                    selected.get(tipo).remove(nombre);
            } else {
                Log.i(TAG, "addOrRemove:adding");
                selected.get(tipo).add(nombre);
            }
        } else {
            Log.i(TAG, "addOrRemove:creating new type " + tipo);
            Vector<String> aux = new Vector<>();
            aux.add(nombre);
            selected.put(tipo, aux);
        }
    }


    public ArrayList<String> getSelected() {
        ArrayList<String> s_nodes = new ArrayList<>();
        String aux_key = "";

        if (selected.size() < 1) {
            Log.i(TAG, selected.size() + "");
            return s_nodes;
        }

        if (selected.containsKey("hostel")) {
            Log.i(TAG, "is an hostel");
            aux_key = "hostel";
        } else if (selected.containsKey("hotel")) {
            Log.i(TAG, "is an hotel");
            aux_key = "hotel";
        } else {
            Log.i(TAG, "no hay nodos de salida");
            return s_nodes;
        }

        if (selected.get(aux_key).size() < 2) {
            Log.i(TAG, selected.get(aux_key).size() + "");
            s_nodes.add(selected.get(aux_key).elementAt(0));

            for (Map.Entry<String, Vector<String>> it : selected.entrySet()) {
                if (!it.getKey().equals(aux_key)) {
                    s_nodes.addAll(it.getValue());
                }
            }
        }


        return s_nodes;
    }


    @Override
    public int getItemCount() {
        return tipos.size();
    }

}
