package com.winterparadox.themovieapp.home;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.HomeSection;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.ErrorViewHolder;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_POSTER;

public class HomeMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0, MOVIE_LARGE = 1, MOVIE_SMALL = 2, ERROR = 99;

    private List<Object> items;
    private ClickListener listener;
    private ErrorViewHolder.OnClickListener retryListener;

    HomeMoviesAdapter (ClickListener listener, ErrorViewHolder.OnClickListener
            retryListener) {
        this.listener = listener;
        this.retryListener = retryListener;
    }

    void addMovies (ArrayList<Object> movies) {
        if ( this.items == null ) {
            this.items = new ArrayList<> ();
        }

        if ( movies.isEmpty () ) {
            return;
        }

        int size = movies.size ();
        if ( items.contains (movies.get (0)) ) {
            int indexOf = items.indexOf (movies.get (0));
            for ( int i = 0; i < size; i++ ) {
                items.set (i + indexOf, movies.get (i));
            }
            notifyItemRangeInserted (indexOf, size);
        } else {
            items.addAll (movies);
            notifyItemRangeInserted (items.size () - size, size);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        switch ( type ) {
            case ERROR:
                View view0 = inflater.inflate (R.layout.item_error, viewGroup, false);
                return new ErrorViewHolder (view0, retryListener);
            case HEADER:
                View view1 = inflater.inflate (R.layout.item_movie_header, viewGroup, false);
                return new HeaderItemHolder (view1);
            case MOVIE_LARGE:
                View view2 = inflater.inflate (R.layout.item_movie_large, viewGroup, false);
                return new MovieItemHolder (view2);
            default:
                View view3 = inflater.inflate (R.layout.item_movie_small, viewGroup, false);
                return new MovieItemHolder (view3);
        }

    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if ( viewHolder instanceof ErrorViewHolder ) {
            ((ErrorViewHolder) viewHolder).error.setText (items.get (i).toString ());
        }

        if ( viewHolder instanceof HeaderItemHolder ) {
            bindHeader (((HeaderItemHolder) viewHolder), ((HomeSection) items.get (i)));
        }

        if ( viewHolder instanceof MovieItemHolder && i % 5 == 1 ) {
            bindMovie (((MovieItemHolder) viewHolder), ((Movie) items.get (i)));

        } else if ( viewHolder instanceof MovieItemHolder ) {
            boolean isLast = i % 5 == 4;
            HomeSection releventSection = null;
            if ( isLast ) {
                releventSection = (HomeSection) items.get (i - 4);
            }
            bindMovie (((MovieItemHolder) viewHolder), ((Movie) items.get (i)), isLast,
                    releventSection);
        }
    }

    private void bindMovie (MovieItemHolder viewHolder, Movie movie) {

        GlideApp.with (viewHolder.itemView)
                .load (Uri.parse (IMAGE + MEDIUM_BACKDROP + movie.backdropPath))
                .centerCrop ()
                .into (viewHolder.thumbnail);
        viewHolder.name.setText (movie.title);

        viewHolder.thumbnail.setTransitionName (TransitionNames.MOVIE_BACKDROP + movie.id);
        viewHolder.itemView.setOnClickListener (v -> listener.onMovieClick (movie, viewHolder
                .thumbnail));
    }

    private void bindMovie (MovieItemHolder viewHolder, Movie movie, boolean isLast,
                            HomeSection releventSection) {
        GlideApp.with (viewHolder.itemView)
                .load (Uri.parse (IMAGE + MEDIUM_POSTER + movie.posterPath))
                .centerCrop ()
                .into (viewHolder.thumbnail);

        viewHolder.thumbnail.setTransitionName (TransitionNames.MOVIE_POSTER + movie.id);
        if ( isLast ) {
            viewHolder.buttonContainer.setVisibility (View.VISIBLE);
            viewHolder.viewAll.setOnClickListener (
                    v -> listener.onSubHeaderClick (releventSection.id));
        } else {
            viewHolder.buttonContainer.setVisibility (View.GONE);
        }

        viewHolder.itemView.setOnClickListener (v -> listener.onMovieClick (movie, viewHolder.thumbnail));
    }

    private void bindHeader (HeaderItemHolder viewHolder, HomeSection header) {
        viewHolder.header.setText (header.name);
        viewHolder.itemView.setOnClickListener (v -> listener.onSubHeaderClick (header.id));
    }

    @Override
    public int getItemViewType (int position) {
        return items.get (position) instanceof HomeSection ? HEADER :
                items.get (position) instanceof String ? ERROR :
                position % 5 == 1 ? MOVIE_LARGE : MOVIE_SMALL;
    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    public void addError (String message) {
        if ( this.items == null ) {
            this.items = new ArrayList<> ();
        }

        items.clear ();
        items.add (message);
        notifyDataSetChanged ();
    }

    public void clearItems () {
        if ( items != null ) {
            items.clear ();
            notifyDataSetChanged ();
        }
    }

    static class HeaderItemHolder extends RecyclerView.ViewHolder {

        TextView header;

        HeaderItemHolder (@NonNull View itemView) {
            super (itemView);
            header = (TextView) itemView;
        }
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @Nullable
        @BindView(R.id.viewAll)
        Button viewAll;
        @Nullable
        @BindView(R.id.buttonContainer)
        View buttonContainer;
        @Nullable
        @BindView(R.id.name)
        TextView name;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onMovieClick (Movie movie, View element);

        void onSubHeaderClick (int header);
    }
}
