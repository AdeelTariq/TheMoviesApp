package com.winterparadox.themovieapp.favorites;

import android.animation.Animator;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter
        .MovieItemHolder> {


    private List<Movie> items;
    private ClickListener listener;

    FavoritesAdapter (ClickListener listener) {
        this.listener = listener;
    }

    void setItems (List<Movie> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    private void remove (int index) {
        this.items.remove (index);
        notifyItemRemoved (index);
    }


    @NonNull
    @Override
    public MovieItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1 = inflater.inflate (R.layout.item_movie_list,
                viewGroup, false);
        return new MovieItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull MovieItemHolder itemHolder, int i) {

        Movie movie = items.get (i);

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .placeholder (R.drawable.ic_fallback_poster)
                .centerCrop ()
                .into (itemHolder.thumbnail);

        itemHolder.thumbnail.setTransitionName (TransitionNames.MOVIE_POSTER + movie.id);

        itemHolder.title.setText (String.format ("%s (%s)", movie.title, movie.year));

        itemHolder.plot.setText (movie.overview);

        if ( itemHolder.favorite.getProgress () > 0 ) {
            itemHolder.favorite.setProgress (0);
        }

        itemHolder.itemView.setOnClickListener (v -> listener.onMovieClick (movie, itemHolder
                .thumbnail));

        itemHolder.favorite.setOnClickListener (v -> {

            itemHolder.favorite.addAnimatorListener (new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animator) {

                }

                @Override
                public void onAnimationEnd (Animator animator) {
                    listener.unFavoriteClick (movie);
                    remove (itemHolder.getAdapterPosition ());
                }

                @Override
                public void onAnimationCancel (Animator animator) {

                }

                @Override
                public void onAnimationRepeat (Animator animator) {

                }
            });

            itemHolder.favorite.playAnimation ();

        });

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.plot) TextView plot;
        @BindView(R.id.favorite) LottieAnimationView favorite;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);

            favorite.setAnimation (R.raw.favorite);
            favorite.setMinAndMaxProgress (0.8f, 1);
        }
    }

    interface ClickListener {
        void onMovieClick (Movie movie, View element);

        void unFavoriteClick (Movie movie);
    }
}
