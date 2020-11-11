package com.example.android.hackernewsdemo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HackerNewsProvider extends ContentProvider {

    Story[] stories = {
            new Story(
                    1,
                    "The Secret Math Society Known as Nicolas Bourbaki",
                    "quantamagazine.org",
                    "pseudolus",
                    127,
                    37
            ),
            new Story(
                    2,
                    "Could a Peasant Defeat a Knight in Battle?",
                    "medievalists.net",
                    "ynac",
                    179,
                    153
            ),
            new Story(
                    3,
                    "AppleCrate II: A New Apple II-Based Parallel Computer (2015)",
                    "michaeljmahon.com",
                    "aresant",
                    63,
                    7
            ),
    };

    static class Story {
        public final int order;
        public final String headline;
        public final String domain;
        public final String user;
        public final int votes;
        public final int numComments;

        public Story(int order, String headline, String domain, String user, int votes, int numComments) {
            this.order = order;
            this.headline = headline;
            this.domain = domain;
            this.votes = votes;
            this.user = user;
            this.numComments = numComments;
        }
    }

    private static final int CODE_STORIES_INDEX = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(HackerNewsProviderContract.CONTENT_AUTHORITY, HackerNewsProviderContract.PATH_STORIES, CODE_STORIES_INDEX);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case CODE_STORIES_INDEX:
                return storiesIndexCursor();
            default:
                throw new RuntimeException();
        }
    }

    @NonNull
    private Cursor storiesIndexCursor() {
        return new AbstractCursor() {

            @Override
            public int getCount() {
                return stories.length;
            }

            @Override
            public String[] getColumnNames() {
                return new String[] {
                    HackerNewsProviderContract.Stories.COLUMN_HEADLINE,
                    HackerNewsProviderContract.Stories.COLUMN_DOMAIN,
                    HackerNewsProviderContract.Stories.COLUMN_USER,
                    HackerNewsProviderContract.Stories.COLUMN_VOTES,
                    HackerNewsProviderContract.Stories.COLUMN_COMMENTS_COUNT,
                };
            }

            @Override
            public String getString(int column) {
                final int columnPositionHeadline = 0;
                final int columnPositionDomain = 1;
                final int columnPositionUser = 2;

                switch (column) {
                    case columnPositionHeadline:
                        return stories[getPosition()].headline;
                    case columnPositionDomain:
                        return stories[getPosition()].domain;
                    case columnPositionUser:
                        return stories[getPosition()].user;
                    default:
                        throw new RuntimeException();
                }
            }

            @Override
            public short getShort(int column) {
                return 0;
            }

            @Override
            public int getInt(int column) {
                final int columnPositionVotes = 3;
                final int columnPositionCommentsCount = 4;

                switch (column) {
                    case columnPositionVotes:
                        return stories[getPosition()].votes;
                    case columnPositionCommentsCount:
                        return stories[getPosition()].numComments;
                    default:
                        throw new RuntimeException();
                }
            }

            @Override
            public long getLong(int column) {
                return 0;
            }

            @Override
            public float getFloat(int column) {
                return 0;
            }

            @Override
            public double getDouble(int column) {
                return 0;
            }

            @Override
            public boolean isNull(int column) {
                return false;
            }
        };
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
