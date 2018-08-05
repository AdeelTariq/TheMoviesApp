package com.winterparadox.themovieapp.movieDetails;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.CastMember;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.LARGE_PROFILE;

public class MoviesDetailsCastAdapter extends RecyclerView.Adapter<MoviesDetailsCastAdapter
        .CastItemHolder> {

    private List<CastMember> items;
    private ClickListener listener;

    public MoviesDetailsCastAdapter (ClickListener listener) {
        this.listener = listener;
    }

    public void setItems (List<CastMember> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public CastItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1 = inflater.inflate (R.layout.item_cast, viewGroup, false);
        return new CastItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull CastItemHolder itemHolder, int i) {

        CastMember castMember = items.get (i);

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + LARGE_PROFILE + castMember.profilePath))
                .centerCrop ()
                .into (itemHolder.profile);
        itemHolder.name.setText (castMember.name);
        itemHolder.character.setText (castMember.character);

        itemHolder.itemView.setOnClickListener (v -> listener.onCastClick (castMember));

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class CastItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile) ImageView profile;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.character) TextView character;

        CastItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onCastClick (CastMember member);
    }
}
