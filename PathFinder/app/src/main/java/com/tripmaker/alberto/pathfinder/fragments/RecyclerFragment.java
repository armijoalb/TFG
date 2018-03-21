package com.tripmaker.alberto.pathfinder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripmaker.alberto.pathfinder.R;
import com.tripmaker.alberto.pathfinder.custom_adapter.MyAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerFragment extends Fragment {

    private RecyclerView mRecycler;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> cityNames = new ArrayList<>();


    public RecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cityNames = getArguments().getStringArrayList("nodes");
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_recycler, container, false);

        mRecycler = (RecyclerView) viewRoot.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(cityNames);
        mRecycler.setAdapter(mAdapter);

        return viewRoot;
    }

}
