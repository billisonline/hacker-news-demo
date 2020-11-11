package com.example.android.hackernewsdemo.modules;

import com.example.android.hackernewsdemo.data.DummyHackerNewsRepository;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public abstract class HackerNewsModule {
    @Binds
    public abstract HackerNewsRepository bindHackerNewsRepository(DummyHackerNewsRepository r);
}
