package com.spielpark.steve.bernieapp.dagger;

import com.spielpark.steve.bernieapp.bernrate.BernRateDialogFragment;
import com.spielpark.steve.bernieapp.bernrate.BernRateFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by AndrewOrobator on 8/25/15.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(BernRateFragment bernRateFragment);

    void inject(BernRateDialogFragment bernRateDialogFragment);
}
