package com.skyegibney.flixter.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.skyegibney.flixter.R;
import com.skyegibney.flixter.activities.MovieDetailsActivity;
import com.skyegibney.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public final static int REGULAR = 0;
    public final static int POPULAR = 1;

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies)
    {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder;
        View movieView;

        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == POPULAR)
        {
            movieView = inflater.inflate(R.layout.item_movie_popular, parent, false);
            viewHolder = new PopularMovieViewHolder(movieView);
        }
        else
        {
            movieView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new MovieViewHolder(movieView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Movie movie = movies.get(position);
        int viewType = holder.getItemViewType();

        if (viewType == POPULAR)
        {
            PopularMovieViewHolder pmvh = (PopularMovieViewHolder) holder;
            pmvh.bind(movie);
        }
        else
        {
            MovieViewHolder mvh = (MovieViewHolder) holder;
            mvh.bind(movie);
        }
    }

    @Override
    public int getItemCount()
    {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        double voteAverage = movies.get(position).getVoteAverage();

        if (voteAverage >= 7)
        {
            return POPULAR;
        }
        else
        {
            return REGULAR;
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {
        private View itemView;
        private ImageView ivBackdrop;
        private ImageView ivPoster;
        private TextView tvTitle;
        private TextView tvOverview;

        public MovieViewHolder(@NonNull View itemView)
        {
            super(itemView);

            this.itemView = itemView;
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
        }

        public void bind(final Movie movie)
        {
            tvTitle.setText(movie.title);
            tvOverview.setText(movie.overview);

            String imageUrl;



            if (ivPoster.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                imageUrl = movie.getBackdropPath();
            }
            else
            {
                imageUrl = movie.getPosterPath();
            }


            Glide.with(ivPoster.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.loading_image))
                    .apply(new RequestOptions()
                            .error(R.drawable.failed_image))
                    .into(ivPoster);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(v.getContext(), MovieDetailsActivity.class);
                    i.putExtra("Movie", Parcels.wrap(movie));
                    Pair<View, String> titlePair = Pair.create((View) tvTitle, "title");
                    Pair<View, String> overviewPair = Pair.create((View) tvOverview, "overview");

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) v.getContext(), titlePair, overviewPair);
                    v.getContext().startActivity(i, options.toBundle());
                }
            });
        }
    }

    public class PopularMovieViewHolder extends RecyclerView.ViewHolder
    {
        private View itemView;
        private ImageView ivBackdrop;

        public PopularMovieViewHolder(@NonNull View itemView)
        {
            super(itemView);

            this.itemView = itemView;
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
        }

        public void bind(final Movie movie)
        {
            Glide.with(ivBackdrop.getContext())
                    .load(movie.getBackdropPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.loading_image))
                    .apply(new RequestOptions()
                            .error(R.drawable.failed_image))
                    .into(ivBackdrop);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(v.getContext(), MovieDetailsActivity.class);
                    i.putExtra("Movie", Parcels.wrap(movie));
                    v.getContext().startActivity(i);
                }
            });
        }
    }

}
