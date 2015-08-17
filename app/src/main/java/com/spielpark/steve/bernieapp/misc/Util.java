package com.spielpark.steve.bernieapp.misc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.spielpark.steve.bernieapp.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Steve on 8/14/2015.
 */
public class Util {

    public enum Preferences {
        BERNRATE_DIALOGUE("BernRate_ShowDialogue", 1);

        Preferences(String s, int i) {
            this.value = i;
            this.name = s;
        }
        public int value;
        public String name;
    }

    public static Bitmap getOGImage(String src, Context context, boolean thumb) {
        try {
            HttpURLConnection conn = ((HttpURLConnection) (new URL(src)).openConnection());
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String next = in.readLine();
            while (next != null) {
                int loc = next.indexOf("og:image");
                if (loc > 0) {
                    int start = next.indexOf("https", loc);
                    Log.d("ImgURL", next.substring(start, next.lastIndexOf('\"')));
                    if (thumb) {
                        return Bitmap.createScaledBitmap(getBitmapFromURL(next.substring(start, next.lastIndexOf('\"'))), 64, 64, false);
                    } else {
                        return getBitmapFromURL(next.substring(start, next.lastIndexOf('\"')));
                    }
                }
                next = in.readLine();
            }
        } catch (IOException e) {
            Log.d("ImageRetrieveFail", "Failed to load url. Using default.");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        }
        return null;
    }

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

    public static AlertDialog getShowAgainDialogue(Context ctx, final SharedPreferences prefs, final Preferences p, String message) {
        LayoutInflater inflator = LayoutInflater.from(ctx);
        final View view = inflator.inflate(R.layout.dialogue_checkbox, null);
        AlertDialog.Builder bld = new AlertDialog.Builder(ctx, 4);
        bld.setMessage(message)
                .setTitle("Welcome!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        boolean checked = ((CheckBox) view.findViewById(R.id.dia_checkbox)).isChecked();
                        if (checked) {
                            prefs.edit().putInt(p.name, 1).commit();
                        }
                    }
                })
                .setView(view);
        return bld.create();
    }
}
