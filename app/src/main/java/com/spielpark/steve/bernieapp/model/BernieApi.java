package com.spielpark.steve.bernieapp.model;

import com.spielpark.steve.bernieapp.model.news.NewsArticle;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface BernieApi {

    @GET("https://berniesanders.com/?json=true&which=news&limit=12")
    Observable<List<NewsArticle>> getNews();
}
