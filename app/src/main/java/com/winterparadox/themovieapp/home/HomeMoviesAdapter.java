package com.winterparadox.themovieapp.home;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class HomeMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0, MOVIE_LARGE = 1, MOVIE_SMALL = 2;
    private List<Object> items;

    public HomeMoviesAdapter () {
    }

    public void addMovies (ArrayList<Object> movies) {
        if ( this.items == null ) {
            this.items = new ArrayList<> ();
        }

        items.addAll (movies);
        notifyItemRangeInserted (items.size () - movies.size (), movies.size ());
    }

    public void setItems (List<Object> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        switch ( type ) {
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
        if ( viewHolder instanceof HeaderItemHolder ) {
            bindHeader (((HeaderItemHolder) viewHolder), ((String) items.get (i)));
        }

        if ( viewHolder instanceof MovieItemHolder && i % 5 == 1 ) {
            bindMovie (((MovieItemHolder) viewHolder), ((Movie) items.get (i)));

        } else if ( viewHolder instanceof MovieItemHolder ) {
            bindMovie (((MovieItemHolder) viewHolder), ((Movie) items.get (i)), i % 5 == 4);
        }
    }

    private void bindMovie (MovieItemHolder viewHolder, Movie movie) {
        GlideApp.with (viewHolder.itemView)
                .load (Uri.parse (IMAGE + MEDIUM_BACKDROP + movie.backdropPath))
                .centerCrop ()
                .into (viewHolder.thumbnail);
        viewHolder.name.setText (movie.title);
    }

    private void bindMovie (MovieItemHolder viewHolder, Movie movie, boolean isLast) {
        GlideApp.with (viewHolder.itemView)
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .centerCrop ()
                .into (viewHolder.thumbnail);

        if ( isLast ) {
            viewHolder.viewAll.setVisibility (View.VISIBLE);
        } else {
            viewHolder.viewAll.setVisibility (View.GONE);
        }
    }

    private void bindHeader (HeaderItemHolder viewHolder, String header) {
        viewHolder.header.setText (header);
    }

    @Override
    public int getItemViewType (int position) {
        return items.get (position) instanceof String ? HEADER :
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

        public HeaderItemHolder (@NonNull View itemView) {
            super (itemView);
            header = (TextView) itemView;
        }
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @Nullable
        @BindView(R.id.viewAll)
        TextView viewAll;
        @Nullable
        @BindView(R.id.name)
        TextView name;

        public MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }
}
