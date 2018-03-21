package com.tripmaker.alberto.pathfinder.custom_adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tripmaker.alberto.pathfinder.R;

import java.util.ArrayList;

/**
 * Created by alberto on 14/03/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<String> mCityNodes = new ArrayList<>();
    private ArrayList<String> selectedNodes = new ArrayList<>();

    public MyAdapter(ArrayList<String> nodes){
        mCityNodes = nodes;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mName;
        public Button mAddButton;

        public ViewHolder(View v){
            super(v);
            mName = (TextView) v.findViewById(R.id.nodeName);
            mAddButton = (Button) v.findViewById(R.id.addButton);
        }

    }

    public ArrayList<String> getSelectedNodes(){
        return selectedNodes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.viewholder_recycler,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mName.setText(mCityNodes.get(position));

        holder.mAddButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        selectedNodes.add(mCityNodes.get(position));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return mCityNodes.size();
    }
}
