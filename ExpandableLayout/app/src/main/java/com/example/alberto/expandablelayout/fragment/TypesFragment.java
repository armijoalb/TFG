package com.example.alberto.expandablelayout.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.alberto.expandablelayout.R;
import com.example.alberto.expandablelayout.adapters.TypesRecyclerAdapter;
import com.example.alberto.expandablelayout.models.CityNode;
import com.example.alberto.expandablelayout.models.TypeOfNode;

import java.util.ArrayList;
import java.util.List;


public class TypesFragment extends Fragment {

    private RecyclerView mRecycler;
    private TypesRecyclerAdapter mAdapter;
    private List<TypeOfNode> mList = new ArrayList<>();
    private String TAG = TypesFragment.class.getSimpleName();

    public TypesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View recyclerView = inflater.inflate(R.layout.fragment_types, container, false);
        mRecycler = (RecyclerView) recyclerView.findViewById(R.id.recycler);
        getNodes();
        mAdapter = new TypesRecyclerAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);

        return recyclerView;
    }

    private void getNodes(){
        mList = new ArrayList<>();
        List<CityNode> nodes = new ArrayList<>();
        nodes.add(new CityNode("Parque de las ciencias"));
        nodes.add(new CityNode("Museo de la historia de Andalucía"));
        mList.add(new TypeOfNode("Museos",nodes));
        nodes = new ArrayList<>();
        nodes.add(new CityNode( "Catedral de Granada") );
        mList.add(new TypeOfNode("Monumentos",nodes));
        nodes = new ArrayList<>();
        nodes.add(new CityNode("Mirador de San Miguel"));
        nodes.add(new CityNode("Mirador de San Nicolás"));
        mList.add(new TypeOfNode("Miradores",nodes));
        nodes = new ArrayList<>();
        nodes.add(new CityNode("Hotel el Carmen"));
        nodes.add(new CityNode("Hotel Granada Center"));
        mList.add(new TypeOfNode("Hoteles/Hostales",nodes));

    }



}
