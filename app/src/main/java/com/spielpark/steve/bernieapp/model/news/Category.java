package com.spielpark.steve.bernieapp.model.news;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Patrick on 10/29/15.
 */
public enum Category {
    @SerializedName("On the Road")
    OnTheRoad,
    @SerializedName("News")
    News,
    @SerializedName("Press Release")
    PressRelease
}
