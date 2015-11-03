package com.spielpark.steve.bernieapp.model;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

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

            LoggingInterceptor interceptor = new LoggingInterceptor();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://berniesanders.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            retrofit.client().interceptors().add(interceptor);
            instance = new ApiManager(retrofit.create(BernieApi.class));

        }
        return instance;
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d("Retrofit", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d("Retrofit", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));


            final String responseString = new String(response.body().bytes());

            Log.d("Retrofit", "Response: " + responseString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }
}
