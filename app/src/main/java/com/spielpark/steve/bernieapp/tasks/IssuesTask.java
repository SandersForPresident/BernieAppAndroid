package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.misc.ImgTxtAdapter;
import com.spielpark.steve.bernieapp.misc.Util;
import com.spielpark.steve.bernieapp.wrappers.Event;
import com.spielpark.steve.bernieapp.wrappers.ImgTxtItem;
import com.spielpark.steve.bernieapp.wrappers.Issue;

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
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Steve on 7/9/2015.
 */
public class IssuesTask extends AsyncTask {
    private static ArrayList<Issue> issues;
    private static ListView list;
    private static ProgressBar progressBar;
    private static Context ctx;
    private static HashMap<String, String> vidLinks;
    public IssuesTask(Context ctx, ListView listView, ProgressBar progressBar) {
        this.list = listView;
        this.ctx = ctx;
        this.progressBar = progressBar;
    }
    public static void clear() {
        issues = null;
        ctx = null;
        list = null;
        progressBar = null;
        vidLinks = null;
    }
    public static Issue getIssue(int position) {
        return issues.get(position);
    }

    private String getHTMLForTitle(Issue i) {
        SimpleDateFormat ft = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        String time;
        try {
            final Date dateObj = ft.parse(i.getPubDate());
            time = new SimpleDateFormat("EEE, d MMM yyyy").format(dateObj);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
            time = " date not available.";
        }
        StringBuilder bld = new StringBuilder();
        bld.append("<big>").append(i.getTitle()).append("</big><br>");
        bld.append("<font color=\"#FF2222\">Published on ").append(time).append("</font>");
        return bld.toString();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        for (Issue i : issues) {
            i.setVideo(vidLinks.get(i.getTitle().toLowerCase()));
            Log.d("Success!", i.getTitle() + "..." + i.getVideo());
        }
        new FetchThumbsTask().execute();
        ImgTxtAdapter adapter = new ImgTxtAdapter(ctx, R.layout.list_news_item, issues);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        retrieveLinks();
        issues = new ArrayList<>();
        BufferedReader xml = null;
        try {
            URL url = new URL("https://berniesanders.com/issues/feed/");
            xml = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (xml == null) {
            Issue i = new Issue();
            i.setHtmlTitle("Unable to Load News");
            i.setDesc("Check your internet connection?");
            issues.add(i);
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

    private void retrieveLinks() {
        BufferedReader in = null;
        vidLinks = new HashMap<>();
        try {
            URL url = new URL("https://www.reddit.com/r/bernienews.json");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in == null) {
            Log.d("rss reader null", "null reader. Is Reddit down?!?!");
        }
        JsonReader reader = new JsonReader(in);
        try {
            readObjects(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObjects(JsonReader reader) throws IOException {
        Log.d("JsonReader", "Beginning parsing");
        String title = "";
        String link = "";
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
            String next = reader.nextName();
            switch(next.toLowerCase().trim()) {
                case "children" : {
                    reader.beginArray();
                    reader.beginObject();
                    break;
                }
                case "data" : {
                    reader.beginObject();
                    break;
                }
                case "title" : {
                    title = reader.nextString();
                    break;
                }
                case "url" : {
                    link = reader.nextString();
                    break;
                }
                case "ups": {
                    reader.skipValue();
                    reader.endObject();
                    reader.endObject();
                    vidLinks.put(title.toLowerCase(), link.substring(link.lastIndexOf('=') + 1));
                    break;
                }
                default: {
                    reader.skipValue();
                }
            }
        }
        reader.close();
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
        Issue i = new Issue();
        int type = in.next();
        while (!(type == XmlPullParser.END_TAG && in.getName().equals("item"))) {
            if (type == XmlPullParser.START_TAG) {
                String name = in.getName();
                if (name.equals("title")) {
                    i.setTitle(in.nextText());
                } else if (name.equals("link")) {
                    i.setUrl(in.getText());
                } else if (name.equals("pubDate")) {
                    String t = (in.nextText());
                    i.setPubDate(t);
                    i.setHtmlTitle(getHTMLForTitle(i));
                } else if (name.equals("description")) {
                    i.setDesc(in.nextText());
                }
            }
            type = in.next();
        }
        issues.add(i);
    }

    private class FetchThumbsTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            StringBuilder bld = new StringBuilder("https://img.youtube.com/vi/");
            for (Issue i : issues) {
                if (i.getVideo() == null) {
                    continue;
                }
                bld.append(i.getVideo());
                bld.append("/default.jpg");
                i.setThumb(Util.getBitmapFromURL(bld.toString()));
                bld = new StringBuilder("http://img.youtube.com/vi/");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ((ImgTxtAdapter) list.getAdapter()).notifyDataSetChanged();
        }
    }

}
