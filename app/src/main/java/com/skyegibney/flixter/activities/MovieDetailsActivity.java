package com.skyegibney.flixter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.skyegibney.flixter.R;
import com.skyegibney.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity
{
    Movie movie;

    ImageView ivBackdrop;
    TextView tvTitle;
    RatingBar rbVoteAverage;
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        ivBackdrop = findViewById(R.id.ivBackdrop);
        tvTitle = findViewById(R.id.tvTitle);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        tvOverview = findViewById(R.id.tvOverview);

        movie = Parcels.unwrap(getIntent().getParcelableExtra("Movie"));

        Glide.with(this)
                .load(movie.getBackdropPath())
                .into(ivBackdrop);
        tvTitle.setText(movie.getTitle());
        rbVoteAverage.setRating((float)movie.getVoteAverage());
        tvOverview.setText(movie.getOverview());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movie.id), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                final String videoId;

                try
                {
                    JSONObject jsonObject = json.jsonObject;
                    JSONArray results = jsonObject.getJSONArray("results");

                    if (results.length() == 0)
                    {
                        return;
                    }

                    videoId = results.getJSONObject(0).getString("key");

                    ivBackdrop.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            intent.putExtra("videoId", videoId);
                            startActivity(intent);
                        }
                    });
                }
                catch (JSONException e)
                {
                    Log.e("MovieDetailsActivity", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response,
                                  Throwable throwable)
            {
                Log.d("MovieDetailsActivity", "onFailure");
            }
        });
    }
}
