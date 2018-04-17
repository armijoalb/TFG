package com.tripmaker.alberto.pathfinder.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.tripmaker.alberto.pathfinder.R;

public class CityNodesViewHolder extends RecyclerView.ViewHolder {
    private TextView nodeName;
    private TextView nodeType;
    private CheckBox mAddButton;
    private String TAG = CityNodesViewHolder.class.getSimpleName();
    private boolean isChecked = false;

    public CityNodesViewHolder(View itemView){
        super(itemView);
        nodeName = (TextView) itemView.findViewById(R.id.nodeName);
        mAddButton = (CheckBox) itemView.findViewById(R.id.addButton);
        nodeType = (TextView) itemView.findViewById(R.id.type_name);
        mAddButton.setChecked(isChecked);
    }

    public void setOnClickListener(View.OnClickListener listener){
        mAddButton.setOnClickListener(listener);
    }

    public void setNodeName(String name){
        nodeName.setText(name);
    }

    public void setNodeType(String type){
        nodeType.setText(type);
    }

    public boolean getIsChecked(){
        return isChecked;
    }

    public void changeChecked(){
        if(mAddButton.isChecked()){
            Log.i(TAG,mAddButton.isChecked()+"");
            mAddButton.setChecked(true);
        }else{
            Log.i(TAG,mAddButton.isChecked()+"");
            mAddButton.setChecked(false);
        }

        isChecked = !isChecked;
    }

    public void setCheck(boolean check){
        mAddButton.setChecked(check);
    }


}
