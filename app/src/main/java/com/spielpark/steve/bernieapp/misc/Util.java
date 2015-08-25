package com.spielpark.steve.bernieapp.misc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import com.spielpark.steve.bernieapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Steve on 8/14/2015.
 */
public class Util {

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    public static Bitmap getOGImage(String src, Context context, boolean thumb) {
        String key = context.getResources().getString(R.string.firesizeacc);
        String fireUrl = "https://" + key + ".firesize.com/" + (thumb ? "150x120" : "500x300") + "/g_none/";
        try {
            HttpURLConnection conn = ((HttpURLConnection) (new URL(src)).openConnection());
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String next = in.readLine();
            while (next != null) {
                int loc = next.indexOf("og:image");
                if (loc > 0) {
                    int start = next.indexOf("https", loc);
                    Log.d("ImgURL", fireUrl + next.substring(start, next.lastIndexOf('\"')));
                    return getBitmapFromURL(fireUrl + next.substring(start, next.lastIndexOf('\"')));
                }
                next = in.readLine();
            }
        } catch (IOException e) {
            Log.d("ImageRetrieveFail", "Failed to load url. Using default.");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        }
        return null;
    }

    public static int[] getScreenWidthHeight(Activity ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = Math.round(metrics.widthPixels / metrics.density) - 24;
        int height = Math.round(metrics.heightPixels / metrics.density) / 3;
        return new int[]{width, height};
    }

    public static boolean isOnWifi(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public enum Preferences {
        BERNRATE_DIALOGUE("BernRate_ShowDialogue", 1);

        public int value;
        public String name;

        Preferences(String s, int i) {
            this.value = i;
            this.name = s;
        }
    }

}
