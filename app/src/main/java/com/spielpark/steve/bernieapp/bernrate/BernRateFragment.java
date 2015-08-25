package com.spielpark.steve.bernieapp.bernrate;


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
import com.spielpark.steve.bernieapp.dagger.Dagger;
import com.spielpark.steve.bernieapp.misc.Util;
import com.spielpark.steve.bernieapp.persistence.DataStore;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 */
public class BernRateFragment extends Fragment {
    private static String[] urls = new String[]{
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=followers:berniesanders\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=tweet:feelthebern\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=tweet:bernie2016\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=reddit:sandersforpresident:subscribers\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=reddit:sandersforpresident:visitors\"",
            "<iframe src=\"http://www.bernrate.com/embed?dataKey=reddit:sandersforpresident:subscriptions\""
    };

    @Inject
    DataStore mDataStore;

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

    private String[] generateURLs() {
        String[] gen = new String[urls.length];
        int[] heightWidth = Util.getScreenWidthHeight(getActivity());
        for (int i = 0; i < gen.length; i++) {
            gen[i] = urls[i].concat(" width=\"" + heightWidth[0] + "px\" height=\"" + heightWidth[1] + "px\"></iframe>");
        }
        return gen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dagger.applicationComponent(getActivity()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_bernrate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mDataStore.showBernRateDialog()) {
            BernRateDialogFragment fragment = new BernRateDialogFragment();
            fragment.show(getChildFragmentManager(), "BernRateIntro");
        }

        displayWebViews(generateURLs());
    }


}
