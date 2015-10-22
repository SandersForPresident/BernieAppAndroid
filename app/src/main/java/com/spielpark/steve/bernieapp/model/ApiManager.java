package com.spielpark.steve.bernieapp.model;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class ApiManager {

    private static ApiManager instance;
    public BernieApi api;

    public ApiManager(BernieApi api) {
        this.api = api;
    }

    public static ApiManager get() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl("https://search.berniesanders.tech/articles_en/berniesanders_com")
                    .build();
            instance = new ApiManager(retrofit.create(BernieApi.class));
        }
        return instance;
    }
}
