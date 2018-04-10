package com.example.alberto.expandablelayout;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alberto.expandablelayout.fragment.TypesFragment;

public class MainActivity extends AppCompatActivity {

    private TypesFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFragment();
    }

    private void setupFragment(){
        mFragment = new TypesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.recyclerLayout,mFragment);
        transaction.commit();

    }
}
