package com.spielpark.steve.bernieapp.model.news;

import android.util.Pair;

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

    public Observable<Pair<NewsArticle, List<NewsArticle>>> getNews() {
        return Observable.zip(api.getNews(BernieApi.NEWS), api.getNews(BernieApi.DAILY), new Func2<List<NewsArticle>, List<NewsArticle>, Pair<NewsArticle, List<NewsArticle>>>() {
            @Override
            public Pair<NewsArticle, List<NewsArticle>> call(List<NewsArticle> newsArticles, List<NewsArticle> newsArticles2) {
                newsArticles.addAll(newsArticles2);
                //Filter out only the first press release article to be used as the header.
                NewsArticle pressRelease = null;
                for (NewsArticle newsArticle : newsArticles) {
                    if (newsArticle.getCategories().contains(Category.PressRelease)) {
                        pressRelease = newsArticle;
                        break;
                    }
                }
                //Remove the header from the list of articles
                if (pressRelease != null) {
                    newsArticles.remove(pressRelease);
                }

                return new Pair<>(pressRelease, newsArticles);
            }
        }).cache().subscribeOn(Schedulers.io());
    }
}

