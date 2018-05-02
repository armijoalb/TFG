package com.tripmaker.alberto.pathfinder;


import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.tripmaker.alberto.pathfinder.fragment.SolutionFragment;

import java.util.ArrayList;

public class ResultActivity extends FragmentActivity  {

    private GoogleMap mMap;
    private ArrayList<ArrayList<Integer>> segundos = new ArrayList<>();

    private ArrayList<String> identificadores = new ArrayList<>();

    private String TAG = ResultActivity.class.getSimpleName();
    private double lat_city;
    private double lon_city;
    private ArrayList<String> lat_ids = new ArrayList<>();
    private ArrayList<String> lon_ids = new ArrayList<>();
    private ArrayList<String> entradas = new ArrayList<>();
    private ArrayList<String> salidas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_sol);

        Log.i(TAG,"onCreate");
        lat_city = getIntent().getExtras().getDouble("LAT");
        lon_city = getIntent().getExtras().getDouble("LON");
        identificadores = getIntent().getExtras().getStringArrayList("IDS");
        lat_ids = getIntent().getExtras().getStringArrayList("LATS");
        lon_ids = getIntent().getExtras().getStringArrayList("LONS");
        entradas = getIntent().getExtras().getStringArrayList("ENTRADA");
        salidas = getIntent().getExtras().getStringArrayList("SALIDA");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private String TAG = SimpleFragmentPagerAdapter.class.getSimpleName();

        public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        // This determines the fragment for each tab
        @Override
        public Fragment getItem(int position) {
            Log.i(TAG,"position "+ position);
            if (position == 0) {
                return SolutionFragment.newInstance(entradas,salidas,identificadores,lat_ids,lon_ids,lat_city,lon_city);
            } else if (position == 1){
                return SolutionFragment.newInstance(entradas,salidas,identificadores,lat_ids,lon_ids,lat_city,lon_city);
            } else if (position == 2){
                return SolutionFragment.newInstance(entradas,salidas,identificadores,lat_ids,lon_ids,lat_city,lon_city);
            } else{
                return null;
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 1;
        }

        // This determines the title for each tab
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Solución 1";
                case 1:
                    return "Solución 2";
                case 2:
                    return "Solución 3";
                default:
                    return null;

            }

        }

    }

}
