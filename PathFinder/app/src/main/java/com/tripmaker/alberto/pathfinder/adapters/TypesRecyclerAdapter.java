package com.tripmaker.alberto.pathfinder.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.models.CityNode;
import com.tripmaker.alberto.pathfinder.models.ModelNode;
import com.tripmaker.alberto.pathfinder.models.TypeOfNode;
import com.tripmaker.alberto.pathfinder.viewholders.CityNodesViewHolder;
import com.tripmaker.alberto.pathfinder.viewholders.TypeViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class TypesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = TypesRecyclerAdapter.class.getSimpleName();
    private ArrayList<?extends ModelNode> tipos = new ArrayList<>();
    private HashMap<String, Vector<String>> selected = new HashMap<>();
    private ArrayList<String> type_selected = new ArrayList<>();

    public TypesRecyclerAdapter(ArrayList< ?extends ModelNode> all) {
        this.tipos = all;
    }

    public void setTipos(ArrayList<?extends ModelNode> tipos){
        this.tipos = tipos;
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
                final TypeOfNode typeNode = (TypeOfNode) tipos.get(position);
                typeViewHolder.setTypeText(typeNode.getName());
                typeViewHolder.setCheck(isInTypeSelected(typeNode.getName()));
                typeViewHolder.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        typeViewHolder.changeChecked();
                        addOrRemoveType(typeNode.getName());
                    }
                });
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

    private void addOrRemoveType(String name){
        if(type_selected.contains(name)){
            type_selected.remove(name);
        }else{
            type_selected.add(name);
        }

        for(Iterator<?extends ModelNode> it = tipos.iterator(); it.hasNext();){
            ModelNode node = it.next();
            if(node.getClass().toString().contains("CityNode")){
                Log.i(TAG,"new city node");
                CityNode aux = (CityNode) node;
                if(aux.getType().equals(name)){
                    Log.i("Adding all types","yes");
                    addOrRemoveNode(name,aux.getName());
                }
            }
        }

        notifyDataSetChanged();
    }

    private boolean isInTypeSelected(String name) {
        return type_selected.contains(name);
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

                if(type_selected.contains(tipo)){
                    type_selected.remove(tipo);
                    notifyDataSetChanged();
                }
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
        String aux_key = "Alojamiento";

        if (selected.size() < 1) {
            Log.i(TAG, selected.size() + "");
            return s_nodes;
        }

        if (!selected.containsKey("Alojamiento")) {
            Log.i(TAG, "no hay nodos de salida");
            return s_nodes;
        }

        if (selected.get(aux_key).size() < 2) {
            s_nodes.add(selected.get(aux_key).elementAt(0));
            Log.i("selected: ", selected.get(aux_key).elementAt(0));
            for (Map.Entry<String, Vector<String>> it : selected.entrySet()) {
                if (!it.getKey().equals(aux_key)) {
                    s_nodes.addAll(it.getValue());
                    Log.i("selected: ", it.getValue().toString());
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
