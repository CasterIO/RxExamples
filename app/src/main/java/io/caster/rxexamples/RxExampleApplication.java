package io.caster.rxexamples;

import android.app.Application;

import timber.log.Timber;

public class RxExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
