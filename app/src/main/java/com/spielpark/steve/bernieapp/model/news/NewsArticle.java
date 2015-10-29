package com.spielpark.steve.bernieapp.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.spielpark.steve.bernieapp.model.ImgTxtItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static String NULL_IMAGE = "https://s.bsd.net/bernie16/main/page/-/website/fb-share.png";
    private String title;
    private String content;
    private String permalink;
    private String date;
    private String time;
    private String imgSrc;

    public NewsArticle() {

    }

    protected NewsArticle(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.permalink = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.imgSrc = in.readString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        return imgSrc == null ? NULL_IMAGE : imgSrc;
    }

    @Override
    public void setImgSrc(String img) {
        this.imgSrc = img;
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
        dest.writeString(this.time);
        dest.writeString(this.imgSrc);
    }
}
