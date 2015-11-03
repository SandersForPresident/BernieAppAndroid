package com.spielpark.steve.bernieapp.model;

import com.spielpark.steve.bernieapp.model.news.NewsArticle;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface BernieApi {
    String NEWS = "news";
    String DAILY = "daily";

    @GET("?json=true&limit=12")
    Observable<List<NewsArticle>> getNews(@Query("which") String newsType);
}
