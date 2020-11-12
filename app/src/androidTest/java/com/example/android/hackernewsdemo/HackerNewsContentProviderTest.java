package com.example.android.hackernewsdemo;

import android.content.Context;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.android.hackernewsdemo.data.HackerNewsProviderContract.Stories;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;
import com.example.android.hackernewsdemo.modules.HackerNewsModule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@HiltAndroidTest
@UninstallModules(HackerNewsModule.class)
public class HackerNewsContentProviderTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    static class TestHackerNewsRepository implements HackerNewsRepository {
        @Inject
        public TestHackerNewsRepository() {}

        @Override
        public Story[] getStories() {
            return new Story[] {
                    new Story() {
                        @Override
                        public String headline() {
                            return "Test Headline";
                        }

                        @Override
                        public String domain() {
                            return "testdomain.com";
                        }

                        @Override
                        public String user() {
                            return "testuser";
                        }

                        @Override
                        public int votes() {
                            return 123;
                        }

                        @Override
                        public int numComments() {
                            return 456;
                        }
                    }
            };
        }
    }

    @Module
    @InstallIn(ApplicationComponent.class)
    public abstract static class TestHackerNewsModule {
        @Binds
        public abstract HackerNewsRepository bindHackerNewsRepository(TestHackerNewsRepository r);
    }

    @Test
    public void listStories() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Cursor c = appContext.getContentResolver().query(Stories.index(), null, null, null, null);

        c.moveToFirst();

        assertEquals(1, c.getCount());

        assertEquals("testdomain.com", c.getString(c.getColumnIndex(Stories.COLUMN_DOMAIN)));
        assertEquals("testuser", c.getString(c.getColumnIndex(Stories.COLUMN_USER)));
        assertEquals("Test Headline", c.getString(c.getColumnIndex(Stories.COLUMN_HEADLINE)));
        assertEquals(123, c.getInt(c.getColumnIndex(Stories.COLUMN_VOTES)));
        assertEquals(456, c.getInt(c.getColumnIndex(Stories.COLUMN_COMMENTS_COUNT)));
    }
}