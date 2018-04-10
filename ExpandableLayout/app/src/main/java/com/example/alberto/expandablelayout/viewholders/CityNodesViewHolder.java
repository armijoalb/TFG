package com.example.alberto.expandablelayout.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.alberto.expandablelayout.R;
import com.example.alberto.expandablelayout.interfaces.CustomViewHolderListener;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class CityNodesViewHolder extends ChildViewHolder {
    private TextView nodeName;
    private CheckBox mAddButton;
    private CustomViewHolderListener listener;
    private String TAG = CityNodesViewHolder.class.getSimpleName();

    public CityNodesViewHolder(View itemView, CustomViewHolderListener l){
        super(itemView);
        nodeName = (TextView) itemView.findViewById(R.id.nodeName);
        mAddButton = (CheckBox) itemView.findViewById(R.id.addButton);
        mAddButton.setChecked(false);
        listener = l;

        mAddButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mAddButton.isChecked()){
                    mAddButton.setChecked(true);
                    listener.onCityNodeSelected(nodeName.getText().toString());
                }else{
                    mAddButton.setChecked(false);
                    listener.onCityNodeUnselected(nodeName.getText().toString());
                }

            }
        });
    }

    public void setNodeName(String name){
        nodeName.setText(name);
    }

}
