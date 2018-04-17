package com.tripmaker.alberto.pathfinder.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.tripmaker.alberto.pathfinder.R;

public class TypeViewHolder extends RecyclerView.ViewHolder{

    private TextView mTypeText;

    public TypeViewHolder(View itemView) {
        super(itemView);
        mTypeText = (TextView) itemView.findViewById(R.id.type);
    }

    public void setTypeText(String text){ mTypeText.setText(text); }


}
