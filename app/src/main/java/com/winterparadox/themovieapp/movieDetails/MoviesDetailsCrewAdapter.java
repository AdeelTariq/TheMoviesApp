package com.winterparadox.themovieapp.movieDetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.beans.CrewMember;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesDetailsCrewAdapter extends RecyclerView.Adapter<MoviesDetailsCrewAdapter
        .CrewItemHolder> {

    private List<CrewMember> items;
    private ClickListener listener;

    public MoviesDetailsCrewAdapter (ClickListener listener) {
        this.listener = listener;
    }

    public void setItems (List<CrewMember> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public CrewItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1 = inflater.inflate (R.layout.item_crew, viewGroup, false);
        return new CrewItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull CrewItemHolder itemHolder, int i) {

        CrewMember castMember = items.get (i);
        itemHolder.name.setText (castMember.name);
        itemHolder.job.setText (castMember.job);

        itemHolder.itemView.setOnClickListener (v -> listener.onCrewClick (castMember));

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class CrewItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.job) TextView job;

        CrewItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onCrewClick (CrewMember member);
    }
}
