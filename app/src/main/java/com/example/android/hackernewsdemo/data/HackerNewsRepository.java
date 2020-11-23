package com.example.android.hackernewsdemo.data;

public interface HackerNewsRepository {
    interface Story {
        String headline();
        String url();
        String user();
        int votes();
        int numComments();
    }

    Story[] getStories();
}
