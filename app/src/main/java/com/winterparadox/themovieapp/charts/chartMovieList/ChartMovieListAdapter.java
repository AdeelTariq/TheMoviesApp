package com.winterparadox.themovieapp.charts.chartMovieList;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
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
import com.winterparadox.themovieapp.common.views.ErrorViewHolder;
import com.winterparadox.themovieapp.common.views.ProgressViewHolder;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class ChartMovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ERROR = 0, MOVIE = 1, PROGRESS = 2;
    private List<Object> items;
    private ClickListener listener;
    private ErrorViewHolder.OnClickListener retryListener;
    private boolean showProgress;

    ChartMovieListAdapter (ClickListener listener,
                           ErrorViewHolder.OnClickListener retryListener) {
        this.listener = listener;
        this.retryListener = retryListener;
    }

    void setItems (List<Movie> movies) {
        this.items = new ArrayList<> (movies);
        notifyDataSetChanged ();
    }

    public void addItems (List<Movie> movies) {
        this.items.addAll (movies);
        notifyItemRangeInserted (items.size () - movies.size (), movies.size ());
    }


    public void setError (String message) {
        if ( this.items == null ) {
            this.items = new ArrayList<> ();
        }

        items.clear ();
        items.add (message);
        notifyDataSetChanged ();
    }

    public synchronized void setProgress (boolean show) {
        showProgress = show;
        notifyItemChanged (items.size ());
    }


    @Override
    public int getItemViewType (int position) {
        if ( position == items.size () ) return PROGRESS;
        return items.get (position) instanceof String ? ERROR : MOVIE;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

        if ( type == ERROR ) {
            View view0 = inflater.inflate (R.layout.item_error, viewGroup, false);
            return new ErrorViewHolder (view0, retryListener);
        } else if ( type == PROGRESS ) {
            View view0 = inflater.inflate (R.layout.item_page_progress, viewGroup, false);
            return new ProgressViewHolder (view0);
        } else {
            View view1 = inflater.inflate (R.layout.item_movie_list,
                    viewGroup, false);
            return new MovieItemHolder (view1);
        }
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder itemHolder, int i) {
        if ( itemHolder instanceof ErrorViewHolder ) {
            ((ErrorViewHolder) itemHolder).error.setText (items.get (i).toString ());

        } else if ( itemHolder instanceof MovieItemHolder ) {
            Movie movie = (Movie) items.get (i);
            bindMovie (((MovieItemHolder) itemHolder), movie, String.valueOf (i + 1));

        }
        // else do nothing if it's the progress item
    }

    private void bindMovie (MovieItemHolder itemHolder, Movie movie, String number) {

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .centerCrop ()
                .into (itemHolder.thumbnail);

        itemHolder.thumbnail.setTransitionName (TransitionNames.MOVIE_POSTER + movie.id);

        itemHolder.title.setText (movie.title);
        itemHolder.plot.setText (movie.overview);

        itemHolder.itemView.setOnClickListener (v -> listener.onMovieClick (movie, itemHolder
                .thumbnail));

        itemHolder.number.setText (number);
    }

    @Override
    public int getItemCount () {
        // if showProgress is On then add one item at the bottom for progress
        return (items == null ? 0 : items.size ()) + (showProgress ? 1 : 0);
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.number) AppCompatTextView number;
        @BindView(R.id.plot) TextView plot;
        @BindView(R.id.favorite) LottieAnimationView hidden;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);

            hidden.setVisibility (View.GONE);
            number.setVisibility (View.VISIBLE);
        }
    }

    interface ClickListener {
        void onMovieClick (Movie movie, View element);
    }
}
