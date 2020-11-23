package com.example.android.hackernewsdemo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.hackernewsdemo.data.HackerNewsRepository.Story;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.android.components.ApplicationComponent;

public class HackerNewsProvider extends ContentProvider {

    @EntryPoint
    @InstallIn(ApplicationComponent.class)
    public interface HackerNewsRepositoryProviderEntryPoint {
        HackerNewsRepository getRepository();
    }

    private HackerNewsRepositoryProviderEntryPoint getEntryPoint() {
        Context context = getContext().getApplicationContext();

        return EntryPointAccessors.fromApplication(context, HackerNewsRepositoryProviderEntryPoint.class);
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
        HackerNewsRepository repository = getEntryPoint().getRepository();

        switch (uriMatcher.match(uri)) {
            case CODE_STORIES_INDEX:
                return storiesIndexCursor(repository.getStories());
            default:
                throw new RuntimeException();
        }
    }

    @NonNull
    private Cursor storiesIndexCursor(Story[] stories) {
        return new AbstractCursor() {

            @Override
            public int getCount() {
                return stories.length;
            }

            @Override
            public String[] getColumnNames() {
                return new String[] {
                    HackerNewsProviderContract.Stories.COLUMN_HEADLINE,
                    HackerNewsProviderContract.Stories.COLUMN_URL,
                    HackerNewsProviderContract.Stories.COLUMN_USER,
                    HackerNewsProviderContract.Stories.COLUMN_VOTES,
                    HackerNewsProviderContract.Stories.COLUMN_COMMENTS_COUNT,
                };
            }

            @Override
            public String getString(int column) {
                final int columnPositionHeadline = 0;
                final int columnPositionUrl = 1;
                final int columnPositionUser = 2;

                switch (column) {
                    case columnPositionHeadline:
                        return stories[getPosition()].headline();
                    case columnPositionUrl:
                        return stories[getPosition()].url();
                    case columnPositionUser:
                        return stories[getPosition()].user();
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
                        return stories[getPosition()].votes();
                    case columnPositionCommentsCount:
                        return stories[getPosition()].numComments();
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
