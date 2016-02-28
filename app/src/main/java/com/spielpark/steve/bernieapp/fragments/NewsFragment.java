package com.spielpark.steve.bernieapp.fragments;

/**
 * Created by Steve on 7/9/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.tasks.NewsTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {

    private static NewsFragment mIntstance;

    public static NewsFragment getInstance() {
        if (mIntstance == null) {
            mIntstance = new NewsFragment();
            return mIntstance;
        } else {
            return mIntstance;
        }
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView newsList = (ListView) view.findViewById(R.id.listNews);
        ((TextView) view.findViewById(R.id.txtSubHeader)).setMovementMethod(new ScrollingMovementMethod());
        new NewsTask(getActivity(), newsList, (ProgressBar) view.findViewById(R.id.progressBar), (TextView) view.findViewById(R.id.txtSubHeader), (TextView) view.findViewById(R.id.txtHeader)).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_newsarticles, container, false);
        return rootView;
    }
}