package com.spielpark.steve.bernieapp.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.spielpark.steve.bernieapp.model.ImgTxtItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

<<<<<<<Updated upstream:app/src/main/java/com/spielpark/steve/bernieapp/wrappers/NewsArticle.java

        =======
        >>>>>>>Stashed changes:app/src/main/java/com/spielpark/steve/bernieapp/model/news/NewsArticle.java

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
    private String title;
    private String desc;
    private String url;
    private String pubdate;
    private String time;
    private String htmlTitle;
    private String imgSrc;

    public NewsArticle() {

    }

    protected NewsArticle(Parcel in) {
        this.title = in.readString();
        this.desc = in.readString();
        this.url = in.readString();
        this.pubdate = in.readString();
        this.time = in.readString();
        this.htmlTitle = in.readString();
        this.imgSrc = in.readString();
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPubDate() {
        return pubdate;
    }

    public void setPubDate(String pubdate) {
        this.pubdate = pubdate;
    }

    @Override
    public String getTxt() {
        return getHtmlTitle();
    }

    @Override
    public void setTxt(String txt) {
        this.setHtmlTitle(txt);
    }

    @Override
    public String getImgSrc() {
        return imgSrc;
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
        dest.writeString(this.desc);
        dest.writeString(this.url);
        dest.writeString(this.pubdate);
        dest.writeString(this.time);
        dest.writeString(this.htmlTitle);
        dest.writeString(this.imgSrc);
    }
}
