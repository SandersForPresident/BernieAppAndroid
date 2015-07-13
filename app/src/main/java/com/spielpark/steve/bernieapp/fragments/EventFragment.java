package com.spielpark.steve.bernieapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    private static Event mEvent;


    public static EventFragment newInstance(Event e) {
        mEvent = e;
        return new EventFragment();
    }

    public EventFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String time = mEvent.getTime();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            time = new SimpleDateFormat("h:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        View root = getView();
        if (!(mEvent.isrss)) {
            root.findViewById(R.id.e_txtVenue).setVisibility(View.VISIBLE);
            root.findViewById(R.id.e_txtCity).setVisibility(View.VISIBLE);
            ((TextView) root.findViewById(R.id.e_txtVenue)).setText(mEvent.getVenue());
            ((TextView) root.findViewById(R.id.e_txtCity)).setText(mEvent.getVenue_city() + ", " + mEvent.getState());
        } else {
            root.findViewById(R.id.e_txtVenue).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.e_txtCity).setVisibility(View.INVISIBLE);
        }
        ((TextView) root.findViewById(R.id.e_txtTitle)).setText(mEvent.getName());
        ((TextView) root.findViewById(R.id.e_txtDate)).setText(mEvent.getDate() + " at " + time);
        ((TextView) root.findViewById(R.id.e_txtDesc)).setText(Html.fromHtml(mEvent.getDescription()));
        ((TextView) root.findViewById(R.id.e_txtDesc)).setMovementMethod(new ScrollingMovementMethod());
         root.findViewById(R.id.e_btnWebsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mEvent.getUrl()));
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_event, container, false);
    }
}
