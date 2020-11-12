package com.example.android.hackernewsdemo;

import com.example.android.hackernewsdemo.data.DummyHackerNewsRepository;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.HiltAndroidApp;
import dagger.hilt.android.components.ApplicationComponent;

@HiltAndroidApp
public class Application extends android.app.Application {

    @Module
    @InstallIn(ApplicationComponent.class)
    public abstract static class HackerNewsModule {
        @Binds
        public abstract HackerNewsRepository bindHackerNewsRepository(DummyHackerNewsRepository r);
    }
}
