package com.spielpark.steve.bernieapp.misc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.spielpark.steve.bernieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Steve on 8/14/2015.
 */
public class Util {

    private static Picasso p;

    public static int[] getScreenWidthHeight(Activity ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = Math.round(metrics.widthPixels / metrics.density) - 24;
        int height = Math.round(metrics.heightPixels / metrics.density) / 3;
        return new int[]{width, height};
    }

    public static Picasso getPicasso(Context ctx) {
        if (p == null) {
            p = new Picasso.Builder(ctx).build();
        }
        return p;
    }

    public static int getFullScreenHeight(Activity ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = Math.round(metrics.heightPixels / metrics.density);
        return height;
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
                        Log.d("Dialog", "Was dismissed.");
                        boolean checked = ((CheckBox) view.findViewById(R.id.dia_checkbox)).isChecked();
                        if (checked) {
                            Log.d("Dialog Gen", "Box was checked, putting in things.");
                            prefs.edit().putInt(p.name, 0).commit();
                        }
                    }
                })
                .setView(view);
        return bld.create();
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
