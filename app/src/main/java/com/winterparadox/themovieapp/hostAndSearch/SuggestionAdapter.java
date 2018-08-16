package com.winterparadox.themovieapp.hostAndSearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.ErrorViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> items;
    private ClickListener listener;

    SuggestionAdapter (ClickListener listener) {
        this.listener = listener;

    }

    void setItems (List<Movie> movies) {
        this.items = new ArrayList<> ();
        this.items.addAll (movies);
        notifyDataSetChanged ();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

        View view1 = inflater.inflate (R.layout.item_movie_suggestion,
                viewGroup, false);
        return new MovieItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder itemHolder, int i) {
        if ( itemHolder instanceof ErrorViewHolder ) {
            ((ErrorViewHolder) itemHolder).error.setText (items.get (i).toString ());
        } else {
            Movie chart = (Movie) items.get (i);
            bindMovie (((MovieItemHolder) itemHolder), chart);
        }
    }

    private void bindMovie (MovieItemHolder itemHolder, Movie movie) {
        itemHolder.title.setText (movie.title);

        itemHolder.itemView.setOnClickListener (v -> listener.onMovieSuggestionClick (movie));
    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    public void clear () {
        if ( items != null ) {
            items.clear ();
            notifyDataSetChanged ();
        }
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.title) TextView title;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onMovieSuggestionClick (Movie movie);
    }
}
