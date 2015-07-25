package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Event;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Steve on 7/8/2015.
 */
public class NewsTask extends AsyncTask {
    private static ArrayList<Event> events;
    private static ListView list;
    private static ProgressBar progressBar;
    private static Context ctx;
    public NewsTask(Context ctx, ListView listView, ProgressBar progressBar) {
        this.list = listView;
        this.ctx = ctx;
        this.progressBar = progressBar;
    }

    public static Event getEvent(int pos) {
        return events.get(pos);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        events = new ArrayList<>();
        BufferedReader xml = null;
        try {
            URL url = new URL("https://berniesanders.com/feed/");
            xml = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (xml == null) {
            Event e = new Event();
            e.setName("Unable to Load News");
            e.setDescription("Check your internet connection?");
            events.add(e);
            super.onCancelled();
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
        Collections.sort(events);
        String[] titles = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            titles[i] = getHTMLForTitle(events.get(i));
        }
        NewsAdapter adapter = new NewsAdapter(ctx, R.layout.list_news_item, R.id.txtItem, titles);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void formatDate(Event e) {
        SimpleDateFormat ft;
        if (e.isrss) {
            ft = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        } else {
            ft = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            Date date = ft.parse(e.getDate());
            e.setDate(new SimpleDateFormat("MMMM d, yyyy").format(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    private String getHTMLForTitle(Event e) {
        StringBuilder bld = new StringBuilder();
        bld.append("<big>").append(e.getName()).append("</big><br>");
        bld.append("<font color=\"#FF2222\">&emsp;").append(e.getDate());
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
        Event e = new Event();
        e.isrss = true;
        int type = in.next();
        while (!(type == XmlPullParser.END_TAG && in.getName().equals("item"))) {
            if (type == XmlPullParser.START_TAG) {
                String name = in.getName();
                if (name.equals("title")) {
                    e.setName(in.nextText());
                } else if (name.equals("link")) {
                    e.setUrl(in.nextText());
                } else if (name.equals("pubDate")) {
                    String t = (in.nextText());
                    String time = t.substring(t.indexOf(':') - 2, t.lastIndexOf(':') +2);
                    e.setTime(time);
                    e.setDate(t);
                } else if (name.equals("description")) {
                    e.setDescription(in.nextText());
                }
            }
            type = in.next();
        }
        formatDate(e);
        events.add(e);
    }

    private class NewsAdapter extends ArrayAdapter {

        private NewsAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public Object getItem(int position) {
            return Html.fromHtml( (String) super.getItem(position));
        }

    }
}
