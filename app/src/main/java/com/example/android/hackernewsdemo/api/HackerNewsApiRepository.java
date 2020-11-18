package com.example.android.hackernewsdemo.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

public class HackerNewsApiRepository implements HackerNewsRepository  {
    public static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";
    public static final String TOP_STORIES_URL = BASE_URL + "topstories.json";

    private final RequestQueue requestQueue;
    private final Gson gson;

    @Inject
    public HackerNewsApiRepository(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.gson = new Gson();
    }

    static class ApiStoriesResponse {
        public ApiStoriesResponse() {}

        public ApiStory[] data;
    }

    static class ApiStory {
        public ApiStory() {}

        public int id;

        public String title;

        public String text;

        public String url = "https://news.ycombinator.com"; //todo

        public String by;

        public int score = 123;

        public int descendants;
    }

    static class StoryImpl implements Story {
        private final String headline;
        private final String domain;
        private final String user;
        private final int votes;
        private final int numComments;

        static StoryImpl fromApiStory(ApiStory apiStory) {
            return new StoryImpl(apiStory.title, apiStory.url, apiStory.by, apiStory.score, apiStory.descendants);
        }

        private StoryImpl(String headline, String domain, String user, int votes, int numComments) {
            this.headline = headline;
            this.domain = domain;
            this.user = user;
            this.votes = votes;
            this.numComments = numComments;
        }


        @Override
        public String headline() {
            return headline;
        }

        @Override
        public String domain() {
            return domain;
        }

        @Override
        public String user() {
            return user;
        }

        @Override
        public int votes() {
            return votes;
        }

        @Override
        public int numComments() {
            return numComments;
        }
    }

    private int[] getStoryIdsFromApi() {
        RequestFuture<String> future = RequestFuture.newFuture();

        StringRequest request = new StringRequest(Request.Method.GET, TOP_STORIES_URL, future, future);

        requestQueue.add(request);

        try {
            String response = future.get(60, TimeUnit.SECONDS);

            return gson.fromJson(response, int[].class);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();

            return new int[] {}; //todo
        }
    }

    private String storyUrl(int id) {
        return BASE_URL + "item/" + id + ".json";
    }

    private ApiStory getStoryFromApi(int id) {
        RequestFuture<String> future = RequestFuture.newFuture();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                storyUrl(id),
                future,
                future);

        requestQueue.add(request);

        try {
            String response = future.get(60, TimeUnit.SECONDS);

            Log.w("HackerNewsApiRepository", response);

            return gson.fromJson(response, ApiStory.class);

        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();

            return null; //todo
        }
    }

    private ApiStory[] getStoriesFromApi() {
        final int storiesCount = 10;

        int[] ids = getStoryIdsFromApi();
        List<ApiStory> stories = new ArrayList<>();

        // todo make requests concurrent
        for (int i=0; i<storiesCount; i++) {
            if (ids.length > i) {
                ApiStory story = getStoryFromApi(ids[i]);
                if (story != null) {
                    stories.add(story);
                }
            }
        }

        ApiStory[] storiesArray = new ApiStory[stories.size()];

        stories.toArray(storiesArray);

        return storiesArray;
    }

    @Override
    public Story[] getStories() {
        List<Story> stories = new ArrayList<>();

        ApiStory[] apiStories = getStoriesFromApi();

        for (ApiStory apiStory: apiStories) {
            stories.add(StoryImpl.fromApiStory(apiStory));
        }

        Story[] storiesArray = new Story[stories.size()];

        stories.toArray(storiesArray);

        return storiesArray;
    }
}
