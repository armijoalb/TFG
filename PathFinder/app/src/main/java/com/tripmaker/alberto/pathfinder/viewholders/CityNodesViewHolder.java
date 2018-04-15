package com.tripmaker.alberto.pathfinder.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.tripmaker.alberto.pathfinder.R;

public class CityNodesViewHolder extends ChildViewHolder {
    private TextView nodeName;
    private CheckBox mAddButton;
    private String TAG = CityNodesViewHolder.class.getSimpleName();
    private boolean isChecked = false;

    public CityNodesViewHolder(View itemView){
        super(itemView);
        nodeName = (TextView) itemView.findViewById(R.id.nodeName);
        mAddButton = (CheckBox) itemView.findViewById(R.id.addButton);
        mAddButton.setChecked(false);
        isChecked = false;
    }

    public void setOnClickListener(View.OnClickListener listener){
        mAddButton.setOnClickListener(listener);
    }

    public void setNodeName(String name){
        nodeName.setText(name);
    }

    public void changeChecked(){
        if(mAddButton.isChecked()){
            Log.i(TAG,mAddButton.isChecked()+"");
            mAddButton.setChecked(true);
            isChecked = true;
        }else{
            Log.i(TAG,mAddButton.isChecked()+"");
            mAddButton.setChecked(false);
            isChecked = false;
        }
    }
    public void setCheck(boolean check){
        mAddButton.setChecked(check);
    }

    public void resetChecked(){
        Log.i(TAG,isChecked+"");
        setCheck(isChecked);
    }

}
