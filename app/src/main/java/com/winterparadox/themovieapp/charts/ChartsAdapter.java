package com.winterparadox.themovieapp.charts;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.views.GradientColorFilterTransformation;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter
        .MovieItemHolder> {


    private final static Random rand = new Random ();
    private final RequestOptions requestOptions;
    private List<Chart> items;
    private ClickListener listener;

    ChartsAdapter (ClickListener listener) {
        this.listener = listener;

        requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new GradientColorFilterTransformation ());

    }

    void setItems (List<Chart> items) {
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
        View view1 = inflater.inflate (R.layout.item_chart, viewGroup, false);
        return new MovieItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull MovieItemHolder itemHolder, int i) {

        Chart chart = items.get (i);

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + MEDIUM_BACKDROP + chart.backDropPath))
                .apply (requestOptions)
                .into (itemHolder.thumbnail);

        itemHolder.title.setText (chart.name);

        itemHolder.itemView.setOnClickListener (v -> listener.onChartClick (chart));
    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.name) TextView title;

        MovieItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);

            int r = (int) ((rand.nextFloat () - 0.3f) * 255);
            int g = (int) ((rand.nextFloat () - 0.3f) * 255);
            int b = (int) ((rand.nextFloat () - 0.3f) * 255);

            thumbnail.setBackgroundColor (Color.rgb (r, g, b));
        }
    }

    interface ClickListener {
        void onChartClick (Chart chart);
    }
}
