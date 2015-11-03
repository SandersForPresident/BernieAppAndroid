package com.spielpark.steve.bernieapp.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.spielpark.steve.bernieapp.model.ImgTxtItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Steve on 8/14/2015.
 */
public class NewsArticle<T> implements Comparable<NewsArticle<T>>, ImgTxtItem, Parcelable {
    public static final Parcelable.Creator<NewsArticle> CREATOR =
            new Parcelable.Creator<NewsArticle>() {
                public NewsArticle createFromParcel(Parcel source) {
                    return new NewsArticle(source);
                }

                public NewsArticle[] newArray(int size) {
                    return new NewsArticle[size];
                }
            };
    private static final String NULL_IMAGE = "https://s.bsd.net/bernie16/main/page/-/website/fb-share.png";
    private String title;
    private String content;
    private String permalink;
    private String date;
    @SerializedName("og_image")
    private String image;
    private List<Category> categories;

    public NewsArticle() {

    }

    public NewsArticle(String title, String content) {
        this.title = title;
        this.content = content;
    }

    protected NewsArticle(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.permalink = in.readString();
        this.date = in.readString();
        this.image = in.readString();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPubDate() {
        return date;
    }

    public void setPubDate(String pubdate) {
        this.date = pubdate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String getTxt() {
        return getTitle();
    }

    @Override
    public void setTxt(String txt) {
        this.setTitle(txt);
    }

    @Override
    public String getImgSrc() {
        return image == null ? NULL_IMAGE : image;
    }

    @Override
    public void setImgSrc(String img) {
        this.image = img;
    }

    @Override
    public int compareTo(NewsArticle<T> tNewsArticle) {
        int ret;
        SimpleDateFormat ft = new SimpleDateFormat("MMMM d, yyyy");
        Date l;
        Date r;
        try {
            r = ft.parse(this.getPubDate());
            l = ft.parse(tNewsArticle.getPubDate());
            ret = l.compareTo(r);
        } catch (ParseException e) {
            e.printStackTrace();
            ret = -1;
        }
        return ret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.permalink);
        dest.writeString(this.date);
        dest.writeString(this.image);
    }
}
