package com.example.android.hackernewsdemo.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.android.hackernewsdemo.data.HackerNewsRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HackerNewsApiRepository implements HackerNewsRepository  {
    private final RequestQueue requestQueue;

    @Inject
    public HackerNewsApiRepository(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    static class ApiStoriesResponse {
        public ApiStoriesResponse() {}

        public ApiStory[] data;
    }

    static class ApiStory {
        public ApiStory() {}

        public String headline;

        public String domain;

        public String user;

        public int votes;

        public int numComments;
    }

    static class StoryImpl implements Story {
        private final String headline;
        private final String domain;
        private final String user;
        private final int votes;
        private final int numComments;

        static StoryImpl fromApiStory(ApiStory apiStory) {
            return new StoryImpl(apiStory.headline, apiStory.domain, apiStory.user, apiStory.votes, apiStory.numComments);
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

    private ApiStory[] getStoriesFromApi() {
        RequestFuture<String> future = RequestFuture.newFuture();

        StringRequest request = new StringRequest(Request.Method.GET, "https://httpbin.org/get", future, future);

        /*requestQueue.add(request);

        try {
            String response = future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }*/

        String response = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"headline\": \"The Secret Math Society Known as Nicolas Bourbaki\",\n" +
                "      \"domain\": \"quantamagazine.org\",\n" +
                "      \"user\": \"pseudolus\",\n" +
                "      \"votes\": 127,\n" +
                "      \"numComments\": 37\n" +
                "    },\n" +
                "    {\n" +
                "      \"headline\": \"Could a Peasant Defeat a Knight in Battle?\",\n" +
                "      \"domain\": \"medievalists.net\",\n" +
                "      \"user\": \"ynac\",\n" +
                "      \"votes\": 179,\n" +
                "      \"numComments\": 153\n" +
                "    },\n" +
                "    {\n" +
                "      \"headline\": \"AppleCrate II: A New Apple II-Based Parallel Computer (2015)\",\n" +
                "      \"domain\": \"michaeljmahon.com\",\n" +
                "      \"user\": \"aresant\",\n" +
                "      \"votes\": 63,\n" +
                "      \"numComments\": 7\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ApiStoriesResponse jsonResponse = (new Gson()).fromJson(response, ApiStoriesResponse.class);

        return jsonResponse.data;
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
