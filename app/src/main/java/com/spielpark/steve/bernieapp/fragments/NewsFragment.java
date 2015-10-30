package com.spielpark.steve.bernieapp.fragments;

/**
 * Created by Steve on 7/9/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
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
import com.spielpark.steve.bernieapp.model.news.Category;
import com.spielpark.steve.bernieapp.model.news.NewsArticle;
import com.spielpark.steve.bernieapp.model.news.NewsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * A placeholder instance containing a simple view.
 */
public class NewsFragment extends Fragment {


    private static NewsFragment instance;
    @Bind(R.id.listNews) ListView list;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.txtSubHeader) TextView subHeader;
    @Bind(R.id.txtHeader) TextView header;
    ImgTxtAdapter adapter;
    NewsArticle headerArticle;
    private Subscription newsSubscription;

    public static NewsFragment getInstance() {
        if (instance == null) {
            instance = new NewsFragment();
            return instance;
        } else {
            return instance;
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

        //Get all of the articles
        Observable<NewsArticle> articles = NewsManager.get().getNews().flatMapIterable(new Func1<List<NewsArticle>, Iterable<NewsArticle>>() {
            @Override
            public Iterable<NewsArticle> call(List<NewsArticle> newsArticles) {
                return newsArticles;
            }
        });

        //Filter out only the first press release article to be used as the header.
        Observable<NewsArticle> firstPressRelease = articles.filter(new Func1<NewsArticle, Boolean>() {
            @Override
            public Boolean call(NewsArticle newsArticle) {
                return newsArticle.getCategories().contains(Category.PressRelease);
            }
        }).first();

        //Remove the header from the list of articles
        newsSubscription = Observable.zip(articles.toList(), firstPressRelease, new Func2<List<NewsArticle>, NewsArticle, Pair<NewsArticle, List<NewsArticle>>>() {
            @Override
            public Pair<NewsArticle, List<NewsArticle>> call(List<NewsArticle> newsArticles, NewsArticle newsArticle) {
                newsArticles.remove(newsArticle);
                return new Pair(newsArticle, newsArticles);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Pair<NewsArticle, List<NewsArticle>>>() {
            @Override
            public void call(Pair<NewsArticle, List<NewsArticle>> pair) {
                //Setup the header
                headerArticle = pair.first;
                subHeader.setText(Html.fromHtml(headerArticle.getContent()));
                String s = headerArticle.getTitle();
                s = s.length() > 40 ? s.substring(0, 40) + "..." : s;
                header.setText(s);

                //Setup the remaining articles
                Collections.sort(pair.second);
                adapter.addAll(pair.second);
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
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (newsSubscription != null && !newsSubscription.isUnsubscribed())
            newsSubscription.unsubscribe();
    }

    @OnClick({R.id.txtSubHeader, R.id.txtHeader})
    void onHeaderClicked() {
        if (headerArticle != null)
            ((actMainPage) getActivity()).loadEvent(headerArticle);
    }
}