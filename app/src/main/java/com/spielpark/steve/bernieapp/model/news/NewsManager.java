package com.spielpark.steve.bernieapp.model.news;

import android.util.Log;

import com.spielpark.steve.bernieapp.model.ApiManager;
import com.spielpark.steve.bernieapp.model.BernieApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class NewsManager {

    private static NewsManager instance;
    private final BernieApi api;
    private PublishSubject<List<NewsArticle>> newsArticleSubject = PublishSubject.create();

    public NewsManager() {
        api = ApiManager.get().api;

    }

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
        Observable.merge(api.getNews(BernieApi.NEWS), api.getNews(BernieApi.DAILY))
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

