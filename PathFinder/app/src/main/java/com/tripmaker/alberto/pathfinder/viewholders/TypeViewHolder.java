package com.tripmaker.alberto.pathfinder.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.tripmaker.alberto.pathfinder.R;

import java.lang.reflect.Type;

public class TypeViewHolder extends RecyclerView.ViewHolder{

    private TextView mTypeText;
    private CheckBox mAllTypeCheckBox;
    private String TAG = TypeViewHolder.class.getSimpleName();

    public TypeViewHolder(View itemView) {
        super(itemView);
        mTypeText = (TextView) itemView.findViewById(R.id.type);
        mAllTypeCheckBox = (CheckBox) itemView.findViewById(R.id.typeCheck);
        mAllTypeCheckBox.setChecked(false);
    }

    public void setTypeText(String text){ mTypeText.setText(text); }

    public void setOnClickListener(View.OnClickListener listener){
        mAllTypeCheckBox.setOnClickListener(listener);
    }

    public void changeChecked(){
        if(mAllTypeCheckBox.isChecked()){
            mAllTypeCheckBox.setChecked(true);
        }else{
            mAllTypeCheckBox.setChecked(false);
        }
    }

    public void setCheck(boolean check){
        mAllTypeCheckBox.setChecked(check);
    }


}
