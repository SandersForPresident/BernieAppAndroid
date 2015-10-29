package com.spielpark.steve.bernieapp.model.news;

import android.util.Log;

import com.spielpark.steve.bernieapp.model.ApiManager;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class NewsManager {

    private static NewsManager instance;
    private PublishSubject<List<NewsArticle>> newsArticleSubject = PublishSubject.create();

    public static NewsManager get() {
        if (instance == null) {
            instance = new NewsManager();
        }
        return instance;
    }

    public Observable<List<NewsArticle>> getNews() {
        getNewsFromApi();
        return newsArticleSubject.asObservable();
    }

    private void getNewsFromApi() {
        ApiManager.get().api.getNews()
                .cache()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<NewsArticle>>() {
                    @Override
                    public void call(List<NewsArticle> newsArticles) {
                        newsArticleSubject.onNext(newsArticles);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("NewsManager", "getNewsFromApi Failed.", throwable);
                    }
                });
    }
}

