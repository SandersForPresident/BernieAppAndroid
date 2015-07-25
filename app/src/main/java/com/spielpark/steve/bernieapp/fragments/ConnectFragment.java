package com.spielpark.steve.bernieapp.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends Fragment {
    private static GoogleMap map;
    public int mZip = 0;
    public int mRadius = 50;
    private static ConnectFragment mInstance;
    public static ConnectFragment getInstance() {
        if (mInstance == null) {
            mInstance = new ConnectFragment();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getView().findViewById(R.id.c_btnRadius).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder bld = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
        });
        getView().findViewById(R.id.c_btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);}
        });
        ((ListView) getView().findViewById(R.id.c_listEvents)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItemClicked(i, false);
            }
        });
        setUpMap();
        super.onViewCreated(view, savedInstanceState);
    }

    private void listItemClicked(int pos, boolean alreadyLoaded) {
        final View base = getView();
        if (!alreadyLoaded) {
            View[] topViews = new View[]{
                    base.findViewById(R.id.c_edtZip),
                    base.findViewById(R.id.c_btnRadius),
                    base.findViewById(R.id.c_btnGo)
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
            final View list = base.findViewById(R.id.c_listEvents);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    list.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            list.startAnimation(anim);
            View mapContainer = base.findViewById(R.id.c_mapContainer);
            mapContainer.startAnimation(new AnimationUtils().loadAnimation(getActivity(), R.anim.view_slide_up));
        }
        Event e = ConnectTask.getEvents().get(pos);
        ((TextView) base.findViewById(R.id.cd_txtDate)).setText(e.getDate());
        ((TextView) base.findViewById(R.id.cd_txtTitle)).setText(e.getName());
        ((TextView) base.findViewById(R.id.cd_txtDescContent)).setText(e.getDescription());
        ((TextView) base.findViewById(R.id.cd_txtDescContent)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView) base.findViewById(R.id.cd_txtLocation)).setText(e.getVenue_addr() + "\n" + e.getVenue_city() + ", " + e.getState() + " - " + e.getZip());
        ((TextView) base.findViewById(R.id.cd_txtRSVP)).setText(e.getAttendee_count() + "/" + e.getCapacity());
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), alreadyLoaded ? R.anim.view_fade_in_fast : R.anim.view_fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                base.findViewById(R.id.cd_container).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        base.findViewById(R.id.cd_container).startAnimation(fadeIn);
    }

    private void startTask() {
        if (validZip()) {
            getView().findViewById(R.id.c_btnGo).setEnabled(false);
            getView().findViewById(R.id.c_btnGo).setBackgroundColor(Color.parseColor("#CCCCCC"));
            getView().findViewById(R.id.c_progress).setVisibility(View.VISIBLE);
            new ConnectTask(getActivity(), getInstance()).execute();
        } else {
            Toast.makeText(getActivity(), "Please enter a valid Zip Code!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validZip() {
        mZip = Integer.parseInt(((EditText) getView().findViewById(R.id.c_edtZip)).getText().toString());
        return mZip > 9999;
    }

    private void setRadius(int m) {
        mRadius = m++ < 4 ? m*25 : ((m-2)*50);
        ((Button) getView().findViewById(R.id.c_btnRadius)).setText(mRadius + " miles");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_connect, container, false);
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.c_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ConnectFragment.this.map = googleMap;
            }
        });
    }

    public void setMarkers() {
        ArrayList<Event> events = ConnectTask.getEvents();
        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        List<Marker> markers = new ArrayList<>(events.size());
        LatLng pos;
        Random gen = new Random();
        float hue = 1.0f;
        for (Event e : ConnectTask.getEvents()) {
            pos = new LatLng(e.getLatitude(), e.getLongitude());
            markers.add(map.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(e.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))));
            hue = (hue + gen.nextFloat() * (16.0f - 8.0f) + 8.0f) % 360;
            bld.include(pos);
        }
        LatLngBounds bounds = bld.build();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final int id = Integer.parseInt(marker.getId().substring(1));
                if (getView().findViewById(R.id.cd_container).getVisibility() == View.VISIBLE) {
                    listItemClicked(id, true);
                    return false;
                }
                final ListView list = (ListView) getView().findViewById(R.id.c_listEvents);
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        list.smoothScrollToPositionFromTop(id, 0, 500);
                        list.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                list.setSelection(id);
                            }
                        }, 500);
                    }
                });
                return false;
            }
        });
    }

    public void updateViews(ArrayAdapter a) {
        View parent = getView();
        ListView list = (ListView) getView().findViewById(R.id.c_listEvents);
        list.setAdapter(a);
        list.setVisibility(View.VISIBLE);
        parent.findViewById(R.id.c_mapContainer).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.c_progress).setVisibility(View.GONE);
        parent.findViewById(R.id.c_btnGo).setEnabled(true);
        parent.findViewById(R.id.c_btnGo).setBackgroundColor(Color.parseColor("#147FD7"));
    }

    public void backPressed() {
        View parent = getView();
        parent.findViewById(R.id.c_edtZip).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.c_btnRadius).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.c_listEvents).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.c_btnGo).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.cd_container).setVisibility(View.GONE);
    }
}
