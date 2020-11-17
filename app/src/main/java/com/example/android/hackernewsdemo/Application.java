package com.example.android.hackernewsdemo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.hackernewsdemo.api.HackerNewsApiRepository;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.HiltAndroidApp;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltAndroidApp
public class Application extends android.app.Application {

    @Module
    @InstallIn(ApplicationComponent.class)
    public abstract static class HackerNewsModule {
        @Binds
        public abstract HackerNewsRepository bindHackerNewsRepository(HackerNewsApiRepository r);
    }

    @Module
    @InstallIn(ApplicationComponent.class)
    public abstract static class HttpModule {
        @Provides
        @Singleton
        public static RequestQueue provideRequestQueue(@ApplicationContext Context context) {
            return Volley.newRequestQueue(context);
        }
    }
}
