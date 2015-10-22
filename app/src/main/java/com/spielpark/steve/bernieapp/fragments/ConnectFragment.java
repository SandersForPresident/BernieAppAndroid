package com.spielpark.steve.bernieapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.tasks.ConnectTask;
import com.spielpark.steve.bernieapp.wrappers.Event;
import com.spielpark.steve.bernieapp.model.Event;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends Fragment {
    private static ConnectTask mTask;
    private static ConnectFragment mInstance;
    public String mZip = "";
    public int mRadius = 50;
    public boolean fetchCountry = true;
    @Bind(R.id.c_btnGo) Button goButton;
    @Bind(R.id.c_btnRadius) Button radiusButton;
    @Bind(R.id.cd_btnRSVP) Button rsvpButton;
    @Bind(R.id.cd_btnDirections) Button directionButton;
    @Bind(R.id.cd_txtDate) TextView dateText;
    @Bind(R.id.cd_txtTitle) TextView titleText;
    @Bind(R.id.cd_txtDescContent) TextView descriptionText;
    @Bind(R.id.cd_txtLocation) TextView locationText;
    @Bind(R.id.cd_txtRSVP) TextView rsvpText;
    @Bind(R.id.c_edtZip) EditText zipEdit;
    @Bind(R.id.c_progress) ProgressBar progressBar;
    @Bind(R.id.c_mapContainer) FrameLayout mapFrame;
    @Bind(R.id.cd_container) RelativeLayout container;
    @Bind(R.id.c_listEvents) ListView eventList;
    private GoogleMap map;
    private HashMap<Marker, Integer> mHashMap;

    public static ConnectFragment getInstance() {
        if (mInstance == null) {
            mInstance = new ConnectFragment();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    public static void cancelTask() {
        if (mTask != null) {
            if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
                mTask.cancel(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_connect, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpMap();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.c_btnRadius)
    void onRadiusClicked() {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setTitle("Pick a Radius");
        bld.setSingleChoiceItems(R.array.radius_choices, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setRadius(i);
                dialogInterface.dismiss();
            }
        });
        bld.create().show();
    }

    @OnClick(R.id.c_btnGo)
    void onGoClicked(View view) {
        fetchCountry = false;
        startTask();
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnItemClick(R.id.c_listEvents)
    void onEventsListClicked(int position) {
        listItemClicked(position, false);
    }

    private void listItemClicked(int pos, boolean alreadyLoaded) {
        if (!alreadyLoaded) {
            View[] topViews = new View[]{
                    zipEdit, radiusButton, goButton
            };
            for (final View v : topViews) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_top);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(anim);
            }
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    eventList.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            eventList.startAnimation(anim);
            mapFrame.startAnimation(
                    new AnimationUtils().loadAnimation(getActivity(), R.anim.view_slide_up));
        }
        final Event e = ConnectTask.getEvents().get(pos);
        rsvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(e.getUrl()));
                startActivity(i);
            }
        });
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse(
                        "geo:" + Double.toString(e.getLatitude()) + "," + Double.toString(e.getLongitude()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        if (e.getDate() != null) dateText.setText(e.getDate());
        titleText.setText(e.getName());
        descriptionText.setText(Html.fromHtml(e.getDescription()));
        descriptionText.setMovementMethod(new ScrollingMovementMethod());
        if (e.getVenue_city() != null) {
            locationText.setText(
                    String.format("%s\n%s, %s - %d", e.getVenue_addr(), e.getVenue_city(), e.getState(),
                            e.getZip()));
        }
        if (e.getAttendee_count() != 0) {
            rsvpText.setText(e.isOfficial() ? "N/A" : Integer.toString(e.getAttendee_count()));
        }
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(),
                alreadyLoaded ? R.anim.view_fade_in_fast : R.anim.view_fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        container.startAnimation(fadeIn);
    }

    private void startTask() {
        cancelTask();
        if (fetchCountry) {
            mTask = new ConnectTask(getActivity(), getInstance());
            mTask.execute();
        } else if (validZip()) {
            goButton.setEnabled(false);
            goButton.setBackgroundResource(R.color.fragment_connect_go_button_disabled);
            progressBar.setVisibility(View.VISIBLE);
            mTask = new ConnectTask(getActivity(), getInstance());
            mTask.execute();
        } else {
            Toast.makeText(getActivity(), "Please enter a valid Zip Code!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validZip() {
        String text = zipEdit.getText().toString();
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        mZip = text;
        return text.length() == 5;
    }

    private void setRadius(int m) {
        mRadius = m++ < 4 ? m * 25 : ((m - 2) * 50);
        radiusButton.setText(String.format("%d miles", mRadius));
    }

    private void setUpMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.c_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map Ready", "Bam.");
                map = googleMap;
                goButton.setEnabled(true);
                goButton.setBackgroundResource(R.color.fragment_connect_go_button_background);
                progressBar.setVisibility(View.GONE);
            }
        });
        startTask();
    }

    public void setMarkers() {
        map.clear();
        ArrayList<Event> events = ConnectTask.getEvents();
        if (events.size() == 0) {
            return; //There are no events to draw.
        }
        mHashMap = new HashMap<>(events.size());
        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        LatLng pos;
        float hue;
        int id = 0;
        for (Event e : ConnectTask.getEvents()) {
            hue = e.isOfficial() ? 1.0f : 214f;
            pos = new LatLng(e.getLatitude(), e.getLongitude());
            mHashMap.put(map.addMarker(new MarkerOptions().position(pos)
                    .title(e.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))), id++);
            bld.include(pos);
        }
        LatLngBounds bounds = bld.build();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final int id = Integer.parseInt(marker.getId().substring(1));
                if (container.getVisibility() == View.VISIBLE) {
                    listItemClicked(mHashMap.get(marker), true);
                    return false;
                }
                eventList.post(new Runnable() {
                    @Override
                    public void run() {
                        eventList.smoothScrollToPositionFromTop(mHashMap.get(marker), 0, 500);
                        eventList.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventList.setSelection(mHashMap.get(marker));
                            }
                        }, 510);
                    }
                });
                return false;
            }
        });
    }

    public void updateViews(ArrayAdapter a) {
        if (getView() == null) {
            return; //We switched out of this view.
        }
        eventList.setAdapter(a);
        eventList.setVisibility(View.VISIBLE);
        mapFrame.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        goButton.setEnabled(true);
        goButton.setBackgroundResource(R.color.fragment_connect_go_button_background);
    }

    public void backPressed() {
        if (goButton.getVisibility() == View.VISIBLE) {
            getActivity().finish();
            cancelTask();
        } else {
            zipEdit.setVisibility(View.VISIBLE);
            radiusButton.setVisibility(View.VISIBLE);
            eventList.setVisibility(View.VISIBLE);
            goButton.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        }
    }
}
