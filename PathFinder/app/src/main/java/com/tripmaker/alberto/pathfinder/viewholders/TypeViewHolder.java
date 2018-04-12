package com.tripmaker.alberto.pathfinder.viewholders;

import android.view.View;
import android.widget.TextView;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.tripmaker.alberto.pathfinder.R;

public class TypeViewHolder extends GroupViewHolder {

    private TextView mTypeText;

    public TypeViewHolder(View itemView) {
        super(itemView);
        mTypeText = (TextView) itemView.findViewById(R.id.type);
    }

    public void setTypeText(String text){ mTypeText.setText(text); }


}
