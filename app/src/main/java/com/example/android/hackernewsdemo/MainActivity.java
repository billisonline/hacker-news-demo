package com.example.android.hackernewsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Story[] stories = {
            new Story(
                    "The Secret Math Society Known as Nicolas Bourbaki",
                    "quantamagazine.org",
                    "pseudolus",
                    127,
                    37
            ),
            new Story(
                    "Could a Peasant Defeat a Knight in Battle?",
                    "medievalists.net",
                    "ynac",
                    179,
                    153
            ),
            new Story(
                    "AppleCrate II: A New Apple II-Based Parallel Computer (2015)",
                    "michaeljmahon.com",
                    "aresant",
                    63,
                    7
            ),
    };;

    static class Story {
        public final String headline;
        public final String domain;
        public final String user;
        public final int votes;
        public final int numComments;

        public Story(String headline, String domain, String user, int votes, int numComments) {
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

        RecyclerView recyclerView = findViewById(R.id.rv_stories);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new StoryAdapter());
    }

    class StoryViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
        }

        public void setStory(int order, Story story) {
            TextView mOrder = itemView.findViewById(R.id.tv_number);
            TextView mHeadline = itemView.findViewById(R.id.tv_headline);
            TextView mDomain = itemView.findViewById(R.id.tv_domain);
            TextView mUser = itemView.findViewById(R.id.tv_user);
            TextView mVotes = itemView.findViewById(R.id.tv_votes);
            TextView mComments = itemView.findViewById(R.id.tv_comments);

            mOrder.setText(String.valueOf(order + 1) + ".");
            mHeadline.setText(story.headline);
            mDomain.setText("(" + story.domain + ")");
            mUser.setText(story.user);
            mVotes.setText(story.votes + " points");
            mComments.setText(story.numComments + " comments");
        }
    }

    class StoryAdapter extends RecyclerView.Adapter<StoryViewHolder> {

        @NonNull
        @Override
        public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.story, parent, false);

            return new StoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
            holder.setStory(position, stories[position]);
        }

        @Override
        public int getItemCount() {
            return stories.length;
        }
    }

}