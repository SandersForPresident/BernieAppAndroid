package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.misc.ImgTxtAdapter;
import com.spielpark.steve.bernieapp.wrappers.Issue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Steve on 7/9/2015.
 */
public class IssuesTask extends AsyncTask<Object, Issue, Object> {
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

    public static Issue getIssue(int position) {
        return issues.get(position);
    }

    private String getHTMLForTitle(Issue i) {
        StringBuilder bld = new StringBuilder();
        String title = i.getTitle();
        if (title.length() > 40) {
            title = title.substring(0, 40);
            title = title.substring(0, Math.min(title.length(), title.lastIndexOf(' '))).concat("...");
        }
        bld.append("<big>").append(title).append("</big><br>");
        bld.append("<font color=\"#FF2222\">").append(i.getPubDate());
        return bld.toString();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        for (Issue i : issues) {
            i.setVideo(vidLinks.get(i.getTitle().toLowerCase()));
            i.setImgSrc("https://img.youtube.com/vi/" + i.getVideo() + "/default.jpg");
        }
        ((ImgTxtAdapter) list.getAdapter()).notifyDataSetChanged();
        list.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        issues = new ArrayList<>();
        ImgTxtAdapter adapter = new ImgTxtAdapter(ctx, R.layout.list_news_item, issues);
        list.setAdapter(adapter);
    }

    @Override
    protected void onProgressUpdate(Issue[] i) {
        super.onProgressUpdate(i);
        issues.add(i[0]);
        ((ImgTxtAdapter) list.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        retrieveLinks();
        BufferedReader in = null;
        try {
            URL url = new URL("https://berniesanders.com/?json=true&which=issues");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in == null) {
            Log.d("reader null", "no events, null reader,");
            Issue i = new Issue();
            i.setHtmlTitle("Unable to Load News");
            i.setDesc("Check your internet connection?");
            issues.add(i);
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

    private void readObjects(JsonReader reader) throws IOException {
        Issue i = new Issue();
        while (reader.hasNext()) {
            if (isCancelled()) {
                return;
            }
            if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                reader.beginArray();
            }
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
            if (reader.peek() == JsonToken.END_ARRAY) {
                reader.endArray();
            }
            if (reader.peek() == JsonToken.END_DOCUMENT) {
                return;
            }
            String next = reader.nextName();
            switch (next.toLowerCase().trim()) {
                case "title": {
                    i.setTitle(Html.fromHtml(reader.nextString()).toString());
                    break;
                }
                case "permalink": {
                    i.setUrl(reader.nextString());
                    break;
                }
                case "date": {
                    i.setPubDate(reader.nextString());
                    break;
                }
                case "content": {
                    String content = reader.nextString();
                    if (content.contains("<style>") && content.contains("</style")) {
                        content = content.substring(content.indexOf("</style") + "</style>".length());
                    }
                    i.setDesc(content);
                    break;
                }
                default: {
                    if (reader.peek() != JsonToken.END_OBJECT) {
                        reader.skipValue();
                    }
                    if (reader.peek() == JsonToken.END_OBJECT) {
                        if (i.getTitle() != null) {
                            i.setTxt(getHTMLForTitle(i));
                            publishProgress(i);
                            i = new Issue();
                        }
                        reader.endObject();
                    }
                }
            }
        }
        reader.close();
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
            readReddit(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readReddit(JsonReader reader) throws IOException {
        String title = "";
        String link = "";
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
            String next = reader.nextName();
            switch (next.toLowerCase().trim()) {
                case "children": {
                    reader.beginArray();
                    reader.beginObject();
                    break;
                }
                case "data": {
                    reader.beginObject();
                    break;
                }
                case "title": {
                    title = reader.nextString().replaceAll("&amp;", "&");
                    break;
                }
                case "url": {
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
}
