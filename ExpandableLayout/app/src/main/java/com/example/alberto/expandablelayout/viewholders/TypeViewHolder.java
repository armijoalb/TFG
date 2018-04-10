package com.example.alberto.expandablelayout.viewholders;

import android.view.View;
import android.widget.TextView;

import com.example.alberto.expandablelayout.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class TypeViewHolder extends GroupViewHolder {

    private TextView mTypeText;

    public TypeViewHolder(View itemView) {
        super(itemView);
        mTypeText = (TextView) itemView.findViewById(R.id.type);
    }

    public void setTypeText(String text){
        mTypeText.setText(text);
    }
}
