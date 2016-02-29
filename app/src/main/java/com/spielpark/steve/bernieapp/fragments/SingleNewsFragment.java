package com.spielpark.steve.bernieapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.misc.Util;
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;

public class SingleNewsFragment extends Fragment {
    private static NewsArticle mEvent;
    private static SingleNewsFragment mInstance;
    private ShareActionProvider shareActionProvider;

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
        View root = getView();
        ((TextView) root.findViewById(R.id.e_txtTitle)).setText(mEvent.getTitle());
        ((TextView) root.findViewById(R.id.e_txtTitle)).setShadowLayer(13, 0, 0, Color.BLACK);
        ((TextView) root.findViewById(R.id.e_txtDate)).setText(mEvent.getPubDate());
        ((TextView) root.findViewById(R.id.e_txtDesc)).setText(Html.fromHtml(mEvent.getDesc()));
        ((TextView) root.findViewById(R.id.e_txtDesc)).setMovementMethod(new LinkMovementMethod());
        Util.getPicasso(getActivity()).load(mEvent.getImgSrc()).placeholder(R.drawable.logo).fit().into((ImageView)root.findViewById(R.id.e_imgLogo));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareActionProvider.setShareIntent(getShareIntent());
        shareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder text = new StringBuilder("Hey, check out this article on Bernie Sanders!\n");
        text.append(mEvent.getTitle());
        text.append("\n\nRead more here: " + mEvent.getUrl());
        text.append("\n\nSent from Bernie Sanders for President 2016 Android Application");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text.toString());
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_event, container, false);
    }
}
