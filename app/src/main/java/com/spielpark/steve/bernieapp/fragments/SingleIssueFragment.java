package com.spielpark.steve.bernieapp.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    private float oldY;

    public SingleIssueFragment() {
        // Required empty public constructor
    }

    public static SingleIssueFragment newInstance(Issue i) {
        mIssue = i;
        return new SingleIssueFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int pixels = (int) (200 * scale + 0.5f);
        final View root = getView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, pixels);
                    root.findViewById(R.id.i_imgLogo).setLayoutParams(params);
        ((TextView) root.findViewById(R.id.i_txtTitle)).setText(mIssue.getTitle());
        ((TextView) root.findViewById(R.id.i_txtDate)).setText("Published " + formattedDate + " at " + time);
        ((TextView) root.findViewById(R.id.i_txtDesc)).setText(Html.fromHtml(mIssue.getDesc()));
        //((TextView) root.findViewById(R.id.i_txtDesc)).setMovementMethod(new ScrollingMovementMethod());
        /*((TextView) root.findViewById(R.id.i_txtDesc)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    oldY = 1.0f;
                    return false;
                }
                if (oldY == 1.0f) {
                    oldY = event.getY();
                } else {
                    float diff = (oldY-event.getY());
                    oldY = event.getY();
                    Log.d("..", Float.toString(diff));
                    root.findViewById(R.id.i_imgLogo).animate().translationYBy(diff);
                }
                return false;
            }
        });
    */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_single_issue, container, false);
    }


}
