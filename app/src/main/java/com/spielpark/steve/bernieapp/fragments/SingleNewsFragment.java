package com.spielpark.steve.bernieapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.spielpark.steve.bernieapp.model.news.NewsArticle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingleNewsFragment extends Fragment {
    private static final String NEW_ARTICLE = "NEW_ARTICLE";
    @Bind(R.id.e_txtTitle) TextView title;
    @Bind(R.id.e_txtDate) TextView date;
    @Bind(R.id.e_txtDesc) TextView description;
    @Bind(R.id.e_imgLogo) ImageView logo;
    private NewsArticle event;

    public static SingleNewsFragment getInstance(NewsArticle newsArticle) {
        SingleNewsFragment fragment = new SingleNewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(NEW_ARTICLE, newsArticle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            event = args.getParcelable(NEW_ARTICLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_event, container, false);
        ButterKnife.bind(this, view);
        title.setText(event.getTitle());
        title.setShadowLayer(13, 0, 0, Color.BLACK);
        date.setText(event.getPubDate());
        description.setText(Html.fromHtml(event.getDesc()));
        description.setMovementMethod(new LinkMovementMethod());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.getPicasso(getActivity()).load(event.getImgSrc()).placeholder(R.drawable.logo).into(logo);
    }

    @OnClick(R.id.e_btnWebsite)
    void onWebsiteClicked() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(event.getUrl()));
        startActivity(i);
    }
}
