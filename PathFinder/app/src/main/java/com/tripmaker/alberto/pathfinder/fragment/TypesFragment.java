package com.tripmaker.alberto.pathfinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.adapters.TypesRecyclerAdapter;
import com.tripmaker.alberto.pathfinder.interfaces.CustomClickListener;
import com.tripmaker.alberto.pathfinder.models.CityNode;
import com.tripmaker.alberto.pathfinder.models.ModelNode;
import com.tripmaker.alberto.pathfinder.models.TypeOfNode;
import java.util.ArrayList;
import java.util.List;


public class TypesFragment extends Fragment {

    private RecyclerView mRecycler;
    private TypesRecyclerAdapter mAdapter;
    private Button searchButton;
    private ArrayList<?extends ModelNode> mList = new ArrayList<>();
    private String TAG = TypesFragment.class.getSimpleName();
    private CustomClickListener listener;

    public TypesFragment() {
        // Required empty public constructor
    }

    public void setmList(ArrayList<?extends ModelNode> mList) {
        this.mList = mList;
    }

    public void setListener(CustomClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View recyclerView = inflater.inflate(R.layout.fragment_types, container, false);
        searchButton = (Button) recyclerView.findViewById(R.id.busqueda);
        mRecycler = (RecyclerView) recyclerView.findViewById(R.id.recycler);
        mAdapter = new TypesRecyclerAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSearchButtonClick(getSelected());
            }
        });

        return recyclerView;
    }


    public ArrayList<String> getSelected(){
        return mAdapter.getSelected();
    }




}
