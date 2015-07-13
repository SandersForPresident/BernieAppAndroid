package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Event;
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
import java.util.Locale;

/**
 * Created by Steve on 7/9/2015.
 */
public class IssuesTask extends AsyncTask {
    private static ArrayList<Issue> issues;
    private static ListView list;
    private static ProgressBar progressBar;
    private static Context ctx;
    public IssuesTask(Context ctx, ListView listView, ProgressBar progressBar) {
        this.list = listView;
        this.ctx = ctx;
        this.progressBar = progressBar;
    }

    public static Issue getIssue(int position) {
        return issues.get(position);
    }

    private String getHTMLForTitle(Issue i) {
        SimpleDateFormat ft = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        String time = "";
        try {
            final Date dateObj = ft.parse(i.getPubDate());
            time = new SimpleDateFormat("EEE, d MMM yyyy").format(dateObj);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
            time = " date not available.";
        }
        StringBuilder bld = new StringBuilder();
        bld.append("<big>").append(i.getTitle()).append("</big><br>");
        bld.append("<font color=\"#FF2222\">&emsp; Published on ").append(time).append("</font>");
        return bld.toString();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("OPE", "There are " + issues.size() + " events.");
        String[] titles = new String[issues.size()];
        for (int i = 0; i < issues.size(); i++) {
            titles[i] = getHTMLForTitle(issues.get(i));
        }
        IssuesAdapter adapter = new IssuesAdapter(ctx, R.layout.list_news_item, R.id.txtItem, titles);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected Object doInBackground(Object[] params) {
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
            i.setTitle("Unable to Load News");
            i.setDesc("Check your internet connection?");
            issues.add(i);
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
                } else if (name.equals("description")) {
                    i.setDesc(in.nextText());
                }
            }
            type = in.next();
        }
        issues.add(i);
        Log.d("New Event", i.toString());
    }

    private class IssuesAdapter extends ArrayAdapter {

        private IssuesAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public Object getItem(int position) {
            return Html.fromHtml((String) super.getItem(position));
        }

    }

}
