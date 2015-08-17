package com.spielpark.steve.bernieapp.wrappers;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Steve on 8/14/2015.
 */
public class NewsArticle<T> implements Comparable<NewsArticle<T>>, ImgTxtItem {
    private String title;
    private String desc;
    private String url;
    private String pubdate;
    private String time;
    private String htmlTitle;
    private Bitmap thumb;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap b) {
        this.thumb = b;
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

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
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
    public Bitmap getImg() {
        return getThumb();
    }

    @Override
    public void setImg(Bitmap img) {
        this.setThumb(img);
    }
    public NewsArticle() {

    }

    @Override
    public int compareTo(NewsArticle<T> tNewsArticle) {
        int ret;
        SimpleDateFormat ft = new SimpleDateFormat("MMMM d, yyyy");
        Date l;
        Date r;
        try {
            l = ft.parse(this.getPubdate());
            r = ft.parse(tNewsArticle.getPubdate());
            ret = r.compareTo(l);
        } catch (ParseException e) {
            e.printStackTrace();
            ret = -1;
        }
        return ret;
    }
}
