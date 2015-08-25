package com.spielpark.steve.bernieapp.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by AndrewOrobator on 8/25/15.
 */
public class DataStore {
    private static final String PREF_KEY_SHOW_BERN_RATE_DIALOG = "show_bern_rate_dialog";
    private SharedPreferences prefs;

    @Inject
    public DataStore(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean showBernRateDialog() {
        return prefs.getBoolean(PREF_KEY_SHOW_BERN_RATE_DIALOG, true);
    }

    public void stopShowingBernRateDialog() {
        prefs.edit().putBoolean(PREF_KEY_SHOW_BERN_RATE_DIALOG, false).apply();
    }

}
