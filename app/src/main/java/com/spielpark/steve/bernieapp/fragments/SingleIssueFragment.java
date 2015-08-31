package com.spielpark.steve.bernieapp.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Issue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleIssueFragment extends Fragment {
    private static Issue mIssue;

    public static SingleIssueFragment newInstance(Issue i) {
        mIssue = i;
        return new SingleIssueFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((WebView) getView().findViewById(R.id.i_video)).onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIssue == null) {
            //User exited the app and returned to it, but android cleared some stuff from memory...
            getActivity().getSupportFragmentManager().popBackStack("base", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        String time = mIssue.getPubDate();
        String formattedDate = "";
        try {
            SimpleDateFormat ft = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
            final Date dateObj = ft.parse(time);
            time = new SimpleDateFormat("h:mm a").format(dateObj);
            formattedDate = new SimpleDateFormat("EEE, d MMM yyyy").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final View root = getView();
        ((TextView) root.findViewById(R.id.i_txtTitle)).setText(mIssue.getTitle());
        ((TextView) root.findViewById(R.id.i_txtTitle)).setShadowLayer(13, 0, 0, Color.BLACK);
        ((TextView) root.findViewById(R.id.i_txtDate)).setText("Published " + formattedDate + " at " + time);
        ((TextView) root.findViewById(R.id.i_txtDesc)).setText(Html.fromHtml(mIssue.getDesc()));
        ((TextView) root.findViewById(R.id.i_txtDesc)).setMovementMethod(new LinkMovementMethod());
        ((WebView) root.findViewById(R.id.i_video)).getSettings().setJavaScriptEnabled(true);
        ((WebView) root.findViewById(R.id.i_video)).loadData(mIssue.getEmbedURL(getActivity()), "text/Html", "UTF-8");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_single_issue, container, false);
    }


}
