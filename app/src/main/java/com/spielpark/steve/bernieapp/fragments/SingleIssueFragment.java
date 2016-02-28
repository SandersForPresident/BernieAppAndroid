package com.spielpark.steve.bernieapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Issue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleIssueFragment extends Fragment {
    private ShareActionProvider shareActionProvider;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIssue == null) {
            //User exited the app and returned to it, but android cleared some stuff from memory...
            getActivity().getSupportFragmentManager().popBackStack("base", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        final View root = getView();
        ((TextView) root.findViewById(R.id.i_txtTitle)).setText(mIssue.getTitle());
        ((TextView) root.findViewById(R.id.i_txtTitle)).setShadowLayer(13, 0, 0, Color.BLACK);
        ((TextView) root.findViewById(R.id.i_txtDate)).setText("Published " + mIssue.getPubDate());
        ((TextView) root.findViewById(R.id.i_txtDesc)).setText(Html.fromHtml(mIssue.getDesc()));
        ((TextView) root.findViewById(R.id.i_txtDesc)).setMovementMethod(new LinkMovementMethod());
        WebView wv = ((WebView) root.findViewById(R.id.i_video));
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadDataWithBaseURL(null, mIssue.getEmbedURL(getActivity()), "text/Html", "UTF-8", null);
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
                getActivity().onBackPressed();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder text = new StringBuilder("Hey, here's Bernie Sanders' official stance on this issue -- \n");
        text.append(mIssue.getTitle());
        text.append("\n\nRead more here: " + mIssue.getUrl());
        text.append("\n\nSent from Bernie Sanders for President 2016 Android Application");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text.toString());
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_single_issue, container, false);
    }


}
