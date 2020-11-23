package com.example.android.hackernewsdemo.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;
import com.example.android.hackernewsdemo.utiil.RequestBatchFuture;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
        private final String url;
        private final String user;
        private final int votes;
        private final int numComments;

        static StoryImpl fromApiStory(ApiStory apiStory) {
            return new StoryImpl(apiStory.title, apiStory.url, apiStory.by, apiStory.score, apiStory.descendants);
        }

        private StoryImpl(String headline, String url, String user, int votes, int numComments) {
            this.headline = headline;
            this.url = url;
            this.user = user;
            this.votes = votes;
            this.numComments = numComments;
        }


        @Override
        public String headline() {
            return headline;
        }

        @Override
        public String url() {
            return url;
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

    private Request<String> getStoryRequest(Response.Listener<String> listener, Response.ErrorListener errorListener, int id) {
        return new StringRequest(
                Request.Method.GET,
                storyUrl(id),
                listener,
                errorListener);
    }

    private List<Request<String>> getStoryRequests(Response.Listener<String> listener,
                                                 Response.ErrorListener errorListener, int[] ids,
                                                 int maxStories) {

        List<Request<String>> storyRequests = new ArrayList<>();

        for (int i=0; i<maxStories; i++) {
            Request<String> storyRequest = getStoryRequest(listener, errorListener, ids[i]);

            storyRequests.add(storyRequest);
        }

        return storyRequests;
    }

    private ApiStory[] getStoriesFromApi() {
        final int maxStories = 10;

        int[] storyIds = getStoryIdsFromApi();

        RequestBatchFuture<String> storiesFuture = RequestBatchFuture.newFuture();

        List<Request<String>> storyRequests = getStoryRequests(storiesFuture, storiesFuture,
                storyIds, maxStories);

        for (Request<String> storyRequest : storyRequests) {
            requestQueue.add(storyRequest);
        }

        storiesFuture.setRequests(storyRequests);

        try {
            List<String> storyResponses = storiesFuture.get(60, TimeUnit.SECONDS);

            ApiStory[] apiStories = new ApiStory[storyResponses.size()];

            for (int i=0; i<storyResponses.size(); i++) {
                apiStories[i] = gson.fromJson(storyResponses.get(i), ApiStory.class);
            }

            return apiStories;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();

            return new ApiStory[] {};
        }
    }

    @Override
    public Story[] getStories() {
        ApiStory[] apiStories = getStoriesFromApi();

        Story[] stories = new Story[apiStories.length];

        for (int i=0; i<apiStories.length; i++) {
            stories[i] = StoryImpl.fromApiStory(apiStories[i]);
        }

        return stories;
    }
}
