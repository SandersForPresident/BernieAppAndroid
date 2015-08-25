package com.spielpark.steve.bernieapp.dagger;

import android.app.Application;
import android.content.Context;

/**
 * Created by AndrewOrobator on 8/25/15.
 */
public class Dagger {
    private static ApplicationComponent sApplicationComponent;

    public static ApplicationComponent applicationComponent(Context context) {
        if (sApplicationComponent == null) {
            sApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule((Application) context.getApplicationContext()))
                    .build();
        }

        return sApplicationComponent;
    }
}
