package com.spielpark.steve.bernieapp.model.news;

import com.spielpark.steve.bernieapp.model.ApiManager;
import com.spielpark.steve.bernieapp.model.BernieApi;

import java.util.List;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class NewsManager {

    private static NewsManager instance;
    private final BernieApi api;

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
        return Observable.zip(api.getNews(BernieApi.NEWS), api.getNews(BernieApi.DAILY), new Func2<List<NewsArticle>, List<NewsArticle>, List<NewsArticle>>() {
            @Override
            public List<NewsArticle> call(List<NewsArticle> newsArticles, List<NewsArticle> newsArticles2) {
                newsArticles.addAll(newsArticles2);
                return newsArticles;
            }
        }).cache().subscribeOn(Schedulers.io());
    }
}

