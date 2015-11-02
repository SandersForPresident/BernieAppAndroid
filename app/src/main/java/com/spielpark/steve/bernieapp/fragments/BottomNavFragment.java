package com.spielpark.steve.bernieapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spielpark.steve.bernieapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomNavFragment extends Fragment {

    private static BottomNavFragment mInstance;

    public static BottomNavFragment getInstance() {
        if (mInstance == null) {
            mInstance = new BottomNavFragment();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_bottom_nav, container, false);
    }
}
