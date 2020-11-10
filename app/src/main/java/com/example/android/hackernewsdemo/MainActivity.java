package com.example.android.hackernewsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Story story = new Story(
                1,
                "The Secret Math Society Known as Nicolas Bourbaki",
                "quantamagazine.org",
                "pseudolus",
                103,
                28
        );

        setStory(story);
    }

    private void setStory(Story story) {
        TextView mOrder = findViewById(R.id.tv_number);
        TextView mHeadline = findViewById(R.id.tv_headline);
        TextView mDomain = findViewById(R.id.tv_domain);
        TextView mUser = findViewById(R.id.tv_user);
        TextView mVotes = findViewById(R.id.tv_votes);
        TextView mComments = findViewById(R.id.tv_comments);

        mOrder.setText(String.valueOf(story.order) + ".");
        mHeadline.setText(story.headline);
        mDomain.setText("(" + story.domain + ")");
        mUser.setText(story.user);
        mVotes.setText(story.votes + " points");
        mComments.setText(story.numComments + " comments");
    }
}