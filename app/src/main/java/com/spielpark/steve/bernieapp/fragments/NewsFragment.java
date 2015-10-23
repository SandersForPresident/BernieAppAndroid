package com.spielpark.steve.bernieapp.fragments;

/**
 * Created by Steve on 7/9/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.model.news.NewsArticle;
import com.spielpark.steve.bernieapp.model.news.NewsManager;
import com.spielpark.steve.bernieapp.tasks.NewsTask;

import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {


    private static NewsFragment mIntstance;
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
        final ListView newsList = (ListView) view.findViewById(R.id.listNews);
        ((TextView) view.findViewById(R.id.txtSubHeader)).setMovementMethod(
                new ScrollingMovementMethod());


        new NewsTask(getActivity(), newsList, (ProgressBar) view.findViewById(R.id.progressBar),
                (TextView) view.findViewById(R.id.txtSubHeader),
                (TextView) view.findViewById(R.id.txtHeader)).execute();

        NewsManager.get().getNews().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<NewsArticle>>() {
            @Override
            public void call(List<NewsArticle> newsArticles) {
                Collections.sort(newsArticles);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("NewsFragment", "Failed to get news", throwable);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        newsSubscription = NewsManager.get().getNews().observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<List<NewsArticle>>() {
                    @Override
                    public void call(List<NewsArticle> newsArticles) {
                        Collections.sort(newsArticles);
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
        if (!newsSubscription.isUnsubscribed())
            newsSubscription.unsubscribe();
    }
}