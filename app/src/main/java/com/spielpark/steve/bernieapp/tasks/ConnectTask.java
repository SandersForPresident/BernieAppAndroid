package com.spielpark.steve.bernieapp.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.fragments.ConnectFragment;
import com.spielpark.steve.bernieapp.misc.Util;
import com.spielpark.steve.bernieapp.wrappers.Event;
import com.spielpark.steve.bernieapp.wrappers.ImgTxtItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Steve on 7/8/2015.
 */
public class ConnectTask extends AsyncTask {
    private static ArrayList<Event> events;
    private static Context ctx;
    private static ConnectFragment frag;

    public ConnectTask(Context ctx, ConnectFragment frag) {
        this.frag = frag;
        this.ctx = ctx;
    }

    public static ArrayList<Event> getEvents() {
        return events;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        events = new ArrayList<>();
        BufferedReader in = null;
        try {
            URL url = frag.fetchCountry ?
                    new URL("https://go.berniesanders.com/page/event/search_results?orderby=date&format=json&limit=500") :
                    new URL("https://go.berniesanders.com/page/event/search_results?orderby=date&format=json&zip_radius=" + frag.mRadius + "&zip=" + frag.mZip);
            Log.d("URL", url.toString());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in == null) {
            Log.d("reader null", "no events, null reader,");
            Event e = new Event();
            e.setName("Unable to Load News");
            e.setDescription("Check your internet connection?");
            events.add(e);
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
        String[] dates = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            titles[i] = getHTMLForTitle(events.get(i));
            dates[i] = getHTMLForDate(events.get(i).getDate());
        }
        ConnectAdapter adapter = new ConnectAdapter(ctx, R.layout.list_connect_events, dates, titles);
        frag.setMarkers();
        frag.updateViews(adapter);
        frag = null;
    }

    private void formatDate(Event e) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = ft.parse(e.getDate());
            e.setDate(new SimpleDateFormat("MMMM d, yyyy").format(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    private String getHTMLForTitle(Event e) {
        StringBuilder bld = new StringBuilder();
        bld.append("<big><font color =\"#147FD7\">").append(e.getName()).append("</font></big><br>");
        if (e.getVenue_city() != null) {
            bld.append("&emsp;").append(e.getVenue_city()).append(", ").append(e.getState()).append(" - ").append(e.getZip()).append("<br>");
            bld.append("&emsp;Participating: ").append(e.isOfficial() ? "N/A" : Integer.toString(e.getAttendee_count()));
        }
        return bld.toString();
    }

    private String getHTMLForDate(String s) {
        String ret = s;
        SimpleDateFormat ft = new SimpleDateFormat("MMMM d, yyyy");
        try {
            Date date = ft.parse(s);
            ret = new SimpleDateFormat("'<big><font color=\"#EA504E\">'d'</font><br><font color=\"#147FD7\">'MMM'</font>'").format(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    private void readObjects(JsonReader reader) throws IOException {
        Log.d("JsonReader", "Beginning parsing");
        Event e = new Event();
        while (reader.hasNext()) {
            if (isCancelled()) {
                return;
            }
            if (reader.peek() == JsonToken.END_OBJECT) {
                Log.d("Adding..", "Adding event: " + e.getName());
                formatDate(e);
                events.add(e);
                e = new Event();
                reader.endObject();
            }
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
            String next = reader.nextName();
            switch(next.toLowerCase().trim()) {
                case "results" : {
                    reader.beginArray();
                    if (reader.peek() == JsonToken.END_ARRAY) {
                        //There's no events~!
                        e.setName("No events in your area!");
                        e.setDescription("Perhaps you want to create one? Visit www.berniesanders.com!");
                        events.add(e);
                        return;
                    }
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
                case "closed_msg" : {
                    reader.skipValue(); //consume and throw away
                    if (reader.peek() == JsonToken.END_OBJECT) {
                        formatDate(e);
                        events.add(e);
                        e = new Event();
                        reader.endObject();
                    }
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

    private class ConnectAdapter extends ArrayAdapter {
        private String[] dates;
        private String[] titles;
        private int res;

        public ConnectAdapter(Context context, int resource, String[] dates, String[] titles) {
            super(context, resource, dates);
            this.dates = dates;
            this.titles = titles;
            this.res = resource;
        }

        @Override
        public Object getItem(int position) {
            return Html.fromHtml( dates[position] + " " + titles[position]);
        }

        @Override
        public int getCount() {
            return dates.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder v;
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                convertView = inflater.inflate(res, parent, false);
                v = new ViewHolder();
                v.date = (TextView) convertView.findViewById(R.id.list_con_txtDate);
                v.title = (TextView) convertView.findViewById(R.id.list_con_txtDesc);
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }
            v.date.setText(Html.fromHtml(dates[position]));
            v.date.setTypeface(Typeface.createFromAsset(ctx.getAssets(), "Jubilat.otf"));
            v.title.setText(Html.fromHtml(titles[position]));
            return convertView;
        }

        private class ViewHolder {
            TextView date;
            TextView title;
        }
    }
}
