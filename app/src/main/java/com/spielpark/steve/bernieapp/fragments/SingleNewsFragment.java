package com.spielpark.steve.bernieapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.misc.Util;
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleNewsFragment extends Fragment {
    private static NewsArticle mEvent;
    private static SingleNewsFragment mInstance;

    public static SingleNewsFragment getInstance(NewsArticle e) {
        mEvent = e;
        if (mInstance == null) {
            mInstance = new SingleNewsFragment();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String time = mEvent.getTime();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final Date dateObj = sdf.parse(time);
            time = new SimpleDateFormat("h:mm a, z").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        View root = getView();
        ((TextView) root.findViewById(R.id.e_txtTitle)).setText(mEvent.getTitle());
        ((TextView) root.findViewById(R.id.e_txtTitle)).setShadowLayer(13, 0, 0, Color.BLACK);
        ((TextView) root.findViewById(R.id.e_txtDate)).setText(mEvent.getPubdate() + " at " + time);
        ((TextView) root.findViewById(R.id.e_txtDesc)).setText(Html.fromHtml(mEvent.getDesc()));
        ((TextView) root.findViewById(R.id.e_txtDesc)).setMovementMethod(new LinkMovementMethod());
        new getNewsPicTask(root.findViewById(R.id.e_imgLogo), mEvent.getUrl(), getActivity()).execute();
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

    private static class getNewsPicTask extends AsyncTask {
        private static ImageView view;
        private static String url;
        private static Context ctx;
        private static Bitmap bmp;

        public getNewsPicTask (View v, String url, Context ctx) {
            view = (ImageView) v;
            this.url = url;
            this.ctx = ctx;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            bmp =  Util.getOGImage(url, ctx, false);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            view.setImageBitmap(bmp);
        }
    }
}
