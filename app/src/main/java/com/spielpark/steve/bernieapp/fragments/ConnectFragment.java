package com.spielpark.steve.bernieapp.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends Fragment {
    private static ConnectTask mTask;
    private static GoogleMap map;
    private HashMap<Marker, Integer> mHashMap;
    public String mZip = "";
    public int mRadius = 50;
    public boolean fetchCountry = true;
    private static ConnectFragment mInstance;
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
                fetchCountry = false;
                startTask();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        ((ListView) getView().findViewById(R.id.c_listEvents)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItemClicked(i, false);
            }
        });
        fetchCountry = true;
        setUpMap();
        MapsInitializer.initialize(getActivity().getApplicationContext());
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
        final Event e = ConnectTask.getEvents().get(pos);
        base.findViewById(R.id.cd_btnRSVP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(e.getUrl()));
                startActivity(i);
            }
        });
        base.findViewById(R.id.cd_btnDirections).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + Double.toString(e.getLatitude()) + "," + Double.toString(e.getLongitude()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        if (e.getDate() != null) ((TextView) base.findViewById(R.id.cd_txtDate)).setText(e.getDate());
        ((TextView) base.findViewById(R.id.cd_txtTitle)).setText(e.getName());
        ((TextView) base.findViewById(R.id.cd_txtDescContent)).setText(Html.fromHtml(e.getDescription()));
        ((TextView) base.findViewById(R.id.cd_txtDescContent)).setMovementMethod(new ScrollingMovementMethod());
        if (e.getVenue_city() != null) ((TextView) base.findViewById(R.id.cd_txtLocation)).setText(e.getVenue_addr() + "\n" + e.getVenue_city() + ", " + e.getState() + " - " + e.getZip());
        if (e.getAttendee_count() != 0) ((TextView) base.findViewById(R.id.cd_txtRSVP)).setText(e.isOfficial() ? "N/A" : Integer.toString(e.getAttendee_count()));
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
        cancelTask();
        if (fetchCountry) {
            mTask = new ConnectTask(getActivity(), getInstance());
            mTask.execute();
        } else if (validZip()) {
            getView().findViewById(R.id.c_btnGo).setEnabled(false);
            getView().findViewById(R.id.c_btnGo).setBackgroundColor(Color.parseColor("#CCCCCC"));
            getView().findViewById(R.id.c_progress).setVisibility(View.VISIBLE);
            mTask = new ConnectTask(getActivity(), getInstance());
            mTask.execute();
        } else {
            Toast.makeText(getActivity(), "Please enter a valid Zip Code!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validZip() {
        String text = ((EditText) getView().findViewById(R.id.c_edtZip)).getText().toString();
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        mZip = text;
        return text.length() == 5;
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
                Log.d("Map Ready", "Bam.");
                ConnectFragment.this.map = googleMap;
                ConnectFragment.this.getView().findViewById(R.id.c_btnGo).setEnabled(true);
                ConnectFragment.this.getView().findViewById(R.id.c_progress).setVisibility(View.GONE);
                ConnectFragment.this.getView().findViewById(R.id.c_btnGo).setBackgroundColor(Color.parseColor("#147FD7"));
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
            mHashMap.put(map.addMarker(new MarkerOptions()
                    .position(pos)
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
                if (getView().findViewById(R.id.cd_container).getVisibility() == View.VISIBLE) {
                    listItemClicked(mHashMap.get(marker), true);
                    return false;
                }
                final ListView list = (ListView) getView().findViewById(R.id.c_listEvents);
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        list.smoothScrollToPositionFromTop(mHashMap.get(marker), 0, 500);
                        list.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                list.setSelection(mHashMap.get(marker));
                            }
                        }, 510);
                    }
                });
                return false;
            }
        });
    }

    public void updateViews(ArrayAdapter a) {
        View parent = getView();
        if (parent == null) {
            return; //We switched out of this view.
        }
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
        if (parent.findViewById(R.id.c_btnGo).getVisibility() == View.VISIBLE) {
            getActivity().finish();
            cancelTask();
        } else {
            parent.findViewById(R.id.c_edtZip).setVisibility(View.VISIBLE);
            parent.findViewById(R.id.c_btnRadius).setVisibility(View.VISIBLE);
            parent.findViewById(R.id.c_listEvents).setVisibility(View.VISIBLE);
            parent.findViewById(R.id.c_btnGo).setVisibility(View.VISIBLE);
            parent.findViewById(R.id.cd_container).setVisibility(View.GONE);
        }
    }
}
