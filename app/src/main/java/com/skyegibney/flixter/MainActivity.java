package com.skyegibney.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.skyegibney.flixter.adapters.MovieAdapter;
import com.skyegibney.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity
{
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    MovieAdapter adapter;
    RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();
        adapter = new MovieAdapter(this, movies);
        rvMovies = findViewById(R.id.rvMovies);

        rvMovies.setAdapter(adapter);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                Log.d(TAG, "onSuccess");

                JSONObject jsonObject = json.jsonObject;

                try
                {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response,
                                  Throwable throwable)
            {
                Log.d(TAG, "onFailure");
            }
        });
    }
}
