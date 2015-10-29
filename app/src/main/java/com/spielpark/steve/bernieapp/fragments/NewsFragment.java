package com.spielpark.steve.bernieapp.fragments;

/**
 * Created by Steve on 7/9/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.actMainPage;
import com.spielpark.steve.bernieapp.misc.ImgTxtAdapter;
import com.spielpark.steve.bernieapp.model.news.NewsArticle;
import com.spielpark.steve.bernieapp.model.news.NewsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {


    private static NewsFragment mIntstance;
    @Bind(R.id.listNews) ListView list;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.txtSubHeader) TextView subHeader;
    @Bind(R.id.txtHeader) TextView header;
    ImgTxtAdapter adapter;
    private Subscription newsSubscription;

    public static NewsFragment getInstance() {
        if (mIntstance == null) {
            mIntstance = new NewsFragment();
            return mIntstance;
        } else {
            return mIntstance;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_newsarticles, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        subHeader.setMovementMethod(new ScrollingMovementMethod());
        adapter = new ImgTxtAdapter(view.getContext(), R.layout.list_news_item, new ArrayList());
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        newsSubscription = NewsManager.get().getNews().observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<List<NewsArticle>>() {
                    @Override
                    public void call(List<NewsArticle> newsArticles) {
                        Collections.sort(newsArticles);
                        adapter.addAll(newsArticles);
                        NewsArticle a;
                        boolean setSubheader = false;
                        for (int i = 0; i < adapter.getCount(); i++) {
                            a = (NewsArticle) adapter.getItem(i);
                            if (!(setSubheader)) {
                                if (a.getPermalink() != null) {
                                    if (a.getPermalink().contains("press-release")) {
                                        subHeader.setText(Html.fromHtml(a.getContent()));
                                        String s = a.getTitle();
                                        s = s.length() > 40 ? s.substring(0, 40) + "..." : s;
                                        header.setText(s);
                                        setSubheader = true;
                                    }
                                }
                            }
                        }

                        ((ImgTxtAdapter) list.getAdapter()).notifyDataSetChanged();
                        list.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ((actMainPage) getActivity()).loadEvent((NewsArticle) list.getAdapter().getItem(position));
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("NewsFragment", "Failed to get news", throwable);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (newsSubscription != null && !newsSubscription.isUnsubscribed())
            newsSubscription.unsubscribe();
    }
}