package com.tripmaker.alberto.pathfinder.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.models.SolutionNode;
import com.tripmaker.alberto.pathfinder.viewholders.SolutionNodeViewHolder;

import java.util.ArrayList;

public class SolutionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SolutionNode> mNodes = new ArrayList<>();

    public SolutionRecyclerAdapter(ArrayList<SolutionNode> nodes){
        mNodes = nodes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.solution_viewholder,parent,false);


        return new SolutionNodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SolutionNodeViewHolder mHolder = (SolutionNodeViewHolder) holder;
        mHolder.setName(mNodes.get(position).getName());
        mHolder.setEntrada("Entrada: "+ mNodes.get(position).getEntrada());
        mHolder.setSalida("Salida: "+ mNodes.get(position).getSalida());
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }
}
