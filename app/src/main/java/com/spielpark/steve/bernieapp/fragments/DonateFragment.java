package com.spielpark.steve.bernieapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spielpark.steve.bernieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Steve on 2/28/2016.
 */
public class DonateFragment extends Fragment{
    private static DonateFragment mInstance;

    public static DonateFragment getInstance() {
        if (mInstance == null) {
            mInstance = new DonateFragment();
        }
        return mInstance;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView thermometer = ((ImageView) view.findViewById(R.id.d_imgThermometer));
        Picasso.with(getContext()).invalidate("https://secure.actblue.com/x/object/actblue-badges/page/reddit-for-bernie/thermometer/dark.png");
        Picasso.with(getContext()).load("https://secure.actblue.com/x/object/actblue-badges/page/reddit-for-bernie/thermometer/dark.png").into(thermometer);
        view.findViewById(R.id.d_btnDonate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.donate_url)));
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_donate, container, false);
    }
}
