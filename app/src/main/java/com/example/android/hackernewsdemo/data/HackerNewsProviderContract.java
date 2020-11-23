package com.example.android.hackernewsdemo.data;

import android.net.Uri;

public class HackerNewsProviderContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.hackernewsdemo";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STORIES = "stories";

    public static class Stories {
        public static Uri index() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_STORIES).build();
        }

        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_COMMENTS_COUNT = "comments_count";
    }
}
