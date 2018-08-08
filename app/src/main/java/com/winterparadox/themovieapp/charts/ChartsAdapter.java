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
import com.winterparadox.themovieapp.common.views.ErrorViewHolder;
import com.winterparadox.themovieapp.common.views.GradientColorFilterTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;

public class ChartsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final static Random rand = new Random ();
    private static final int ERROR = 0, CHART = 1;
    private final RequestOptions requestOptions;
    private List<Object> items;
    private ClickListener listener;
    private ErrorViewHolder.OnClickListener retryListener;

    ChartsAdapter (ClickListener listener, ErrorViewHolder.OnClickListener retryListener) {
        this.listener = listener;
        this.retryListener = retryListener;

        requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new GradientColorFilterTransformation ());

    }

    void setItems (List<Chart> charts) {
        this.items = new ArrayList<> ();
        this.items.addAll (charts);
        notifyDataSetChanged ();
    }

    public void setError (String message) {
        if ( this.items == null ) {
            this.items = new ArrayList<> ();
        }

        items.clear ();
        items.add (message);
        notifyDataSetChanged ();
    }

    @Override
    public int getItemViewType (int position) {
        return items.get (position) instanceof String ? ERROR : CHART;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

        if ( type == ERROR ) {
            View view0 = inflater.inflate (R.layout.item_error, viewGroup, false);
            return new ErrorViewHolder (view0, retryListener);
        } else {
            View view1 = inflater.inflate (R.layout.item_chart, viewGroup, false);
            return new ChartItemHolder (view1);
        }
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder itemHolder, int i) {
        if ( itemHolder instanceof ErrorViewHolder ) {
            ((ErrorViewHolder) itemHolder).error.setText (items.get (i).toString ());
        } else {
            Chart chart = (Chart) items.get (i);
            bindChart (((ChartItemHolder) itemHolder), chart);
        }
    }

    private void bindChart (ChartItemHolder itemHolder, Chart chart) {
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

    static class ChartItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.name) TextView title;

        ChartItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);

            int r = (int) (rand.nextFloat () * 255);
            int g = (int) (rand.nextFloat () * 255);
            int b = (int) (rand.nextFloat () * 255);

            int maxColor = 50;
            int threshold = 70;
            int minColor = 30;
            r = r > threshold ? r - maxColor : r + minColor;
            g = g > threshold ? g - maxColor : g + minColor;
            b = b > threshold ? b - maxColor : b + minColor;

            thumbnail.setBackgroundColor (Color.rgb (r, g, b));
        }
    }

    interface ClickListener {
        void onChartClick (Chart chart);
    }
}
