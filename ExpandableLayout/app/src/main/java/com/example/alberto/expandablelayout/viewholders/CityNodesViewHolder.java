package com.example.alberto.expandablelayout.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.alberto.expandablelayout.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class CityNodesViewHolder extends ChildViewHolder {
    private TextView nodeName;
    private Button mAddButton;

    public CityNodesViewHolder(View itemView){
        super(itemView);
        nodeName = (TextView) itemView.findViewById(R.id.nodeName);
        mAddButton = (Button) itemView.findViewById(R.id.addButton);
    }

    public void setNodeName(String name){
        nodeName.setText(name);
    }
}
