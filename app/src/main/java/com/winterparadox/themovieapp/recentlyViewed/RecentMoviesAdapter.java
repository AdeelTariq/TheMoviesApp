package com.winterparadox.themovieapp.recentlyViewed;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.GradientColorFilterTransformation;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_POSTER;

public class RecentMoviesAdapter extends RecyclerView.Adapter<RecentMoviesAdapter
        .MovieItemHolder> {


    private final RequestOptions requestOptions;
    private List<Movie> items;
    private ClickListener listener;

    RecentMoviesAdapter (ClickListener listener) {
        this.listener = listener;
        requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new GradientColorFilterTransformation ());
    }

    void setItems (List<Movie> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public MovieItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {

        boolean isTablet = viewGroup.getContext ().getResources ()
                .getBoolean (R.bool.isLargeTablet);

        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1;
        if ( isTablet ) {
            view1 = inflater.inflate (R.layout.item_movie_large,
                    viewGroup, false);
        } else {
            view1 = inflater.inflate (R.layout.item_movie_small,
                    viewGroup, false);
        }
        return new MovieItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull MovieItemHolder itemHolder, int i) {

        Movie movie = items.get (i);


        itemHolder.thumbnail.setTransitionName (TransitionNames.MOVIE_POSTER + movie.id);

        if ( itemHolder.title != null ) {
            itemHolder.title.setText (movie.title);

            GlideApp.with (itemHolder.itemView)
                    .load (Uri.parse (IMAGE + MEDIUM_POSTER + movie.posterPath))
                    .placeholder (R.drawable.ic_fallback_poster)
                    .apply (requestOptions)
                    .into (itemHolder.thumbnail);

        } else {
            GlideApp.with (itemHolder.itemView)
                    .load (Uri.parse (IMAGE + MEDIUM_POSTER + movie.posterPath))
                    .placeholder (R.drawable.ic_fallback_poster)
                    .centerCrop ()
                    .into (itemHolder.thumbnail);
        }

        itemHolder.itemView.setOnClickListener (v -> listener.onMovieClick (movie, itemHolder
                .thumbnail));

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @Nullable
        @BindView(R.id.name)
        TextView title;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onMovieClick (Movie member, View element);
    }
}
