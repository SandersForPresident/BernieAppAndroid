package com.spielpark.steve.bernieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.actMainPage;
import com.spielpark.steve.bernieapp.misc.ImgTxtAdapter;
import com.spielpark.steve.bernieapp.wrappers.NewsArticle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Steve on 7/8/2015.
 */
public class NewsTask extends AsyncTask<Object, NewsArticle, Object> {
    private static ArrayList<NewsArticle> articles;
    private static ListView list;
    private static ProgressBar progressBar;
    private static Context ctx;
    private static TextView subHeader;
    private static TextView header;
    private static String NULL_IMAGE = "https://s.bsd.net/bernie16/main/page/-/website/fb-share.png";

    public NewsTask(Context ctx, ListView listView, ProgressBar progressBar, TextView subHeader, TextView header) {
        list = listView;
        NewsTask.ctx = ctx;
        NewsTask.progressBar = progressBar;
        NewsTask.subHeader = subHeader;
        NewsTask.header = header;
    }

    public static NewsArticle getArticle(int pos) {
        return articles.get(pos);
    }

    public static ArrayList<NewsArticle> getData() {
        return articles;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        BufferedReader in;
        try {
            URL[] urls = new URL[]{
                    (new URL("https://berniesanders.com/?json=true&which=news&limit=12")),
                    (new URL("https://berniesanders.com/?json=true&which=daily&limit=12"))
            };
            for (URL u : urls) {
                in = new BufferedReader(new InputStreamReader(u.openStream()));
                if (in == null) {
                    Log.d("reader null", "no events, null reader,");
                    NewsArticle a = new NewsArticle();
                    a.setHtmlTitle("Unable to Load News");
                    a.setDesc("Check your internet connection?");
                    articles.add(a);
                    return null;
                }
                JsonReader reader = new JsonReader(in);
                try {
                    readObjects(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void readObjects(JsonReader reader) throws IOException {
        NewsArticle a = new NewsArticle();
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
            String next = reader.nextName();
            switch (next.toLowerCase().trim()) {
                case "title": {
                    a.setTitle(reader.nextString());
                    break;
                }
                case "permalink": {
                    a.setUrl(reader.nextString());
                    break;
                }
                case "date": {
                    a.setPubDate(reader.nextString());
                    break;
                }
                case "content": {
                    String content = reader.nextString();
                    if (content.contains("<style>") && content.contains("</style")) {
                        content = content.substring(content.indexOf("</style") + "</style>".length());
                    }
                    a.setDesc(content);
                    break;
                }
                case "og_image": {
                    if (reader.peek() == JsonToken.NULL) {
                        a.setImgSrc(NULL_IMAGE);
                        reader.nextNull();
                    } else {
                        a.setImgSrc(reader.nextString());
                    }
                    break;
                }
                default: {
                    if (reader.peek() != JsonToken.END_OBJECT) {
                        reader.skipValue();
                    }
                    if (reader.peek() == JsonToken.END_OBJECT) {
                        if (a.getTitle() != null) {
                            a.setTxt(getHTMLForTitle(a));
                            publishProgress(a);
                            a = new NewsArticle();
                        }
                        reader.endObject();
                    }
                }
            }
        }
        reader.close();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        articles = new ArrayList<>();
        ImgTxtAdapter adapter = new ImgTxtAdapter(ctx, R.layout.list_news_item, articles);
        list.setAdapter(adapter);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Collections.sort(articles);
        NewsArticle a;
        boolean setSubheader = false;
        for (int i = 0; i < articles.size(); i++) {
            a = articles.get(i);
            if (!(setSubheader)) {
                if (a.getUrl() != null) {
                    if (a.getUrl().contains("press-release")) {
                        subHeader.setText(Html.fromHtml(a.getDesc()));
                        String s = a.getTitle();
                        s = s.length() > 40 ? s.substring(0, 40) + "..." : s;
                        header.setText(s);
                        setSubheader = true;
                    }
                }
            }
        }
        ((ImgTxtAdapter) list.getAdapter()).notifyDataSetChanged();
        list.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((actMainPage) ctx).loadEvent(NewsTask.getArticle(position));
            }
        });
    }

    @Override
    protected void onProgressUpdate(NewsArticle[] a) {
        super.onProgressUpdate(a);
        articles.add(a[0]);
        ((ImgTxtAdapter) list.getAdapter()).notifyDataSetChanged();
    }

    private String getHTMLForTitle(NewsArticle e) {
        StringBuilder bld = new StringBuilder();
        String title = e.getTitle();
        if (title.length() > 40) {
            title = title.substring(0, 40);
            title = title.substring(0, Math.min(title.length(), title.lastIndexOf(' '))).concat("...");
        }
        bld.append("<big>").append(title).append("</big><br>");
        bld.append("<font color=\"#FF2222\">").append(e.getPubDate());
        return bld.toString();
    }
}
