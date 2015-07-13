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
        BufferedReader in = null;
        BufferedReader xml = null;
        try {
            URL url = new URL("https://go.berniesanders.com/page/event/search_results?orderby=date&format=json");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            url = new URL("https://berniesanders.com/feed/");
            xml = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in == null || xml == null) {
            Event e = new Event();
            e.setName("Unable to Load News");
            e.setDescription("Check your internet connection?");
            events.add(e);
            super.onCancelled();
            return null;
        }
        JsonReader reader = new JsonReader(in);
        XmlPullParser xmlReader = Xml.newPullParser();
        try {
            xmlReader.setInput(xml);
            readXml(xmlReader);
            readObjects(reader);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("OPE", "There are " + events.size() + " events.");
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
        if (! (e.isrss)) {
            bld.append("<font color=\"#FF2222\">&emsp;").append(e.getVenue_city()).append(", ").append(e.getState()).append("</font>");
        } else {
            bld.append("<font color=\"#FF2222\">&emsp;").append(e.getDate());
        }
        return bld.toString();
    }

    private void readObjects(JsonReader reader) throws IOException {
        Event e = new Event();
        reader.beginObject();
        while (reader.hasNext()) {
            String next = reader.nextName();
            switch(next.toLowerCase().trim()) {
                case "results" : {
                    reader.beginArray();
                    reader.beginObject();
                    break;
                }
                case "name" : {
                    e.setName(reader.nextString());
                    break;
                }
                case "start_day" : {
                    e.setDate(reader.nextString());
                    break;
                }
                case "start_time" : {
                    e.setTime(reader.nextString());
                    break;
                }
                case "url" : {
                    e.setUrl(reader.nextString().replaceAll("\\\\", ""));
                    break;
                }
                case "timezone" : {
                    e.setTimezone(reader.nextString());
                    break;
                }
                case "description" : {
                    e.setDescription(reader.nextString());
                    break;
                }
                case "event_type_name" : {
                    e.setEventType(reader.nextString());
                    break;
                }
                case "venue_name" : {
                    e.setVenue(reader.nextString());
                    break;
                }
                case "venue_state_cd" : {
                    e.setState(reader.nextString());
                    break;
                }
                case "venue_addr1" : {
                    e.setVenue_addr(reader.nextString());
                    break;
                }
                case "venue_city" : {
                    e.setVenue_city(reader.nextString());
                    break;
                }
                case "venue_zip" : {
                    e.setZip(reader.nextInt());
                    break;
                }
                case "capacity" : {
                    e.setCapacity(reader.nextInt());
                    break;
                }
                case "latitude" : {
                    e.setLatitude(reader.nextDouble());
                    break;
                }
                case "longitude" : {
                    e.setLongitude(reader.nextDouble());
                    break;
                }
                case "is_official" : {
                    e.setOfficial(reader.nextInt() == 1);
                    break;
                }
                case "attendee_count" : {
                    e.setLatitude(reader.nextInt());
                    break;
                }
                default: reader.skipValue();
            }
        }
        formatDate(e);
        events.add(e);
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
        Event e = new Event();
        e.isrss = true;
        int type = in.next();
        while (!(type == XmlPullParser.END_TAG && in.getName().equals("item"))) {
            if (type == XmlPullParser.START_TAG) {
                String name = in.getName();
                if (name.equals("title")) {
                    e.setName(in.nextText());
                } else if (name.equals("link")) {
                    e.setUrl(in.getText());
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
        Log.d("New Event", e.toString());
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
