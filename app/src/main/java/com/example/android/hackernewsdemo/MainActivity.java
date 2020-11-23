package com.example.android.hackernewsdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hackernewsdemo.data.HackerNewsProviderContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID_STORIES = 1;

    StoryAdapter storiesViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView storiesView = findViewById(R.id.rv_stories);

        storiesViewAdapter = new StoryAdapter();

        storiesView.setLayoutManager(new LinearLayoutManager(this));
        storiesView.setAdapter(storiesViewAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID_STORIES, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_ID_STORIES:
                return new CursorLoader(this, HackerNewsProviderContract.Stories.index(), null, null, null, null);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        storiesViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    static class Story {
        public final String headline;
        public final String url;
        public final String user;
        public final int votes;
        public final int numComments;

        public static Story fromCursor(Cursor c) {
            String headline = c.getString(c.getColumnIndex(HackerNewsProviderContract.Stories.COLUMN_HEADLINE));
            String url = c.getString(c.getColumnIndex(HackerNewsProviderContract.Stories.COLUMN_URL));
            String user = c.getString(c.getColumnIndex(HackerNewsProviderContract.Stories.COLUMN_USER));
            int votes = c.getInt(c.getColumnIndex(HackerNewsProviderContract.Stories.COLUMN_VOTES));
            int numComments = c.getInt(c.getColumnIndex(HackerNewsProviderContract.Stories.COLUMN_COMMENTS_COUNT));

            return new Story(headline, url, user, votes, numComments);
        }

        public Story(String headline, String url, String user, int votes, int numComments) {
            this.headline = headline;
            this.url = url;
            this.votes = votes;
            this.user = user;
            this.numComments = numComments;
        }
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        Context context;

        TextView mOrder;
        TextView mHeadline;
        TextView mDomain;
        TextView mUser;
        TextView mVotes;
        TextView mComments;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.context = itemView.getContext();

            mOrder = itemView.findViewById(R.id.tv_number);
            mHeadline = itemView.findViewById(R.id.tv_headline);
            mDomain = itemView.findViewById(R.id.tv_domain);
            mUser = itemView.findViewById(R.id.tv_user);
            mVotes = itemView.findViewById(R.id.tv_votes);
            mComments = itemView.findViewById(R.id.tv_comments);
        }

        private String getDomainFromUrl(String url)
        {
            return Uri.parse(url).getAuthority().replace("www.", "");
        }

        public void setStory(int order, Story story) {
            mOrder.setText(String.valueOf(order + 1) + ".");
            mHeadline.setText(story.headline);
            mDomain.setText("(" + getDomainFromUrl(story.url) + ")");
            mUser.setText(story.user);
            mVotes.setText(story.votes + " points");
            mComments.setText(story.numComments + " comments");

            mHeadline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(story.url);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    static class StoryAdapter extends RecyclerView.Adapter<StoryViewHolder> {

        private Cursor storiesCursor;

        @NonNull
        @Override
        public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.story, parent, false);

            return new StoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
            if (storiesCursor == null) {return;}

            storiesCursor.moveToPosition(position);

            holder.setStory(position, Story.fromCursor(storiesCursor));
        }

        public void swapCursor(Cursor cursor) {
            storiesCursor = cursor;

            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (storiesCursor == null) {return 0;}

            return storiesCursor.getCount();
        }
    }

}