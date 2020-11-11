package com.example.android.hackernewsdemo.data;

import javax.inject.Inject;

public class DummyHackerNewsRepository implements HackerNewsRepository {

    @Inject
    public DummyHackerNewsRepository() {}

    static class StoryImpl implements Story {

        private final String headline;
        private final String domain;
        private final String user;
        private final int votes;
        private final int numComments;

        public StoryImpl(String headline, String domain, String user, int votes, int numComments) {
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

    @Override
    public Story[] getStories() {
        return new Story[] {
                new StoryImpl(
                        "The Secret Math Society Known as Nicolas Bourbaki",
                        "quantamagazine.org",
                        "pseudolus",
                        127,
                        37
                ),
                new StoryImpl(
                        "Could a Peasant Defeat a Knight in Battle?",
                        "medievalists.net",
                        "ynac",
                        179,
                        153
                ),
                new StoryImpl(
                        "AppleCrate II: A New Apple II-Based Parallel Computer (2015)",
                        "michaeljmahon.com",
                        "aresant",
                        63,
                        7
                ),
        };
    }
}
