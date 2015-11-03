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
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String time = mEvent.getPubDate();
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
        ((TextView) root.findViewById(R.id.e_txtDate)).setText(mEvent.getPubDate() + " at " + time);
        ((TextView) root.findViewById(R.id.e_txtDesc)).setText(Html.fromHtml(mEvent.getDesc()));
        ((TextView) root.findViewById(R.id.e_txtDesc)).setMovementMethod(new LinkMovementMethod());
        Util.getPicasso(getActivity()).load(mEvent.getImgSrc()).placeholder(R.drawable.logo).into((ImageView)root.findViewById(R.id.e_imgLogo));
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
