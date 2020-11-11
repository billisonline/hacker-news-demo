package com.example.android.hackernewsdemo.data;

public interface HackerNewsRepository {
    interface Story {
        String headline();
        String domain();
        String user();
        int votes();
        int numComments();
    }

    Story[] getStories();
}
