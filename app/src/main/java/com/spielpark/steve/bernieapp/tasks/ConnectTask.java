package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.fragments.ConnectFragment;
import com.spielpark.steve.bernieapp.wrappers.Event;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
public class ConnectTask extends AsyncTask {
    private static ArrayList<Event> events;
    private static ListView list;
    private static ProgressBar progressBar;
    private static Context ctx;
    private static int radius;
    private static int zip;
    private static View map;
    private static StringBuilder bld;

    public ConnectTask(Context ctx, ListView listView, ProgressBar progressBar, View map, int zip, int radius) {
        this.list = listView;
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.zip = zip;
        this.radius = radius;
        this.map = map;
    }

    public static ArrayList<Event> getEvents() {
        return events;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        events = new ArrayList<>();
        BufferedReader in = null;
        try {
            URL url = new URL("https://go.berniesanders.com/page/event/search_results?orderby=date&format=json&zip_radius=" + radius + "&zip=" + zip);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in == null) {
            Event e = new Event();
            e.setName("Unable to Load News");
            e.setDescription("Check your internet connection?");
            events.add(e);
            super.onCancelled();
            return null;
        }
        JsonReader reader = new JsonReader(in);
        try {
            readObjects(reader);
        } catch (IOException e) {
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
        map.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        map = null;
        ConnectFragment.setMarkers();
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
        bld.append("<font color=\"#FF2222\">&emsp;").append(e.getVenue_city()).append(", ").append(e.getState()).append("</font>");
        return bld.toString();
    }

    private void readObjects(JsonReader reader) throws IOException {
        Event e = new Event();
        bld = new StringBuilder();
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
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
                    String zipped = reader.nextString().substring(0, 5);
                    e.setZip(Integer.parseInt(zipped));
                    bld.append(zipped).append("%7C");
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
                    e.setAttendee_count(reader.nextInt());
                    formatDate(e);
                    events.add(e);
                    e = new Event();
                    reader.endObject();
                    break;
                }
                default: {
                    reader.skipValue();
                }
            }
        }
        reader.close();
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
