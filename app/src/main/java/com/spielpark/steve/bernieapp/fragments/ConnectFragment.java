package com.spielpark.steve.bernieapp.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.actMainPage;
import com.spielpark.steve.bernieapp.tasks.ConnectTask;
import com.spielpark.steve.bernieapp.wrappers.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends Fragment {
    private static GoogleMap map;
    private int mZip = 0;
    private int mRadius = 50;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((Button) getView().findViewById(R.id.c_btnRadius)).setOnClickListener(new View.OnClickListener() {
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
        ((Button) getView().findViewById(R.id.c_btnGo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
            }
        });
        setUpMap();
        super.onViewCreated(view, savedInstanceState);
    }

    private void startTask() {
        ListView list = (ListView) getView().findViewById(R.id.c_listEvents);
        ProgressBar prog = (ProgressBar) getView().findViewById(R.id.c_progress);
        mZip = Integer.parseInt(((EditText) getView().findViewById(R.id.c_edtZip)).getText().toString());
        new ConnectTask(getActivity(), list, prog, getView().findViewById(R.id.c_mapContainer), mZip, mRadius).execute();
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

    public static void setMarkers() {
        ArrayList<Event> events = ConnectTask.getEvents();
        LatLngBounds.Builder bld = new LatLngBounds.Builder();
        List<Marker> markers = new ArrayList<>(events.size());
        LatLng pos;
        for (Event e : ConnectTask.getEvents()) {
            pos = new LatLng(e.getLatitude(), e.getLongitude());
            markers.add(map.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(e.getName())));
            bld.include(pos);
        }
        LatLngBounds bounds = bld.build();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("Marker Clicked", marker.getId());
                return true;
            }
        });
    }
}
