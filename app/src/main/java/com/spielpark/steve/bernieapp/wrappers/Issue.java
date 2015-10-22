package com.spielpark.steve.bernieapp.wrappers;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.spielpark.steve.bernieapp.misc.Util;

/**
 * Created by Steve on 7/9/2015.
 */
public class Issue implements ImgTxtItem, Parcelable {

    public static final Parcelable.Creator<Issue> CREATOR = new Parcelable.Creator<Issue>() {
        public Issue createFromParcel(Parcel source) {
            return new Issue(source);
        }

        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
    private String pubDate;
    private String htmlTitle;
    private String desc;
    private String url;
    private String title;
    private String video;
    private String imgSrc;

    public Issue() {
    }

    protected Issue(Parcel in) {
        this.pubDate = in.readString();
        this.htmlTitle = in.readString();
        this.desc = in.readString();
        this.url = in.readString();
        this.title = in.readString();
        this.video = in.readString();
        this.imgSrc = in.readString();
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String title) {
        this.htmlTitle = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String videos) {
        this.video = videos;
    }

    public String getEmbedURL(Activity ctx) {
        StringBuilder bld = new StringBuilder();
        int[] wh = Util.getScreenWidthHeight(ctx);
        bld.append("<body style=\"margin: 0; padding: 0\"> <iframe width=\"");
        bld.append(wh[0] + 24);
        bld.append("\" height=\"");
        bld.append(wh[1]);
        bld.append("\" src=\"https://www.youtube.com/embed/");
        bld.append(this.getVideo());
        bld.append("\" frameborder=\"0\" allowfullscreen></iframe></body>");
        Log.d("Issue URL", bld.toString());
        return bld.toString();
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        imgSrc = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pubDate);
        dest.writeString(this.htmlTitle);
        dest.writeString(this.desc);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.video);
        dest.writeString(this.imgSrc);
    }
}
