package com.tripmaker.alberto.pathfinder.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.tripmaker.alberto.pathfinder.R;

public class SolutionNodeViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView salida;
    private TextView entrada;

    public SolutionNodeViewHolder(View view){
        super(view);
        this.name = (TextView) view.findViewById(R.id.sol_node_name);
        this.entrada = (TextView) view.findViewById(R.id.entrada);
        this.salida = (TextView) view.findViewById(R.id.salida);
    }

    public void setName(String n){
        this.name.setText(n);
    }

    public void setEntrada(String en){
        this.entrada.setText(en);
    }

    public void setSalida(String sa){
        this.salida.setText(sa);
    }
}
