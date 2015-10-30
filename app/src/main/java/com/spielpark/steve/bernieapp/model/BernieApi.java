package com.spielpark.steve.bernieapp.model;

import com.spielpark.steve.bernieapp.model.news.NewsArticle;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface BernieApi {
    String NEWS = "news";
    String DAILY = "daily";

    @GET("https://berniesanders.com/?json=true&which={which}&limit=12")
    Observable<List<NewsArticle>> getNews(@Path("which") String newsType);
}
