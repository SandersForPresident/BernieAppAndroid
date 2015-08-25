package com.spielpark.steve.bernieapp.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.MainActivity;
import com.spielpark.steve.bernieapp.misc.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class BernRateFragment extends Fragment {
    private static BernRateFragment mInstance;
    private static String[] urls = new String[]{
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=followers:berniesanders\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=tweet:feelthebern\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=tweet:bernie2016\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=reddit:sandersforpresident:subscribers\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=reddit:sandersforpresident:visitors\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=reddit:sandersforpresident:subscriptions\""
    };
    //private static final String[] urls = new String[]{
    //        "<iframe src=\"http://www.bernrate.com/embed?dataKey=followers:berniesanders\"></iframe>"
   // };

    public static BernRateFragment getInstance() {
        if (mInstance == null) {
            mInstance = new BernRateFragment();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences p = ((MainActivity) getActivity()).getPrefs();
        Util.Preferences key = Util.Preferences.BERNRATE_DIALOGUE;
        if (p.getInt(key.name, key.value) == 1) {
            Util.getShowAgainDialogue(getActivity(), p, key, "Welcome to BernRate. This section is dedicated to showing you how Bernie Sanders is doing with popularity in various Social Medias. Created by Arman, this contribution was made by volunteers, NOT the millionaire class.").show();
        }
        displayWebViews(generateURLs());
    }

    private String[] generateURLs() {
        String[] gen = new String[urls.length];
        int[] heightWidth = Util.getScreenWidthHeight(getActivity());
        for (int i = 0; i < gen.length; i++) {
            gen[i] = urls[i].concat(" width=\"" + heightWidth[0] + "px\" height=\"" + heightWidth[1] + "px\"></iframe>");
        }
        return gen;
    }

    private void displayWebViews(String[] urls) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout parent = (LinearLayout) getView().findViewById(R.id.layoutBernRate);
        params.setMargins(8, 8, 8, 8);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (String s : urls) {
            WebView wv = new WebView(getActivity());
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setLayoutParams(params);
            wv.loadData(s, "text/Html", "UTF-8");
            parent.addView(wv);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_bernrate, container, false);
    }


}
