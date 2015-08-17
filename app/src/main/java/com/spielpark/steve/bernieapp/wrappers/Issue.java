package com.spielpark.steve.bernieapp.wrappers;

import android.graphics.Bitmap;

/**
 * Created by Steve on 7/9/2015.
 */
public class Issue implements ImgTxtItem {
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

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String videos) {
        this.video = videos;
    }


    private String pubDate;
    private String htmlTitle;
    private String desc;
    private Bitmap thumb;
    private String Url;
    private String title;
    private String video;
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
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
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
    public Bitmap getImg() {
        return getThumb();
    }

    @Override
    public void setImg(Bitmap img) {
        this.setThumb(img);
    }
}
