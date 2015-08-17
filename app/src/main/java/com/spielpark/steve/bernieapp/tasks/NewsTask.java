package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.misc.ImgTxtAdapter;
import com.spielpark.steve.bernieapp.misc.Util;
import com.spielpark.steve.bernieapp.wrappers.Event;
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Steve on 7/8/2015.
 */
public class NewsTask extends AsyncTask {
    private static ArrayList<NewsArticle> articles;
    private static ListView list;
    private static ProgressBar progressBar;
    private static Context ctx;
    private static boolean demdaily = true;

    public NewsTask(Context ctx, ListView listView, ProgressBar progressBar, boolean demdaily) {
        this.list = listView;
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.demdaily = demdaily;
    }

    public static NewsArticle getArticle(int pos) {
        return articles.get(pos);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        articles = new ArrayList<>();
        BufferedReader xml = null;
        try {
            URL url = new URL(demdaily ? "https://berniesanders.com/feed/" : "https://berniesanders.com/press-release/feed/");
            xml = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (xml == null) {
            NewsArticle a = new NewsArticle();
            a.setTitle("Unable to Load News");
            a.setDesc("Check your internet connection?");
            articles.add(a);
            return null;
        }
        XmlPullParser xmlReader = Xml.newPullParser();
        try {
            xmlReader.setInput(xml);
            readXml(xmlReader);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Collections.sort(articles);
        NewsArticle a;
        for (int i = 0; i < articles.size(); i++) {
            a = articles.get(i);
            a.setHtmlTitle(getHTMLForTitle(a));
        }
        //new FetchNewsThumbs().execute();
        ImgTxtAdapter adapter = new ImgTxtAdapter(ctx, R.layout.list_news_item, articles);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void formatDate(NewsArticle e) {
        SimpleDateFormat ft;
        ft = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            Date date = ft.parse(e.getPubdate());
            e.setPubdate(new SimpleDateFormat("MMMM d, yyyy").format(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    private String getHTMLForTitle(NewsArticle e) {
        StringBuilder bld = new StringBuilder();
        String title = e.getTitle();
        if (title.length() > 40) {
            title = title.substring(0, 40);
            title = title.substring(0, Math.min(title.length(), title.lastIndexOf(' '))).concat("...");
        }
        bld.append("<big>").append(title).append("</big><br>");
        bld.append("<font color=\"#FF2222\">").append(e.getPubdate());
        return bld.toString();
    }

    private void readXml(XmlPullParser in) throws XmlPullParserException, IOException {

        int type = in.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG && in.getName().equals("item")) {
                readItem(in);
            }
            type = in.next();
        }
    }

    private void readItem(XmlPullParser in) throws XmlPullParserException, IOException {
        NewsArticle a = new NewsArticle();
        int type = in.next();
        while (!(type == XmlPullParser.END_TAG && in.getName().equals("item"))) {
            if (type == XmlPullParser.START_TAG) {
                String name = in.getName();
                if (name.equals("title")) {
                    a.setTitle(in.nextText());
                } else if (name.equals("link")) {
                    a.setUrl(in.nextText());
                } else if (name.equals("pubDate")) {
                    String t = (in.nextText());
                    String time = t.substring(t.indexOf(':') - 2, t.lastIndexOf(':') +2);
                    a.setTime(time);
                    a.setPubdate(t);
                } else if (name.equals("description")) {
                    a.setDesc(in.nextText());
                }
            }
            type = in.next();
        }
        formatDate(a);
        articles.add(a);
    }

    private static class FetchNewsThumbs extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ((ImgTxtAdapter) NewsTask.list.getAdapter()).notifyDataSetChanged();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            for (NewsArticle a : articles) {
                a.setThumb(Util.getOGImage(a.getUrl(), NewsTask.ctx, true));
            }
            return null;
        }
    }
}
