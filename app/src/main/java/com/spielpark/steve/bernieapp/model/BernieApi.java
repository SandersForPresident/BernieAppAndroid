package com.spielpark.steve.bernieapp.model;

import com.spielpark.steve.bernieapp.model.news.NewsArticle;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface BernieApi {

    @GET("/_search?q=!article_type%3A%28ExternalLink%20OR%20Issues%29&sort=created_at:desc&size=20")
    Observable<List<NewsArticle>> getNews();
}
