package com.winterparadox.themovieapp.movieDetails;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class MoviesDetailsMoviesAdapter extends RecyclerView.Adapter<MoviesDetailsMoviesAdapter
        .MovieItemHolder> {


    private List<Movie> items;
    private ClickListener listener;

    public MoviesDetailsMoviesAdapter (ClickListener listener) {
        this.listener = listener;
    }

    public void setItems (List<Movie> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public MovieItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1 = inflater.inflate (R.layout.item_movie_small_horizontal,
                viewGroup, false);
        return new MovieItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull MovieItemHolder itemHolder, int i) {

        Movie movie = items.get (i);

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .centerCrop ()
                .into (itemHolder.thumbnail);


        itemHolder.itemView.setOnClickListener (v -> listener.onMovieClick (movie));

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onMovieClick (Movie member);
    }
}
