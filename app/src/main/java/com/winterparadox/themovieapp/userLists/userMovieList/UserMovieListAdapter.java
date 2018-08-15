package com.winterparadox.themovieapp.userLists.userMovieList;

import android.animation.Animator;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.winterparadox.themovieapp.common.views.TransitionNames;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class UserMovieListAdapter extends DragItemAdapter<Object, DragItemAdapter.ViewHolder> {

    private static final int ERROR = 0, MOVIE = 1;
    private ClickListener listener;
    private ErrorViewHolder.OnClickListener retryListener;

    UserMovieListAdapter (ClickListener listener,
                          ErrorViewHolder.OnClickListener retryListener) {
        this.listener = listener;
        this.retryListener = retryListener;
        setHasStableIds (true);
    }

    void setMovies (List<Movie> movies) {
        setItemList (new ArrayList<> (movies));
        notifyDataSetChanged ();
    }


    private void remove (int index) {
        this.getItemList ().remove (index);
        notifyItemRemoved (index);
    }

    public void setError (String message) {
        if ( this.getItemList () == null ) {
            this.setItemList (new ArrayList<> ());
        }

        getItemList ().clear ();
        getItemList ().add (message);
        notifyDataSetChanged ();
    }

    @Override
    public int getItemViewType (int position) {
        return getItemList ().get (position) instanceof String ? ERROR : MOVIE;
    }

    @Override
    public long getUniqueItemId (int position) {
        if ( getItemList ().get (position) instanceof Movie ) {
            return ((Movie) getItemList ().get (position)).id;
        }
        return getItemList ().get (position).hashCode ();
    }


    @NonNull
    @Override
    public DragItemAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

        if ( type == ERROR ) {
            View view0 = inflater.inflate (R.layout.item_error, viewGroup, false);
            return new ErrorViewHolder (view0, retryListener,
                    viewGroup.getContext ().getString (R.string.show_me));
        } else {
            View view1 = inflater.inflate (R.layout.item_movie_user_list,
                    viewGroup, false);
            return new MovieItemHolder (view1, listener, getItemList ());
        }
    }

    @Override
    public void onBindViewHolder (@NonNull DragItemAdapter.ViewHolder itemHolder, int i) {
        super.onBindViewHolder (itemHolder, i);
        if ( itemHolder instanceof ErrorViewHolder ) {
            ((ErrorViewHolder) itemHolder).error.setText (getItemList ().get (i).toString ());

        } else if ( itemHolder instanceof MovieItemHolder ) {
            Movie movie = (Movie) getItemList ().get (i);
            bindMovie (((MovieItemHolder) itemHolder), movie);

        }
    }

    private void bindMovie (MovieItemHolder itemHolder, Movie movie) {

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .centerCrop ()
                .into (itemHolder.thumbnail);

        itemHolder.thumbnail.setTransitionName (TransitionNames.MOVIE_POSTER + movie.id);

        itemHolder.title.setText (movie.title);
        itemHolder.plot.setText (movie.overview);

        itemHolder.delete.setOnClickListener (v -> {

            itemHolder.delete.addAnimatorListener (new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animator) {

                }

                @Override
                public void onAnimationEnd (Animator animator) {
                    listener.onDeleteClick (movie);
                    remove (itemHolder.getAdapterPosition ());
                }

                @Override
                public void onAnimationCancel (Animator animator) {

                }

                @Override
                public void onAnimationRepeat (Animator animator) {

                }
            });

            itemHolder.delete.playAnimation ();

        });
    }

    @Override
    public int getItemCount () {
        // if showProgress is On then add one item at the bottom for progress
        return getItemList () == null ? 0 : getItemList ().size ();
    }

    static class MovieItemHolder extends DragItemAdapter.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.plot) TextView plot;
        @BindView(R.id.delete) LottieAnimationView delete;
        private ClickListener listener;
        private List<Object> items;

        MovieItemHolder (@NonNull View itemView, ClickListener listener, List<Object> items) {
            super (itemView, R.id.item, true);
            this.listener = listener;
            this.items = items;
            ButterKnife.bind (this, itemView);
        }

        @Override
        public void onItemClicked (View view) {
            super.onItemClicked (view);
            listener.onMovieClick ((Movie) items.get (getAdapterPosition ()), thumbnail);
        }
    }

    interface ClickListener {
        void onMovieClick (Movie movie, View element);

        void onDeleteClick (Movie movie);
    }
}
